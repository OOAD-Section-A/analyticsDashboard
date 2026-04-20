package service;

import exception.AnalyticsExceptionSource;
import model.OrderData;
import repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private static final int INVALID_DATA_ID = 2003;

    private final OrderRepository repository;
    private final AnalyticsExceptionSource exceptionSource;

    public OrderService(OrderRepository repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<OrderData> getCleanedData() {
        List<OrderData> cleaned = new ArrayList<>();

        for (OrderData orderData : repository.fetchAll()) {
            if (orderData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "OrderData", "null", "Record is null");
                continue;
            }
            if (isBlank(orderData.getOrderId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "orderId", null, "must not be blank");
                continue;
            }
            if (isBlank(orderData.getCustomerId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "customerId", orderData.getOrderId(), "must not be blank");
                continue;
            }
            if (isBlank(orderData.getStatus())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "status", orderData.getOrderId(), "must not be blank");
                continue;
            }
            if (orderData.getOrderDate() == null) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "orderDate", orderData.getOrderId(), "must not be null");
                continue;
            }
            if (orderData.getTotalAmount() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "totalAmount", String.valueOf(orderData.getTotalAmount()), "must be non-negative");
                continue;
            }

            cleaned.add(orderData);
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
