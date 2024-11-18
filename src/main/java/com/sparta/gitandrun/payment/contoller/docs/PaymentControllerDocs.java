package com.sparta.gitandrun.payment.contoller.docs;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.order.dto.res.ResDto;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByCustomerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentPostDTO;
import com.sparta.gitandrun.payment.dto.res.ResPaymentGetByUserIdDTO;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Payment", description = "결제 생성, 결제 내역 조회, 수정 등의 사용자 API")
public interface PaymentControllerDocs {

    @Operation(summary = "결제 생성", description = "사용자의 ID 를 통해 주문을 생성하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "결제 생성 성공", content = @Content(schema = @Schema(implementation = ApiResDto.class))),
            @ApiResponse(responseCode = "400", description = "총 금액이 맞지않습니다.", content = @Content(schema = @Schema(implementation = ApiResDto.class)))
    })

    @PostMapping("/pay")
    ResponseEntity<ApiResDto> createPayment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ReqPaymentPostDTO dto);


    @Operation(summary = "결제 조회", description = "고객의 본인 결제 내역을 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "결제 생성 성공", content = @Content(schema = @Schema(implementation = ApiResDto.class))),
            @ApiResponse(responseCode = "400", description = "총 금액이 맞지않습니다.", content = @Content(schema = @Schema(implementation = ApiResDto.class)))
    })

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/pay")
    ResponseEntity<ResDto<ResPaymentGetByUserIdDTO>> readPayment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestBody ReqPaymentCondByCustomerDTO cond,
                                                                 @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);


}
