package com.sparta.gitandrun.order.repository.custom;

import com.sparta.gitandrun.order.dto.req.ReqOrderCondByCustomerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByManagerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByOwnerDTO;
import com.sparta.gitandrun.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

    Page<Order> findMyOrderListWithConditions(Long userId, ReqOrderCondByCustomerDTO cond, Pageable pageable);

    Page<Order> findOwnerOrderListWithConditions(Long userId, ReqOrderCondByOwnerDTO cond, Pageable pageable);

    Page<Order> findAllOrderListWithConditions(ReqOrderCondByManagerDTO cond, Pageable pageable);
}
