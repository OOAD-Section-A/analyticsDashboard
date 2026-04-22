package dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TableViewDTO {
    private final String dataset;
    private final List<String> columns;
    private final List<Map<String, Object>> rows;
    private final LocalDateTime generatedAt;

    public TableViewDTO(String dataset, List<String> columns, List<Map<String, Object>> rows, LocalDateTime generatedAt) {
        this.dataset = dataset;
        this.columns = columns;
        this.rows = rows;
        this.generatedAt = generatedAt;
    }

    public String getDataset() {
        return dataset;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}
