package kitchenpos.table.application;


import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderDao orderDao, OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.toList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(request.isEmpty());

        OrderTable changedOrderTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(changedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        OrderTable changedOrderTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(changedOrderTable);
    }
}
