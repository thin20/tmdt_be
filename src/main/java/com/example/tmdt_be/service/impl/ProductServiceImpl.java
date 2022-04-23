package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.domain.ImageProduct;
import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.*;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdi.CreateProductSdi;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import com.example.tmdt_be.service.sdi.SearchProductBySellerSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
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

        List<ImageProduct> depicted = imageProductService.getListImageProductByProductId(productId);
        productDetailSdo.setDepicted(depicted);

        ProductSdo productSdo = productRepo.getProductById(productId);

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
        List<MultipartFile> images = sdi.getImages();
        String description = sdi.getDescription();

        if (DataUtil.isNullOrZero(categoryId) | DataUtil.isNullOrEmpty(productName) | DataUtil.isNullOrZero(price) | DataUtil.isNullOrZero(discount) | DataUtil.isNullOrEmpty(images)) {
            throw new AppException("API-PRD005", "Thêm mới sản phẩm thất bại!");
        }

        MultipartFile mainImage = images.get(0);
        String imagePath = amazonUploadService.uploadFile(mainImage);

        Product product = new Product();
        product.setName(productName);
        product.setCategoryId(categoryId);
        product.setUserId(user.getId());
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setNumberOfStar(5L);
        product.setImage(imagePath);
        product.setIsSell(1);
        product.setCreatedAt(new Date());

        product = productRepo.save(product);
        if (DataUtil.isNullOrZero(product.getId())) {
            throw new AppException("API-PRD005", "Thêm mới sản phẩm thất bại!");
        }
        Long productId = product.getId();

        // Save images products
        for (int i = 1; i <= images.size(); i++) {
            String imgPath = amazonUploadService.uploadFile(images.get(i));
            imageProductService.saveImageProduct(productId, imagePath);
        }

        return product.toProductSdo();
    }
}
