package com.alperendev.MrBeastECommerce.service.interfaces;

import com.alperendev.MrBeastECommerce.dto.OrderRequest;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.enums.OrderStatus;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;

public interface OrderItemService {
    Response placeOrder(OrderRequest orderRequest);
    Response updateOrderItemStatus(Long orderItemId, String status);
    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);
}
