package com.sparta.gitandrun.payment.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderMenu;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.entity.enums.PaymentStatus;
import com.sparta.gitandrun.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPaymentGetByIdDTO {

    private PaymentDTO paymentDTO;

    public static ResPaymentGetByIdDTO of(Payment payment, Order order, List<OrderMenu> orderMenus) {
        return ResPaymentGetByIdDTO.builder()
                .paymentDTO(PaymentDTO.from(payment, order, orderMenus))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class PaymentDTO {

        private UUID id;
        private int paymentPrice;
        private String paymentStatus;
        private LocalDateTime createdAt;
        private LocalDateTime canceledAt;
        private OrderDTO orderDTO;

        private static PaymentDTO from(Payment payment, Order order, List<OrderMenu> orderMenus) {
            return PaymentDTO.builder()
                    .id(payment.getId())
                    .paymentPrice(payment.getPaymentPrice())
                    .paymentStatus(payment.getPaymentStatus().status)
                    .createdAt(payment.getCreatedAt())
                    .canceledAt(
                            payment.getPaymentStatus() == PaymentStatus.CANCEL
                                    ? payment.getUpdatedAt() : null
                    )
                    .orderDTO(OrderDTO.from(order, orderMenus))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class OrderDTO {

            private UUID id;
            private String status;
            private String type;
            private LocalDateTime createdAt;
            private List<OrderMenuDTO> orderMenuDTOS;
            private StoreDTO storeDTO;

            private static OrderDTO from(Order order, List<OrderMenu> orderMenus) {

                return OrderDTO.builder()
                        .id(order.getId())
                        .status(order.getOrderStatus().status)
                        .type(order.getOrderType().getType())
                        .createdAt(order.getCreatedAt())
                        .orderMenuDTOS(OrderMenuDTO.from(orderMenus).get(order.getId()))
                        .storeDTO(StoreDTO.from(order.getStore()))
                        .build();
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            private static class OrderMenuDTO {

                private UUID id;
                private UUID menuId;
                private String menuName;
                private int menuPrice;
                private int count;

                private static Map<UUID, List<OrderMenuDTO>> from(List<OrderMenu> orderMenus) {
                    return orderMenus.stream()
                            .collect(Collectors.groupingBy(
                                    orderMenu -> orderMenu.getOrder().getId(),
                                    Collectors.mapping(
                                            OrderMenuDTO::from,
                                            Collectors.toList(
                                            )
                                    )));
                }

                private static OrderMenuDTO from(OrderMenu orderMenu) {
                    return OrderMenuDTO.builder()
                            .id(orderMenu.getId())
                            .menuId(orderMenu.getMenu().getMenuId())
                            .menuName(orderMenu.getMenu().getMenuName())
                            .menuPrice(orderMenu.getOrderPrice())
                            .count(orderMenu.getOrderCount())
                            .build();
                }
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            private static class StoreDTO {
                private UUID id;
                private String name;
                private String zipcode;
                private String address;
                private String addressDetail;

                private static StoreDTO from(Store store) {
                    return StoreDTO.builder()
                            .id(store.getStoreId())
                            .name(store.getStoreName())
                            .zipcode(store.getAddress().getZipCode())
                            .address(store.getAddress().getAddress())
                            .addressDetail(store.getAddress().getAddressDetail())
                            .build();
                }
            }
        }
    }
}
