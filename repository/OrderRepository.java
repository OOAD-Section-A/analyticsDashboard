package repository;

import com.jackfruit.scm.database.adapter.OrderAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.reporting.model.OrderData;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository {

    private final OrderAdapter orderAdapter;

    public OrderRepository(SupplyChainDatabaseFacade facade) {
        this.orderAdapter = new OrderAdapter(facade);
    }

    public List<OrderData> fetchAll() {
        return orderAdapter.listOrders().stream()
                .map(o -> new OrderData(
                        o.orderId(),
                        o.customerId(),
                        o.status(),
                        o.totalAmount(),
                        o.orderDate()
                ))
                .collect(Collectors.toList());
    }
}