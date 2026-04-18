package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.Order;
import exception.AnalyticsExceptionSource;
import model.OrderData;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository {

    private static final int CONNECTION_FAILURE_ID = 1003;

    private final AnalyticsExceptionSource exceptionSource;

    public OrderRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<OrderData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            return facade.orders().listOrders().stream()
                    .map(this::mapOrder)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            exceptionSource.fireDataSourceUnavailable("OrderRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch order data", ex);
        }
    }

    private OrderData mapOrder(Order order) {
        return new OrderData(
                order.getOrderId(),
                order.getCustomerId(),
                order.getOrderStatus(),
                order.getTotalAmount() == null ? 0.0 : order.getTotalAmount().doubleValue(),
                order.getOrderDate() == null ? null : order.getOrderDate().toLocalDate()
        );
    }
}
