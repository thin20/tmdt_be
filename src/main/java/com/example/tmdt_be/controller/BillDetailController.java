package com.example.tmdt_be.controller;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.service.BillDetailService;
import com.example.tmdt_be.service.sdi.AddToCartSdi;
import com.example.tmdt_be.service.sdo.BillBySellerSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResponseEntity<Boolean> updateBillStatus(@Valid @RequestBody Long billId, Long statusId,
                                       @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(billDetailService.updateBillStatus(token, billId, statusId));
    }
}
