package repository;

import model.Comanda;

import java.util.List;

public interface ComandaRepository extends Repository<Integer, Comanda>{
    List<Comanda> findMyOrders(String username);
}
