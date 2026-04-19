package repository.interfaces;

import model.SalesData;
import java.util.List;

public interface SalesRepositoryInterface {
    List<SalesData> fetchAll();
}