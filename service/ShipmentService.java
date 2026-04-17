package service;

import exception.AnalyticsExceptionSource;
import model.ShipmentData;
import repository.ShipmentRepository;

import java.util.ArrayList;
import java.util.List;

public class ShipmentService {

    private static final int INVALID_DATA_ID = 2005;

    private final ShipmentRepository repository;
    private final AnalyticsExceptionSource exceptionSource;

    public ShipmentService(ShipmentRepository repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<ShipmentData> getCleanedData() {
        List<ShipmentData> cleaned = new ArrayList<>();

        for (ShipmentData shipmentData : repository.fetchAll()) {
            if (shipmentData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "ShipmentData", "null", "Record is null");
                continue;
            }
            if (isBlank(shipmentData.getShipmentId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "shipmentId", null, "must not be blank");
                continue;
            }
            if (isBlank(shipmentData.getOrderId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "orderId", shipmentData.getShipmentId(), "must not be blank");
                continue;
            }
            if (isBlank(shipmentData.getStatus())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "status", shipmentData.getShipmentId(), "must not be blank");
                continue;
            }
            if (shipmentData.getDispatchDate() == null) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "dispatchDate", shipmentData.getShipmentId(), "must not be null");
                continue;
            }
            if (shipmentData.getDeliveryDate() != null
                    && shipmentData.getDispatchDate() != null
                    && shipmentData.getDeliveryDate().isBefore(shipmentData.getDispatchDate())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "deliveryDate", shipmentData.getShipmentId(), "must not be before dispatchDate");
                continue;
            }

            cleaned.add(shipmentData);
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
