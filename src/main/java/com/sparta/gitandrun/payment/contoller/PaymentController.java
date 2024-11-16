package com.sparta.gitandrun.payment.contoller;

import com.sparta.gitandrun.payment.dto.req.ReqPaymentPostDTO;
import com.sparta.gitandrun.payment.service.PaymentService;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
@Slf4j(topic = "Payment-Controller")
public class PaymentController {

    private final PaymentService paymentService;

    @Secured("ROLE_CUSTOMER")
    @PostMapping
    public void createPayment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody ReqPaymentPostDTO dto) {

        paymentService.createPayment(userDetails.getUser(), dto);
    }
}
