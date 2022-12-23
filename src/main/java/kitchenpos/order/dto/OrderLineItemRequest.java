package kitchenpos.order.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemRequest> list(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getMenuId(),
                orderLineItem.getQuantity()))
            .collect(toList());
    }

    public OrderLineItem toOrderLineItem(Order order, List<Menu> menus) {
        Menu firstMenu = menus.stream().filter(menu -> menu.getId().equals(menuId)).findFirst().get();
        return new OrderLineItem(order, firstMenu.getId(), quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
