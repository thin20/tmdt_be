package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.BillDetailService;
import com.example.tmdt_be.service.ProductService;
import com.example.tmdt_be.service.ProductUserLikedService;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static BillDetailService billDetailService;
    private static ProductUserLikedService productUserLikedService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(BillDetailService billDetailService, ProductUserLikedService productUserLikedService) {
        this.billDetailService = billDetailService;
        this.productUserLikedService = productUserLikedService;
    }

    @Override
    public Page<ProductSdo> searchListProduct(SearchProductSdi sdi) {
        String sortType = sdi.getSortType();
        String orderType = sdi.getOrderType();
        Pageable pageable = sdi.getPageable();
        List<Product> listProduct = productRepo.searchListProduct(sdi);

        List<ProductSdo> listSdo = new ArrayList<>();
        for (Product product : listProduct) {
            ProductSdo productSdo = product.toProductSdo();

            Long currentUserId = sdi.getCurrentUserId();
            Long productId = product.getId();

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

            listSdo.add(productSdo);
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
}
