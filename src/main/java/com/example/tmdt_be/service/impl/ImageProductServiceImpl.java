package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.ImageProduct;
import com.example.tmdt_be.repository.ImageProductRepo;
import com.example.tmdt_be.service.ImageProductService;
import com.example.tmdt_be.service.exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageProductServiceImpl implements ImageProductService {
    @Autowired
    ImageProductRepo imageProductRepo;

    @Override
    public Optional<ImageProduct> getImageProductById(Long id) {
        return imageProductRepo.findById(id);
    }

    @Override
    public List<ImageProduct> getListImageProductByProductId(Long productId) {
        return imageProductRepo.getListImageProductByProductId(productId);
    }

    @Override
    public Boolean saveImageProduct(Long productId, String path) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(productId) | DataUtil.isNullOrEmpty(path)) {
            throw new AppException("API-PRD004", "Thêm mới ảnh mô tả sản phẩm thất bại!");
        }

        ImageProduct imageProduct = new ImageProduct();
        imageProduct.setProductId(productId);
        imageProduct.setPath(path);
        imageProductRepo.save(imageProduct);

        return true;
    }

    @Override
    public Boolean removeImageProduct(Long productId, String path) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(productId) | DataUtil.isNullOrEmpty(path)) {
            throw new AppException("API-PRD010", "Xóa ảnh của sản phẩm thất bại!");
        }

        return imageProductRepo.removeImageProductByProductIdAndPath(productId, path);
    }

    @Override
    public Boolean removeImageProductById(Long id) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(id)) {
            throw new AppException("API-PRD010", "Xóa ảnh của sản phẩm thất bại!");
        }

        ImageProduct imageProduct = imageProductRepo.findById(id).orElseGet(() -> {
            throw new AppException("API-PRD010", "Không tồn tại ảnh!");
        });

        imageProductRepo.delete(imageProduct);

        return true;
    }
}
