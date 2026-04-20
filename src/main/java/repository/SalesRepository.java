package repository;

import model.SalesData;

import java.util.List;

public interface SalesRepository {
    List<SalesData> fetchAll();
}
