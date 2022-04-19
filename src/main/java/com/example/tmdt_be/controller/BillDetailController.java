package com.example.tmdt_be.controller;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.service.BillDetailService;
import com.example.tmdt_be.service.sdi.*;
import com.example.tmdt_be.service.sdo.BillBySellerSdo;
import com.example.tmdt_be.service.sdo.BillDetailSdo;
import com.example.tmdt_be.service.sdo.BillDetailSellerSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.coong.web.response.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="bill")
public class BillDetailController {
    @Autowired
    BillDetailService billDetailService;

    @GetMapping(value="cart")
    ResponseEntity<List<BillBySellerSdo>> cart(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.getListBillBySeller(token, DataUtil.safeToLong(Const.PURCHASE_TYPE.ORDER)));
    }

    @GetMapping(value="listBill")
    ResponseEntity<PagedResponse<BillDetailSdo>> listBill(@RequestHeader("Authorization") String token,
                                                          @RequestParam(value="purchaseType", required = false) Long purchaseType,
                                                          @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<BillDetailSdo> result = billDetailService.getListBillByUserAndStatus(token, purchaseType, pageable);

        return ResponseEntity.ok(PagedResponse.builder().page(result).build());
    }

    @GetMapping(value="listBillOfSeller")
    ResponseEntity<PagedResponse<BillDetailSellerSdo>> listBillOfSeller(@RequestHeader("Authorization") String token,
                                                                        @RequestParam(value="purchaseType", required = false) Long purchaseType,
                                                                        @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<BillDetailSellerSdo> result = billDetailService.getListBillBySellerAndStatus(token, purchaseType, pageable);

        return ResponseEntity.ok(PagedResponse.builder().page(result).build());
    }


    @GetMapping(value="countTotalProductSold")
    ResponseEntity<Long> countTotalProductSold(@RequestParam(value = "productId", required = true) Long productId) {
        return ResponseEntity.ok(billDetailService.countTotalProductSold(productId));
    }

    @PostMapping(value="addToCart")
    ResponseEntity<Boolean> addToCart(@Valid @RequestBody AddToCartSdi sdi,
                                      @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.addToCart(token, sdi.getProductId(), sdi.getQuantity()));
    }

    @PutMapping(value="updateBillStatus")
    ResponseEntity<Boolean> updateBillStatus(@Valid @RequestBody UpdateBillStatusSdi sdi,
                                             @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.updateBillStatus(token, sdi.getBillId(), sdi.getStatusId()));
    }

    @PutMapping(value="updateQuantityProductInCart")
    ResponseEntity<Boolean> updateQuantityProductInCart(@Valid @RequestBody UpdateQuantityProductInCartSdi sdi,
                                                        @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.updateQuantityProductInCart(token, sdi.getBillId(), sdi.getQuantity()));
    }

    @DeleteMapping(value="deleteProductInCart")
    ResponseEntity<Boolean> deleteProductInCart(@Valid @RequestParam Long billId,
                                                        @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.deleteProductInCart(token, billId));
    }

    @PostMapping(value="deleteProductsInCart")
    ResponseEntity<Boolean> deleteProductsInCart(@Valid @RequestBody DeleteProductsInCartSdi sdi,
                                                 @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.deleteProductsInCart(token, sdi));
    }

    @PostMapping(value="buyProducts")
    ResponseEntity<Boolean> buyProducts(@Valid @RequestBody BuyProductsSdi sdi,
                                        @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.buyProducts(token, sdi));
    }
}
