package com.sparta.gitandrun.order.service;

import com.sparta.gitandrun.menu.entity.Menu;
import com.sparta.gitandrun.menu.repository.MenuRepository;
import com.sparta.gitandrun.order.dto.req.CreateOrderReqDto;
import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderMenu;
import com.sparta.gitandrun.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void createOrder(CreateOrderReqDto dto) {

        /*
        유저 조회
        User findUser = userRepository.findUser(user.getId);
        */

        /*
        메뉴 조회
        */
        Menu findMenu = menuRepository.findByIdAndIsDeletedFalse(dto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 항목입니다."));

        /*
         주문 상품 생성
        */
        OrderMenu orderMenu = OrderMenu.createOrderMenu(findMenu, findMenu.getPrice(), dto.getCount());

        /*
        주문 생성
        */
        Order order = Order.createOrder(dto.isType(), orderMenu);

        /*
        주문 저장
        */
        orderRepository.save(order);
    }

}
