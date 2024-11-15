package com.sparta.gitandrun.order.repository;

import com.sparta.gitandrun.order.entity.OrderMenu;

import java.util.List;
import java.util.UUID;

public interface OrderMenuCustomRepository {

    List<OrderMenu> findOrderMenusByStoreId(UUID storeId, List<UUID> storeIds);
}
