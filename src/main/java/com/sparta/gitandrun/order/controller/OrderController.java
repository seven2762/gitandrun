package com.sparta.gitandrun.order.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.order.dto.req.CreateOrderReqDto;
import com.sparta.gitandrun.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /*
        주문생성 메서드
        1. @Secured("CUSTOMER") 추후 접근 권한 처리
        2. 매개변수로 User 추가할 것
    */
    @PostMapping
    public ResponseEntity<ApiResDto> createOrder(@RequestBody CreateOrderReqDto dto) {

        orderService.createOrder(dto);

        return ResponseEntity.ok().body(new ApiResDto("주문 완료", HttpStatus.OK.value()));
    }

    /*
       주문취소 메서드
       1. @Secured("CUSTOMER, ADMIN") 추후 접근 권한 처리
       2. 매개변수로 User 추가할 것
   */
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResDto> cancelOrder(@PathVariable("orderId") Long orderId) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.ok().body(new ApiResDto("주문 취소 완료", HttpStatus.OK.value()));
    }
}
