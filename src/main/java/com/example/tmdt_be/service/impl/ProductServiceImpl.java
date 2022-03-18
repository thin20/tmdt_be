package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.domain.ImageProduct;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.*;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductDetailSdo;
import com.example.tmdt_be.service.sdo.ProductSdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static BillDetailService billDetailService;
    private static ProductUserLikedService productUserLikedService;
    private static ImageProductService imageProductService;
    private static CategoryService categoryService;
    private static CommentService commentService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(BillDetailService billDetailService, ProductUserLikedService productUserLikedService, ImageProductService imageProductService, CategoryService categoryService, CommentService commentService) {
        this.billDetailService = billDetailService;
        this.productUserLikedService = productUserLikedService;
        this.imageProductService = imageProductService;
        this.categoryService = categoryService;
        this.commentService = commentService;
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
}
