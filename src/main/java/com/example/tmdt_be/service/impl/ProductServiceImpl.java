package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.domain.ImageProduct;
import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.*;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdi.*;
import com.example.tmdt_be.service.sdo.ProductDetailSdo;
import com.example.tmdt_be.service.sdo.ProductSdo;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static BillDetailService billDetailService;
    private static ProductUserLikedService productUserLikedService;
    private static ImageProductService imageProductService;
    private static CategoryService categoryService;
    private static CommentService commentService;
    private static UserService userService;
    private static AmazonUploadService amazonUploadService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(BillDetailService billDetailService,
                              ProductUserLikedService productUserLikedService,
                              ImageProductService imageProductService,
                              CategoryService categoryService,
                              CommentService commentService,
                              UserService userService,
                              AmazonUploadService amazonUploadService) {
        this.billDetailService = billDetailService;
        this.productUserLikedService = productUserLikedService;
        this.imageProductService = imageProductService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.userService = userService;
        this.amazonUploadService = amazonUploadService;
    }

    @Override
    public Page<ProductSdo> searchListProduct(SearchProductSdi sdi) {
        String sortType = sdi.getSortType();
        String orderType = sdi.getOrderType();
        Pageable pageable = sdi.getPageable();
        List<ProductSdo> listSdo = productRepo.searchListProduct(sdi);

        for (ProductSdo productSdo : listSdo) {

            Long currentUserId = sdi.getCurrentUserId();
            Long productId = productSdo.getId();

            Long sold = billDetailService.countTotalProductSold(productId);
            productSdo.setSold(sold);

            Long totalLikedOfProduct = productUserLikedService.getTotalLikedOfProduct(productId);
            productSdo.setTotalLiked(totalLikedOfProduct);

            Boolean isLiked = false;
            if (!DataUtil.isNullOrZero(currentUserId)) {
                LikeProductSdi lpSdi = new LikeProductSdi();
                lpSdi.setUserId(currentUserId);
                lpSdi.setProductId(productId);
                isLiked = productUserLikedService.isUserLikedProduct(lpSdi);
            }
            productSdo.setIsLiked(isLiked);

        }

        // Sort list product
        if (sortType.equals(Const.SORT_TYPE.BEST_SALE)) {
            Collections.sort(listSdo, (a, b) -> {
                if (orderType.equals(Const.ORDER_TYPE.ASC)) {
                    return (int) (a.getSold() - b.getSold());
                } else {
                    return (int) (b.getSold() - a.getSold());
                }
            });
        }

        if (sortType.equals(Const.SORT_TYPE.FAVOURITE)) {
            Collections.sort(listSdo, (a, b) -> {
                if (orderType.equals(Const.ORDER_TYPE.ASC)) {
                    return (int) (a.getTotalLiked() - b.getTotalLiked());
                } else {
                    return (int) (b.getTotalLiked() - a.getTotalLiked());
                }
            });
        }

        Long countItem = productRepo.countItemListProduct(sdi);

        return new PageImpl<>(listSdo, pageable, countItem);
    }

    @Override
    public ProductDetailSdo getProductDetail(Long currentUserId, Long productId) {
        if (DataUtil.isNullOrZero(productId)) {
            return null;
        }
        ProductDetailSdo productDetailSdo = new ProductDetailSdo();


        ProductSdo productSdo = productRepo.getProductById(productId);

        List<ImageProduct> depicted = imageProductService.getListImageProductByProductId(productId);
        ImageProduct mainImage = new ImageProduct();
        mainImage.setId(-1L);
        mainImage.setProductId(productId);
        mainImage.setPath(productSdo.getImage());

        depicted.add(0, mainImage);
        productDetailSdo.setDepicted(depicted);

        Long sold = billDetailService.countTotalProductSold(productId);
        productSdo.setSold(sold);

        Long totalLikedOfProduct = productUserLikedService.getTotalLikedOfProduct(productId);
        productSdo.setTotalLiked(totalLikedOfProduct);

        Boolean isLiked = false;
        if (!DataUtil.isNullOrZero(currentUserId)) {
            LikeProductSdi lpSdi = new LikeProductSdi();
            lpSdi.setUserId(currentUserId);
            lpSdi.setProductId(productId);
            isLiked = productUserLikedService.isUserLikedProduct(lpSdi);
        }
        productSdo.setIsLiked(isLiked);

        Long categoryId = productSdo.getCategoryId();
        if (!DataUtil.isNullOrZero(categoryId)) {
            List<Category> categories = categoryService.getAllParentOfCategory(categoryId);
            productSdo.setCategories(categories);
        }

        Boolean canComment = false;
        if (!DataUtil.isNullOrZero(currentUserId)) {
            Long countBillDetail = billDetailService.countBillDetailOfUserAndProduct(currentUserId, productId);
            if (!DataUtil.isNullOrZero(countBillDetail)) {
                Long countComment = commentService.countCommentOfUserAndProduct(currentUserId, productId);
                if (DataUtil.isNullOrZero(countComment)) {
                    canComment = true;
                }
            }
        }
        productSdo.setCanComment(canComment);

        productDetailSdo.setProductDetail(productSdo);

        return productDetailSdo;
    }

    @Override
    public ProductSdo getProductById(Long productId) {

        ProductSdo productSdo = productRepo.getProductById(productId);

        Long sold = billDetailService.countTotalProductSold(productId);
        productSdo.setSold(sold);

        Long totalLikedOfProduct = productUserLikedService.getTotalLikedOfProduct(productId);
        productSdo.setTotalLiked(totalLikedOfProduct);

        return productRepo.getProductById(productId);
    }

    @Override
    public Page<ProductSdo> searchListProductBySeller(String token, SearchProductBySellerSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);
        Pageable pageable = sdi.getPageable();

        List<ProductSdo> listProduct = productRepo.searchListProductBySeller(userId, sdi);
        Long countItem = productRepo.countItemListProductBySeller(userId,sdi);

        listProduct.forEach(product -> {
            Long sold = billDetailService.countTotalProductSold(product.getId());
            product.setSold(sold);
        });

        return new PageImpl<>(listProduct, pageable, countItem);
    }

    @Override
    public ProductSdo createProduct(String token, CreateProductSdi sdi) throws JsonProcessingException {
        UserSdo user = userService.getUserByBearerToken(token);
        Long categoryId = sdi.getCategoryId();
        String productName = sdi.getProductName();
        Long quantity = sdi.getQuantity();
        Double price = sdi.getPrice();
        Long discount = sdi.getDiscount();
        List<String> imagesPath = sdi.getImagesPath();
        String description = sdi.getDescription();

        if (DataUtil.isNullOrZero(categoryId) | DataUtil.isNullOrEmpty(productName) | DataUtil.isNullOrZero(price) | DataUtil.isNullOrZero(quantity) | discount == null | DataUtil.isNullOrEmpty(imagesPath)) {
            throw new AppException("API-PRD005", "Thêm mới sản phẩm thất bại!");
        }

        String imagePath = imagesPath.get(0);

        Product product = new Product();
        product.setName(productName);
        product.setCategoryId(categoryId);
        product.setUserId(user.getId());
        product.setQuantity(quantity);
        product.setDiscount(discount);
        product.setPrice(price);
        product.setDescription(description);
        product.setTitle(productName);
        product.setNumberOfStar(5L);
        product.setImage(imagePath);
        product.setIsSell(1);
        product.setVisit(0L);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

            product = productRepo.save(product);
        if (DataUtil.isNullOrZero(product.getId())) {
            throw new AppException("API-PRD005", "Thêm mới sản phẩm thất bại!");
        }
        Long productId = product.getId();

        // Save images products
        if (imagesPath.size() > 1) {
            for (int i = 1; i < imagesPath.size(); i++) {
                imageProductService.saveImageProduct(productId, imagesPath.get(i));
            }
        }

        return product.toProductSdo();
    }

    @Override
    public ProductSdo updateProduct(String token, UpdateProductSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        Long productId = sdi.getProductId();
        String productName = sdi.getProductName();
        Double price = sdi.getPrice();
        Long quantity = sdi.getQuantity();
        Long discount = sdi.getDiscount();
        String imagePath = sdi.getImagePath();
        String description = sdi.getDescription();
        List<Long> fileIdRemove = sdi.getFileIdRemove();
        List<String> imagesPath = sdi.getImagesPath();


        if (DataUtil.isNullOrZero(productId) | DataUtil.isNullOrEmpty(productName) | DataUtil.isNullOrZero(price) | DataUtil.isNullOrZero(quantity) | discount == null | DataUtil.isNullOrEmpty(imagePath)) {
            throw new AppException("API-PRD008", "Cập nhật sản phẩm thất bại!");
        }

        Product product = productRepo.findById(productId).orElseGet(() -> {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        });

        if (product.getUserId() != userId) {
            throw new AppException("API-PRD009", "Không có quyền cập nhật sản phẩm!");
        }

        product.setName(productName);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDiscount(discount);
        product.setImage(imagePath);
        product.setDescription(description);
        product.setUpdatedAt(new Date());

        productRepo.save(product);

        fileIdRemove.forEach(id -> {
            try {
                imageProductService.removeImageProductById(id);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });


        if (imagesPath.size() > 0) {
            for (int i = 0; i < imagesPath.size(); i++) {
                imageProductService.saveImageProduct(productId, imagesPath.get(i));
            }
        }

        return product.toProductSdo();
    }

    @Override
    public Boolean changeImageProduct(String token, ChangeImageProductSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        Long productId = sdi.getProductId();
        MultipartFile newImage = sdi.getNewImage();
        String oldPathImage = sdi.getOldPathImage();

        if (DataUtil.isNullOrZero(productId) | newImage == null | DataUtil.isNullOrEmpty(oldPathImage)) {
            throw new AppException("API-PRD012", "Cập nhật ảnh của sản phẩm thất bại!");
        }

        Product product = productRepo.findById(productId).orElseGet(() -> {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        });

        if (product.getUserId() != userId) {
            throw new AppException("API-PRD011", "Không có quyền cập nhật ảnh của sản phẩm!");
        }

        String newPathImage = amazonUploadService.uploadFile(newImage);

        if (oldPathImage.equals(product.getImage())) {
            product.setImage(newPathImage);
            product.setUpdatedAt(new Date());
            productRepo.save(product);
        } else {
            imageProductService.removeImageProduct(productId, oldPathImage);
            imageProductService.saveImageProduct(productId, newPathImage);
        }

        return true;
    }

    @Override
    public Boolean removeImageProduct(String token, RemoveImageProductSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        Long productId = sdi.getProductId();
        String pathImage = sdi.getPathImage();

        if (DataUtil.isNullOrZero(productId) | DataUtil.isNullOrEmpty(pathImage)) {
            throw new AppException("API-PRD010", "Xóa ảnh của sản phẩm thất bại!");
        }

        Product product = productRepo.findById(productId).orElseGet(() -> {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        });

        if (product.getUserId() == userId) {
            throw new AppException("API-PRD013", "Không có quyền xóa ảnh của sản phẩm!");
        }

        if (pathImage.equals(product.getImage())) {
            throw new AppException("API-PRD014", "Không thể xóa ảnh chính của sản phẩm!");
        }

        return imageProductService.removeImageProduct(productId, pathImage);
    }

    @Override
    public Boolean changeStatusSell(String token, ChangeStatusSellSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        Long productId = sdi.getProductId();
        if (DataUtil.isNullOrZero(productId)) {
            throw new AppException("API-PRD006", "Đổi trạng thái sản phẩm thành công!");
        }

        Product product = productRepo.findById(productId).orElseGet(() -> {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        });

        if (product.getUserId() != userId) {
            throw new AppException("API-PRD007", "Không có quyền cập nhật trạng thái sản phẩm!");
        }

        Integer isSell = product.getIsSell();
        if (DataUtil.isNullOrZero(isSell)) {
            isSell = 1;
        } else {
            isSell = 0;
        }

        product.setIsSell(isSell);
        productRepo.save(product);
        
        return true;
    }

    @Override
    public Boolean addProductVisit(Long productId) throws JsonProcessingException {
        Product product = productRepo.findById(productId).orElseGet(() -> {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        });

        Long visit = product.getVisit() + 1L;
        product.setVisit(visit);

        productRepo.save(product);

        return true;
    }
}
