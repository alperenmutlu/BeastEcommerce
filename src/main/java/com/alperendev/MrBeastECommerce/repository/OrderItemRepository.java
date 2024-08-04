package com.alperendev.MrBeastECommerce.repository;

import com.alperendev.MrBeastECommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
}
