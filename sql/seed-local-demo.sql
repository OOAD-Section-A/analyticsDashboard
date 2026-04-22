-- Local demo seed for the Analytics Dashboard
-- Run this after the OOAD schema is created.
-- Example:
--   mysql -u root -p OOAD < analyticsDashboard/sql/seed-local-demo.sql

USE OOAD;
START TRANSACTION;

INSERT INTO proc_suppliers (supplier_id, name, avg_lead_time, reliability_score, status)
VALUES
    ('SUP-001', 'Green Valley Farms', 3, 96.50, 'ACTIVE'),
    ('SUP-002', 'Sunrise Produce Co.', 5, 91.20, 'ACTIVE'),
    ('SUP-003', 'FreshLine Wholesale', 4, 93.75, 'ACTIVE')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    avg_lead_time = VALUES(avg_lead_time),
    reliability_score = VALUES(reliability_score),
    status = VALUES(status);

INSERT INTO warehouses (warehouse_id, warehouse_name)
VALUES
    ('WH-001', 'Central Distribution Center'),
    ('WH-002', 'South City Hub')
ON DUPLICATE KEY UPDATE
    warehouse_name = VALUES(warehouse_name);

INSERT INTO warehouse_zones (zone_id, warehouse_id, zone_type, temperature_class)
VALUES
    ('ZONE-001', 'WH-001', 'STORAGE', 'AMBIENT'),
    ('ZONE-002', 'WH-001', 'PICKING', 'AMBIENT'),
    ('ZONE-003', 'WH-002', 'STORAGE', 'COLD')
ON DUPLICATE KEY UPDATE
    warehouse_id = VALUES(warehouse_id),
    zone_type = VALUES(zone_type),
    temperature_class = VALUES(temperature_class);

INSERT INTO bins (bin_id, zone_id, bin_capacity, bin_status, max_weight_kg, barcode)
VALUES
    ('BIN-A1', 'ZONE-001', 500, 'OCCUPIED', 1200.00, 'BC-BIN-A1'),
    ('BIN-A2', 'ZONE-001', 400, 'AVAILABLE', 900.00, 'BC-BIN-A2'),
    ('BIN-B1', 'ZONE-003', 350, 'OCCUPIED', 800.00, 'BC-BIN-B1')
ON DUPLICATE KEY UPDATE
    zone_id = VALUES(zone_id),
    bin_capacity = VALUES(bin_capacity),
    bin_status = VALUES(bin_status),
    max_weight_kg = VALUES(max_weight_kg),
    barcode = VALUES(barcode);

INSERT INTO products (
    product_id, product_name, sku, category, sub_category, supplier_id,
    unit_of_measure, zone, reorder_threshold, product_image_reference,
    storage_conditions, shelf_life_days
)
VALUES
    (
        'PROD-APL-001', 'Fresh Apples', 'SKU-APL-001', 'Fruits', 'Fresh Produce', 'SUP-001',
        'KG', 'COLD STORAGE', 80, NULL, 'Keep refrigerated between 2-4 C', 21
    ),
    (
        'PROD-BAN-001', 'Bananas', 'SKU-BAN-001', 'Fruits', 'Fresh Produce', 'SUP-002',
        'KG', 'AMBIENT', 100, NULL, 'Store in cool dry area', 10
    ),
    (
        'PROD-MNG-001', 'Mangoes', 'SKU-MNG-001', 'Fruits', 'Fresh Produce', 'SUP-003',
        'KG', 'AMBIENT', 60, NULL, 'Avoid direct sunlight', 14
    )
ON DUPLICATE KEY UPDATE
    product_name = VALUES(product_name),
    sku = VALUES(sku),
    category = VALUES(category),
    sub_category = VALUES(sub_category),
    supplier_id = VALUES(supplier_id),
    unit_of_measure = VALUES(unit_of_measure),
    zone = VALUES(zone),
    reorder_threshold = VALUES(reorder_threshold),
    storage_conditions = VALUES(storage_conditions),
    shelf_life_days = VALUES(shelf_life_days);

INSERT INTO stock_levels (
    stock_level_id, product_id, current_stock_qty, reserved_stock_qty,
    available_stock_qty, reorder_threshold, reorder_quantity,
    safety_stock_level, zone_assignment, stock_health_status, snapshot_timestamp
)
VALUES
    (
        'SL-001', 'PROD-APL-001', 260, 20, 240, 80, 120, 40,
        'ZONE-001', 'HEALTHY', '2026-04-05 09:00:00'
    ),
    (
        'SL-002', 'PROD-BAN-001', 140, 30, 110, 100, 140, 50,
        'ZONE-001', 'LOW', '2026-04-05 09:00:00'
    ),
    (
        'SL-003', 'PROD-MNG-001', 90, 10, 80, 60, 90, 30,
        'ZONE-003', 'HEALTHY', '2026-04-05 09:00:00'
    )
ON DUPLICATE KEY UPDATE
    current_stock_qty = VALUES(current_stock_qty),
    reserved_stock_qty = VALUES(reserved_stock_qty),
    available_stock_qty = VALUES(available_stock_qty),
    reorder_threshold = VALUES(reorder_threshold),
    reorder_quantity = VALUES(reorder_quantity),
    safety_stock_level = VALUES(safety_stock_level),
    zone_assignment = VALUES(zone_assignment),
    stock_health_status = VALUES(stock_health_status),
    snapshot_timestamp = VALUES(snapshot_timestamp);

INSERT INTO stock_records (stock_id, product_id, bin_id, quantity, batch_id, lpn_id, last_updated)
VALUES
    ('SR-001', 'PROD-APL-001', 'BIN-A1', 260, 'BATCH-APL-001', NULL, '2026-04-05 09:00:00'),
    ('SR-002', 'PROD-BAN-001', 'BIN-A2', 140, 'BATCH-BAN-001', NULL, '2026-04-05 09:00:00'),
    ('SR-003', 'PROD-MNG-001', 'BIN-B1', 90, 'BATCH-MNG-001', NULL, '2026-04-05 09:00:00')
ON DUPLICATE KEY UPDATE
    product_id = VALUES(product_id),
    bin_id = VALUES(bin_id),
    quantity = VALUES(quantity),
    batch_id = VALUES(batch_id),
    lpn_id = VALUES(lpn_id),
    last_updated = VALUES(last_updated);

INSERT INTO price_list (
    price_id, sku_id, region_code, channel, price_type, base_price, price_floor,
    currency_code, effective_from, effective_to, status
)
VALUES
    (
        'PRICE-APL-001', 'SKU-APL-001', 'SOUTH', 'RETAIL', 'RETAIL', 120.00, 100.00,
        'INR', '2026-04-01 00:00:00', '2026-05-01 00:00:00', 'ACTIVE'
    ),
    (
        'PRICE-BAN-001', 'SKU-BAN-001', 'SOUTH', 'RETAIL', 'RETAIL', 80.00, 70.00,
        'INR', '2026-04-01 00:00:00', '2026-05-01 00:00:00', 'ACTIVE'
    ),
    (
        'PRICE-MNG-001', 'SKU-MNG-001', 'SOUTH', 'RETAIL', 'RETAIL', 150.00, 130.00,
        'INR', '2026-04-01 00:00:00', '2026-05-01 00:00:00', 'ACTIVE'
    )
ON DUPLICATE KEY UPDATE
    base_price = VALUES(base_price),
    price_floor = VALUES(price_floor),
    currency_code = VALUES(currency_code),
    effective_from = VALUES(effective_from),
    effective_to = VALUES(effective_to),
    status = VALUES(status);

INSERT INTO orders (
    order_id, customer_id, order_status, order_date, total_amount, payment_status, sales_channel
)
VALUES
    ('ORD-1001', 'CUST-101', 'CONFIRMED', '2026-04-05 10:15:00', 420.00, 'PAID', 'ONLINE'),
    ('ORD-1002', 'CUST-102', 'FULFILLED', '2026-04-05 11:05:00', 240.00, 'PAID', 'POS'),
    ('ORD-1003', 'CUST-103', 'CONFIRMED', '2026-04-05 12:30:00', 450.00, 'PAID', 'ONLINE')
ON DUPLICATE KEY UPDATE
    customer_id = VALUES(customer_id),
    order_status = VALUES(order_status),
    order_date = VALUES(order_date),
    total_amount = VALUES(total_amount),
    payment_status = VALUES(payment_status),
    sales_channel = VALUES(sales_channel);

INSERT INTO order_items (
    order_item_id, order_id, product_id, ordered_quantity, unit_price, line_total
)
VALUES
    ('ITEM-1001', 'ORD-1001', 'PROD-APL-001', 3, 120.00, 360.00),
    ('ITEM-1002', 'ORD-1002', 'PROD-BAN-001', 2, 80.00, 160.00),
    ('ITEM-1003', 'ORD-1003', 'PROD-MNG-001', 3, 150.00, 450.00)
ON DUPLICATE KEY UPDATE
    order_id = VALUES(order_id),
    product_id = VALUES(product_id),
    ordered_quantity = VALUES(ordered_quantity),
    unit_price = VALUES(unit_price),
    line_total = VALUES(line_total);

INSERT INTO delivery_orders (
    delivery_id, order_id, customer_id, delivery_address, delivery_status, delivery_date,
    delivery_type, delivery_cost, assigned_agent, warehouse_id, created_at, updated_at
)
VALUES
    (
        'SHIP-1001', 'ORD-1001', 'CUST-101', 'Bengaluru, Karnataka', 'IN_TRANSIT', NULL,
        'STANDARD', 60.00, 'AGENT-11', 'WH-001', '2026-04-05 10:30:00', '2026-04-05 10:30:00'
    ),
    (
        'SHIP-1002', 'ORD-1002', 'CUST-102', 'Mysuru, Karnataka', 'DELIVERED', '2026-04-06 16:20:00',
        'EXPRESS', 40.00, 'AGENT-12', 'WH-001', '2026-04-05 11:20:00', '2026-04-06 16:20:00'
    ),
    (
        'SHIP-1003', 'ORD-1003', 'CUST-103', 'Chennai, Tamil Nadu', 'DELAYED', NULL,
        'STANDARD', 75.00, 'AGENT-13', 'WH-002', '2026-04-05 12:45:00', '2026-04-07 09:15:00'
    )
ON DUPLICATE KEY UPDATE
    order_id = VALUES(order_id),
    customer_id = VALUES(customer_id),
    delivery_address = VALUES(delivery_address),
    delivery_status = VALUES(delivery_status),
    delivery_date = VALUES(delivery_date),
    delivery_type = VALUES(delivery_type),
    delivery_cost = VALUES(delivery_cost),
    assigned_agent = VALUES(assigned_agent),
    warehouse_id = VALUES(warehouse_id),
    created_at = VALUES(created_at),
    updated_at = VALUES(updated_at);

INSERT INTO sales_records (
    sale_id, product_id, store_id, sale_date, quantity_sold, unit_price, revenue, region
)
VALUES
    ('SALE-1001', 'PROD-APL-001', 'STORE-01', '2026-04-05', 3, 120.00, 360.00, 'SOUTH'),
    ('SALE-1002', 'PROD-BAN-001', 'STORE-01', '2026-04-05', 2, 80.00, 160.00, 'SOUTH'),
    ('SALE-1003', 'PROD-MNG-001', 'STORE-02', '2026-04-05', 3, 150.00, 450.00, 'SOUTH')
ON DUPLICATE KEY UPDATE
    product_id = VALUES(product_id),
    store_id = VALUES(store_id),
    sale_date = VALUES(sale_date),
    quantity_sold = VALUES(quantity_sold),
    unit_price = VALUES(unit_price),
    revenue = VALUES(revenue),
    region = VALUES(region);

INSERT INTO approval_requests (
    approval_id, request_type, order_id, requested_discount_amt, status,
    submission_time, escalation_time, approval_timestamp, routed_to_approver_id,
    approving_manager_id, rejection_reason, audit_log_flag
)
VALUES
    (
        'APR-1001', 'MANUAL_DISCOUNT', 'ORD-1001', 125.0000, 'APPROVED',
        '2026-04-05 08:30:00', NULL, '2026-04-05 09:15:00', 'MGR-01',
        'MGR-01', NULL, TRUE
    ),
    (
        'APR-1002', 'POLICY_EXCEPTION', 'ORD-1003', 85.0000, 'REJECTED',
        '2026-04-05 09:40:00', '2026-04-05 10:15:00', '2026-04-05 10:45:00', 'MGR-02',
        'MGR-02', 'Discount exceeds policy cap', TRUE
    )
ON DUPLICATE KEY UPDATE
    request_type = VALUES(request_type),
    order_id = VALUES(order_id),
    requested_discount_amt = VALUES(requested_discount_amt),
    status = VALUES(status),
    submission_time = VALUES(submission_time),
    escalation_time = VALUES(escalation_time),
    approval_timestamp = VALUES(approval_timestamp),
    routed_to_approver_id = VALUES(routed_to_approver_id),
    approving_manager_id = VALUES(approving_manager_id),
    rejection_reason = VALUES(rejection_reason),
    audit_log_flag = VALUES(audit_log_flag);

INSERT INTO profitability_analytics (
    approval_id, request_type, discount_amount, final_status, recorded_at
)
VALUES
    ('APR-1001', 'MANUAL_DISCOUNT', 125.0000, 'APPROVED', '2026-04-05 09:15:00'),
    ('APR-1002', 'POLICY_EXCEPTION', 85.0000, 'REJECTED', '2026-04-05 10:45:00')
ON DUPLICATE KEY UPDATE
    request_type = VALUES(request_type),
    discount_amount = VALUES(discount_amount),
    final_status = VALUES(final_status),
    recorded_at = VALUES(recorded_at);

INSERT INTO demand_forecasts (
    forecast_id, product_id, forecast_period, forecast_date, predicted_demand,
    confidence_score, reorder_signal, suggested_order_qty, lifecycle_stage,
    algorithm_used, generated_at, source_event_reference
)
VALUES
    (
        'DF-1001', 'PROD-APL-001', 'WEEKLY', '2026-04-06', 320, 92.50, TRUE, 100,
        'GROWTH', 'ARIMA', '2026-04-05 09:30:00', 'SR-001'
    ),
    (
        'DF-1002', 'PROD-BAN-001', 'WEEKLY', '2026-04-06', 210, 88.40, TRUE, 140,
        'MATURE', 'LSTM', '2026-04-05 09:30:00', 'SR-002'
    ),
    (
        'DF-1003', 'PROD-MNG-001', 'WEEKLY', '2026-04-06', 180, 90.10, FALSE, 90,
        'GROWTH', 'ARIMA', '2026-04-05 09:30:00', 'SR-003'
    )
ON DUPLICATE KEY UPDATE
    product_id = VALUES(product_id),
    forecast_period = VALUES(forecast_period),
    forecast_date = VALUES(forecast_date),
    predicted_demand = VALUES(predicted_demand),
    confidence_score = VALUES(confidence_score),
    reorder_signal = VALUES(reorder_signal),
    suggested_order_qty = VALUES(suggested_order_qty),
    lifecycle_stage = VALUES(lifecycle_stage),
    algorithm_used = VALUES(algorithm_used),
    generated_at = VALUES(generated_at),
    source_event_reference = VALUES(source_event_reference);

INSERT INTO commission_sales (sale_id, agent_id, sale_amount, sale_date, status)
VALUES
    ('ORD-1001', 'AGENT-11', 360.00, '2026-04-05 10:30:00', 'APPROVED'),
    ('ORD-1002', 'AGENT-12', 160.00, '2026-04-05 11:20:00', 'APPROVED'),
    ('ORD-1003', 'AGENT-13', 450.00, '2026-04-05 12:45:00', 'APPROVED')
ON DUPLICATE KEY UPDATE
    agent_id = VALUES(agent_id),
    sale_amount = VALUES(sale_amount),
    sale_date = VALUES(sale_date),
    status = VALUES(status);

DELETE FROM SCM_EXCEPTION_LOG
WHERE subsystem = 'REPORTING'
  AND exception_id IN (1001, 1002);

INSERT INTO SCM_EXCEPTION_LOG (exception_id, exception_name, severity, subsystem, error_message, logged_at)
VALUES
    (1001, 'LowStockWarning', 'MEDIUM', 'REPORTING', 'Bananas have dropped below reorder threshold', '2026-04-05 13:00:00.123'),
    (1002, 'ShipmentDelay', 'HIGH', 'REPORTING', 'Shipment SHIP-1003 delayed due to route congestion', '2026-04-05 14:10:00.456')
ON DUPLICATE KEY UPDATE
    exception_name = VALUES(exception_name),
    severity = VALUES(severity),
    subsystem = VALUES(subsystem),
    error_message = VALUES(error_message),
    logged_at = VALUES(logged_at);

COMMIT;
