package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        validation(order, menu, quantity);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validation(Order order, Menu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException("주문 내역이 없습니다.");
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("주문 메뉴가 없습니다.");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("주문 수량이 없습니다.");
        }
    }

    void updateOrder(Order order) {
        if (this.order != order) {
            this.order = order;
            order.addOrderLineItem(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }
}
