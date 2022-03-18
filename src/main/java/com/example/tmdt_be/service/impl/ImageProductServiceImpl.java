package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.domain.ImageProduct;
import com.example.tmdt_be.repository.ImageProductRepo;
import com.example.tmdt_be.service.ImageProductService;
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
}
