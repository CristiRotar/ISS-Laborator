package repository;

import model.Comanda;
import model.TipStatus;

import java.util.List;

public interface ComandaRepository extends Repository<Integer, Comanda>{
    List<Comanda> findMyOrders(String username);
    void update (Integer id, TipStatus status);
    Integer getMaxId();
}
