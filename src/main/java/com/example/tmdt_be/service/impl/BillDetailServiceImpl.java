package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.repository.BillDetailRepo;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.AddressService;
import com.example.tmdt_be.service.BillDetailService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdi.BuyProductsSdi;
import com.example.tmdt_be.service.sdi.DeleteProductsInCartSdi;
import com.example.tmdt_be.service.sdo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BillDetailServiceImpl implements BillDetailService {
    private static UserService userService;
    private static AddressService addressService;

    @Autowired
    BillDetailRepo billDetailRepo;

    @Autowired
    ProductRepo productRepo;

    public BillDetailServiceImpl(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @Override
    public Long countTotalProductSold(Long productId) {
        return billDetailRepo.countTotalProductSold(productId);
    }

    @Override
    public Long countBillDetailOfUserAndProduct(Long userId, Long productId) {
        return billDetailRepo.countBillDetailOfUserAndProduct(userId, productId);
    }

    @Override
    public List<BillBySellerSdo> getListBillBySeller(String token,Long purchaseType) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        List<IdBillDetailSdo> listIdBillDetail = billDetailRepo.getListIdBillDetail(userId, purchaseType);
        HashMap<Long, List<BillDetailSdo>> hashMap = new HashMap<Long, List<BillDetailSdo>>();
        for (IdBillDetailSdo item : listIdBillDetail) {
            BillDetailSdo billDetailSdo = new BillDetailSdo();

            billDetailSdo.setBillId(item.getBillId());

            billDetailSdo.setQuantity(item.getQuantity());

            if (!DataUtil.isNullOrZero(item.getAddressId())) {
                Address address = addressService.getAddressById(item.getAddressId());
                billDetailSdo.setAddress(address.getAddress());
            }

            if (!DataUtil.isNullOrZero(item.getProductId())) {
                ProductSdo productSdo = productRepo.getProductById(item.getProductId());
                billDetailSdo.setProduct(productSdo);
            }

            if (!hashMap.containsKey(item.getSellerId())) {
                List<BillDetailSdo> list = new ArrayList<BillDetailSdo>();

                list.add(billDetailSdo);

                hashMap.put(item.getSellerId(), list);
            } else {
                hashMap.get(item.getSellerId()).add(billDetailSdo);
            }
        }

        List<BillBySellerSdo> listBillBySeller = new ArrayList<BillBySellerSdo>();

        for(Map.Entry<Long, List<BillDetailSdo>> entry : hashMap.entrySet()) {

            Long sellerId = entry.getKey();
            List<BillDetailSdo> value = entry.getValue();

            if (DataUtil.isNullOrZero(sellerId)) break;

            UserSdo user = userService.findById(sellerId);

            BillBySellerSdo billBySellerSdo = new BillBySellerSdo();
            billBySellerSdo.setSellerId(sellerId);
            billBySellerSdo.setSellerName(user.getFullName());
            billBySellerSdo.setBills(value);

            listBillBySeller.add(billBySellerSdo);
        }

        return listBillBySeller;
    }

    @Override
    public Page<BillDetailSdo> getListBillByUserAndStatus(String token, Long purchaseType, Pageable pageable) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        if (DataUtil.isNullOrZero(purchaseType)) { List<String> errParams = new ArrayList<>();
            errParams.add(DataUtil.safeToString(purchaseType));
            throw new AppException("API-BILL003", "Không tồn tại trạng thái đơn hàng = " + purchaseType);
        }

        List<BillDetailSdo> listBillDetail = new ArrayList<>();

        List<IdBillDetailSdo> listIdBillDetail = billDetailRepo.getListIdBillDetailPagination(userId, purchaseType, pageable);
        listIdBillDetail.forEach(item -> {
            BillDetailSdo billDetail = new BillDetailSdo();
            billDetail.setBillId(item.getBillId());
            billDetail.setQuantity(item.getQuantity());

            if (!DataUtil.isNullOrZero(item.getAddressId())) {
                Address address = addressService.getAddressById(item.getAddressId());
                billDetail.setAddress(address.getAddress());
            }

            if (!DataUtil.isNullOrZero(item.getProductId())) {
                ProductSdo product = productRepo.getProductById(item.getProductId());
                billDetail.setProduct(product);
            }

            listBillDetail.add(billDetail);
        });

        Long count = billDetailRepo.countBillDetailOfUserAndProduct(userId, purchaseType);

        return new PageImpl<>(listBillDetail, pageable , count);
    }

    @Override
    public BillDetail getBillById(Long billId, Long statusId) {
        if (DataUtil.isNullOrZero(billId) | DataUtil.isNullOrZero(statusId)) {
            return null;
        }
        return billDetailRepo.getBillById(billId, statusId);
    }

    @Override
    public BillDetail getBillByUserAndProduct(Long userId, Long productId) {
        if (DataUtil.isNullOrZero(userId) | DataUtil.isNullOrZero(productId)) return null;
        return getBillByUserAndProduct(userId, productId);
    }

    @Override
    public Boolean addToCart(String token, Long productId, Long quantity) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        BillDetail billDetail = new BillDetail();
        billDetail.setUserId(userId);

        ProductSdo productSdo = productRepo.getProductById(productId);
        if (!DataUtil.isNullOrZero(productSdo.getId())) {
            billDetail.setProductId(productId);
        } else {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        }

        if (productSdo.getIsSell() == 0) {
            throw new AppException("API-PRD002", "Sản phẩm hiện không được bán nữa!");
        }

        quantity = !DataUtil.isNullOrZero(quantity) ? quantity : 1;

        BillDetail billDetail1 = billDetailRepo.getBillByUserAndProduct(userId, productId, Const.PURCHASE_TYPE.ORDER);
        if (!DataUtil.isNullOrZero(billDetail1.getId())) {
            quantity = quantity + billDetail1.getQuantity();
            billDetail = billDetail1;
            billDetail.setUpdatedAt(new Date());
        } else {
            billDetail.setUserId(userId);
            billDetail.setProductId(productId);
            billDetail.setStatusId(1L);
            billDetail.setCreatedAt(new Date());
        }

        billDetail.setQuantity(quantity);

        BillDetail bd = billDetailRepo.save(billDetail);
        return !DataUtil.isNullOrZero(bd.getId());
    }

    @Override
    public Boolean updateBillStatus(String token, Long billId, Long statusId) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(billId) | DataUtil.isNullOrZero(statusId)) throw new AppException("API-BILL001", "Cập nhật trạng thái đơn hàng thất bại!");

        Long userId = userService.getUserIdByBearerToken(token);

        BillDetail billDetail = billDetailRepo.findById(billId).get();
        if (DataUtil.isNullOrZero(billDetail.getId())) {
            throw new AppException("API-BILL002", "Đơn hàng không tồn tại!");
        }

        billDetail.setStatusId(statusId);
        billDetail.setUpdatedAt(new Date());

        Product product = productRepo.findById(billDetail.getProductId()).get();
        if (product.getIsSell() == 0) {
            throw new AppException("API-PRD002", "Sản phẩm hiện không được bán nữa!");
        }
        if (DataUtil.isNullOrZero(product.getId())) throw new AppException("API-BILL006", "Không tồn tại sản phẩm trong đơn hàng!");
        Boolean isClient = (userId == billDetail.getUserId());
        Boolean isAdmin = (userId == product.getUserId());

        if (statusId == Const.PURCHASE_TYPE.DELIVERING) {
            if (!isAdmin) throw new AppException("API-BILL004", "Không có quyền cập nhật trạng thái đơn hàng!");
            if (billDetail.getQuantity() > product.getQuantity()) throw new AppException("API-BILL005", "Số lượng sản phẩm trong đơn hàng lớn hơn số lượng sản phẩm có sẵn trong kho!");
            if (!DataUtil.isNullOrZero(product.getIsSell())) throw new AppException("API-PRD002", "Sản phẩm hiện không được bán nữa!");
            Long quantity = product.getQuantity() - billDetail.getQuantity();
            product.setQuantity(quantity);
            product.setCreatedAt(new Date());
            productRepo.save(product);
        } else if (statusId == Const.PURCHASE_TYPE.ORDER | statusId == Const.PURCHASE_TYPE.WAIT_CONFIRM | statusId == Const.PURCHASE_TYPE.DELIVERED | statusId == Const.PURCHASE_TYPE.CANCELED) {
            if (!isClient) throw new AppException("API-BILL004", "Không có quyền cập nhật trạng thái đơn hàng!");
        } else if (statusId == Const.PURCHASE_TYPE.WAIT_TAKE) {
            if (!isAdmin) throw new AppException("API-BILL004", "Không có quyền cập nhật trạng thái đơn hàng!");
        } else {
            List<String> errParams = new ArrayList<>();
            errParams.add(DataUtil.safeToString(statusId));
            throw new AppException("API-BILL003", "Không tồn tại trạng thái đơn hàng = " + statusId);
        }

        BillDetail billDetail1 = billDetailRepo.save(billDetail);

        return !DataUtil.isNullOrZero(billDetail1.getId());
    }

    @Override
    public Boolean updateQuantityProductInCart(String token, Long billId, Long quantity) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(billId) || quantity == null) {
            throw new AppException("API-BILL007", "Cập nhật số lượng sản phẩm trong giỏ hàng thất bại!");
        }

        Long userId = userService.getUserIdByBearerToken(token);

        BillDetail billDetail = billDetailRepo.findBillByIdAndStatus(billId, Const.PURCHASE_TYPE.ORDER).get();
        if (DataUtil.isNullOrZero(billDetail.getId())) {
            throw new AppException("API-BILL002", "Đơn hàng không tồn tại!");
        }

        if (userId != billDetail.getUserId()) {
            throw new AppException("API-BILL008", "Không có quyền thay đổi số lượng sản phẩm trong giỏ hàng!");
        }

        if (quantity == 0) {
            return billDetailRepo.deleteProductInCart(billDetail.getId());
        } else {
            return billDetailRepo.updateQuantityProductInCart(billDetail.getId(), quantity);
        }
    }

    @Override
    public Boolean deleteProductInCart(String token, Long billId) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(billId)) {
            throw new AppException("API-BILL009", "Xóa sản phẩm trong giỏ hàng thất bại!");
        }

        Long userId = userService.getUserIdByBearerToken(token);

        BillDetail billDetail = billDetailRepo.findBillByIdAndStatus(billId, Const.PURCHASE_TYPE.ORDER).get();
        if (DataUtil.isNullOrZero(billDetail.getId())) {
            throw new AppException("API-BILL002", "Đơn hàng không tồn tại!");
        }

        if (userId != billDetail.getUserId()) {
            throw new AppException("API-BILL010", "Không có quyền xóa sản phẩm trong giỏ hàng!");
        }

        return billDetailRepo.deleteProductInCart(billDetail.getId());
    }

    @Override
    public Boolean deleteProductsInCart(String token, DeleteProductsInCartSdi sdi) throws JsonProcessingException {
        List<Long> billIds = sdi.getBillIds();
        if (DataUtil.isNullOrEmpty(billIds)) {
            throw new AppException("API-BILL009", "Xóa sản phẩm trong giỏ hàng thất bại!");
        }

        Long userId = userService.getUserIdByBearerToken(token);

        billIds.forEach(billId -> {
            BillDetail billDetail = billDetailRepo.findBillByIdAndStatus(billId, Const.PURCHASE_TYPE.ORDER).get();
            if (DataUtil.isNullOrZero(billDetail.getId())) {
                throw new AppException("API-BILL002", "Đơn hàng không tồn tại!");
            }

            if (userId != billDetail.getUserId()) {
                throw new AppException("API-BILL010", "Không có quyền xóa sản phẩm trong giỏ hàng!");
            }

            billDetailRepo.deleteProductInCart(billDetail.getId());
        });

        return true;
    }

    @Override
    public Boolean buyProducts(String token, BuyProductsSdi sdi) throws JsonProcessingException {
        List<Long> billIds = sdi.getBillIds();
        Long addressId = sdi.getAddressId();

        if (DataUtil.isNullOrEmpty(billIds)) {
            throw new AppException("API-BILL012", "Mua sản phẩm thất bại!");
        }

        Long userId = userService.getUserIdByBearerToken(token);

        if (DataUtil.isNullOrZero(addressId)) {
            Address address = addressService.getAddressDefault(userId);
            addressId = address.getId();
        }

        Long finalAddressId = addressId;
        billIds.forEach(billId -> {
            BillDetail billDetail = billDetailRepo.getBillById(billId, Const.PURCHASE_TYPE.ORDER);
            if (DataUtil.isNullOrZero(billDetail.getId())) {
                throw new AppException("API-BILL002", "Đơn hàng không tồn tại!");
            }

            if (userId != billDetail.getUserId()) {
                throw new AppException("API-BILL011", "Không có quyền mua sản phẩm trong giỏ hàng!");
            }

            Product product = productRepo.findById(billDetail.getProductId()).get();
            if (DataUtil.isNullOrZero(product.getId())) {
                throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
            }

            if (product.getIsSell() == 0) {
                throw new AppException("API-PRD002", "Sản phẩm hiện không được bán nữa!");
            }

            Long quantityBuy = billDetail.getQuantity();
            if (product.getQuantity() < quantityBuy) {
                List<String> errParams = new ArrayList<>();
                errParams.add(DataUtil.safeToString(product.getQuantity()));
                throw new AppException("API-PRD003", "Số lượng sản phẩm trong giỏ hàng chỉ còn " + product.getQuantity() +" sản phẩm!");
            }

            product.setQuantity(billDetail.getQuantity() - quantityBuy);
            productRepo.save(product);

            billDetail.setStatusId(Const.PURCHASE_TYPE.WAIT_CONFIRM);
            billDetail.setAddressId(finalAddressId);
            billDetailRepo.save(billDetail);

        });

        return true;
    }
}
