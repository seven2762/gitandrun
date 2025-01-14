package com.sparta.gitandrun.order.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByCustomerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByManagerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByOwnerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderPostDTO;
import com.sparta.gitandrun.order.dto.res.*;
import com.sparta.gitandrun.order.service.OrderService;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /*
        주문 생성
    */
    @Secured("ROLE_CUSTOMER")
    @PostMapping("/orders")
    public ResponseEntity<ApiResDto> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestBody ReqOrderPostDTO dto) {

        orderService.createOrder(userDetails.getUser(), dto);

        return ResponseEntity.ok().body(new ApiResDto("주문 완료", HttpStatus.OK.value()));
    }

    /*
       주문 전체 조회
       - 유저 본인의 주문 내역을 조회할 수 있음.
   */
    @Secured("ROLE_CUSTOMER")
    @PostMapping("/customer/orders/search")
    public ResponseEntity<ResDto<ResOrderGetByCustomerDTO>> readByCustomer(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                           @RequestBody ReqOrderCondByCustomerDTO cond,
                                                                           @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return orderService.readByCustomer(userDetails.getUser(), cond, pageable);
    }

    /*
      본인 가게 주문 조회
      - 사장 권한의 유저가 본인 가게의 전체 주문 내역을 조회할 수 있음.
    */
    @Secured("ROLE_OWNER")
    @PostMapping("/owner/orders/search")
    public ResponseEntity<ResDto<ResOrderGetByOwnerDTO>> readByOwner(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestBody ReqOrderCondByOwnerDTO cond,
                                                                     @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return orderService.readByOwner(userDetails.getUser(), cond, pageable);
    }

    /*
        매니저 주문 조회
         - 매니저 권한의 유저가 모든 가게 및 고객에 대한 주문 조회를 할 수 있음
         - 조회 간 가게 이름별 / 고객 이름별 조회 가능
    */
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @PostMapping("/manager/orders/search")
    public ResponseEntity<ResDto<ResOrderGetByManagerDTO>> readByManager(@RequestBody ReqOrderCondByManagerDTO cond,
                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return orderService.readByManager(cond, pageable);
    }

    /*
        주문 단일 및 상세 조회
    */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ResDto<ResOrderGetByIdDTO>> readById(@PathVariable("orderId") UUID orderId) {
        return orderService.readById(orderId);
    }

    /*
       주문 취소
       - 고객과 매니저만이 주문을 취소할 수 있다.
   */
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER"})
    @PatchMapping("/orders/{orderId}/cancel")
    public ResponseEntity<ApiResDto> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("orderId") UUID orderId) {

        orderService.cancelOrder(userDetails.getUser(), orderId);

        return ResponseEntity.ok().body(new ApiResDto("주문 취소 완료", HttpStatus.OK.value()));
    }

    /*
       주문 거절
       - 사장과 매니저가 주문을 취소할 수 있다.
   */
    @Secured({"ROLE_OWNER", "ROLE_MANAGER"})
    @PatchMapping("/orders/{orderId}/reject")
    public ResponseEntity<ApiResDto> rejectOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("orderId") UUID orderId) {

        orderService.rejectOrder(userDetails.getUser(), orderId);

        return ResponseEntity.ok().body(new ApiResDto("주문 거절 완료", HttpStatus.OK.value()));
    }

    /*
        주문 삭제
        - 어드민은 주문을 삭제할 수 있다.
    */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResDto> deleteOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("orderId") UUID orderId) {

        orderService.deleteOrder(userDetails.getUser(), orderId);

        return ResponseEntity.ok().body(new ApiResDto("주문 삭제 완료", HttpStatus.OK.value()));
    }
}
