package com.sparta.gitandrun.order.service;


import com.sparta.gitandrun.menu.entity.Menu;
import com.sparta.gitandrun.menu.repository.MenuRepository;
import com.sparta.gitandrun.order.dto.req.CreateOrderReqDto;
import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderMenu;
import com.sparta.gitandrun.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    // 주문 생성
    @Transactional
    public void createOrder(CreateOrderReqDto dto) {

        /*
            유저 조회
            User findUser = userRepository.findUser(user.getId);
        */

        /*
            메뉴 조회 및 중복된 메뉴 개수 계산
        */
        List<Menu> findMenus = getMenus(dto);

        Map<Menu, Long> menuCountMap = getMenuCountMap(dto, findMenus);

        /*
            주문 상품 생성
        */
        List<OrderMenu> orderMenus = OrderMenu.createOrderMenus(findMenus, menuCountMap);

        /*
            주문 생성
        */
        Order order = Order.createOrder(dto.isType(), orderMenus);

        /*
            주문 저장
        */
        orderRepository.save(order);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        /*
            본인 검증 메서드 추후 구현 예정
        */
//        private void checkOrderAccessPermission (User user, Order findOrder){
//            if (!user.getId().equals(findOrder.getUser().getId())) {
//                Objects.equals();
//                throw new IllegalArgumentException("해당 주문에 대한 접근 권한이 없습니다.");
//            }
//        }
//
        Order findOrder = getOrder(orderId);

        findOrder.cancelOrder();
    }

    // 주문 거절
    @Transactional
    public void rejectOrder(Long orderId) {

        Order findOrder = getOrder(orderId);

        /*
            본인 가게 주문 검증 메서드 구현 필요
        */

        findOrder.rejectOrder();
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 항목입니다."));
    }

    private List<Menu> getMenus(CreateOrderReqDto dto) {
        List<Menu> findMenus = menuRepository.findByIdsAndIsDeletedFalse(dto.getMenuIds());

        if (findMenus.isEmpty()) {
            throw new NullPointerException("메뉴 목록이 비어있습니다.");
        }
        return findMenus;
    }

    private static Map<Menu, Long> getMenuCountMap(CreateOrderReqDto dto, List<Menu> findMenus) {
        /*
            1. 클라이언트로부터 전달 받은 menuId 의 리스트를 { 메뉴 Id : 개수 } 형태로 구성된 Map 으로 생성한다.
            2. 이때 Collectors.counting() 키 값의 개수를 세는 역할을 한다.
            3. 이후 { 메뉴 : 메뉴의 개수 } 형태로 구성된 Map 을 생성하여 반환한다.
        */
        Map<Long, Long> idsCountingMap = dto.getMenuIds().stream()
                .collect(Collectors.groupingBy(menuId -> menuId, Collectors.counting()));

        return findMenus.stream()
                .collect(Collectors.toMap(
                        menu -> menu,
                        menu -> idsCountingMap.get(menu.getMenuId())
                ));
    }

}
