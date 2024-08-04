package com.alperendev.MrBeastECommerce.service.Impl;

import com.alperendev.MrBeastECommerce.dto.OrderItemDto;
import com.alperendev.MrBeastECommerce.dto.OrderRequest;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.entity.Order;
import com.alperendev.MrBeastECommerce.entity.OrderItem;
import com.alperendev.MrBeastECommerce.entity.Product;
import com.alperendev.MrBeastECommerce.entity.User;
import com.alperendev.MrBeastECommerce.enums.OrderStatus;
import com.alperendev.MrBeastECommerce.exception.NotFoundException;
import com.alperendev.MrBeastECommerce.mapper.EntityDtoMapper;
import com.alperendev.MrBeastECommerce.repository.OrderItemRepository;
import com.alperendev.MrBeastECommerce.repository.OrderRepository;
import com.alperendev.MrBeastECommerce.repository.ProductRepository;
import com.alperendev.MrBeastECommerce.service.interfaces.OrderItemService;
import com.alperendev.MrBeastECommerce.service.interfaces.UserService;
import com.alperendev.MrBeastECommerce.specification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {
        User user = userService.getLoginUser();
        //map orderRequest items to ORDER ENTITIES

        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(orderItemRequest -> {
                    Product product = productRepository.findById(orderItemRequest.getProductId()).orElseThrow(
                            () -> new NotFoundException("Product not found")
                    );

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemRequest.getQuantity());
                    orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))); //set price according tp the quantity
                    orderItem.setStatus(OrderStatus.PENDING);
                    orderItem.setUser(user);
                    return orderItem;
                }).collect(Collectors.toList());

        //calculate the total price
        BigDecimal totalPrice = orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0
                ? orderRequest.getTotalPrice()
                : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //create order entity
        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);

        //set the order reference in each orderItem
        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        orderRepository.save(order);

        return Response.builder()
                .status(200)
                .message("Order is successfully placed")
                .build();
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order item not found"));
        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepository.save(orderItem);
        return Response.builder()
                .status(200)
                .message("Order status updated successfully!")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> specification = Specification.where(OrderItemSpecification
                .hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepository.findAll(specification, pageable);

        if(orderItemPage.isEmpty()){
            throw new NotFoundException("No order found!");
        }
        List<OrderItemDto> orderItemDtos = orderItemPage.getContent()
                .stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElements(orderItemPage.getTotalElements())
                .build();
    }
}
