package com.alperendev.MrBeastECommerce.specification;

import com.alperendev.MrBeastECommerce.entity.OrderItem;
import com.alperendev.MrBeastECommerce.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderItemSpecification {

    /** Speicification to filter orderItems by status */
    public static Specification<OrderItem> hasStatus(OrderStatus status) {
        return ((root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"), status)
        : null);
    }

    /** Specifiacation to filter orderItems by date range*/
    public static Specification<OrderItem> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ((root, query, criteriaBuilder) -> {
          if(startDate != null && endDate != null) {
              return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
          }else if(startDate != null) {
              return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
          }else if(endDate != null) {
              return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
          }else{
              return null;
          }
        });
    }

    /** Generate specification to filter orderItems by itemId*/
    public static Specification<OrderItem> hasItemId(Long itemId){
        return ((root, query, criteriaBuilder) ->
                itemId != null ? criteriaBuilder.equal(root.get("id"), itemId) : null);
    }


}
