package service;

import model.Comanda;
import model.Medic;
import model.Medicament;
import model.User;

public interface IService {
    String login(User user) throws ServicesException;
    Iterable<Comanda> findAllOrders() throws ServicesException;
    Iterable<Comanda> findMyOrders(Medic medic) throws ServicesException;
    Iterable<Medicament> findAllMedicamente() throws ServicesException;
    Medicament saveMedicament(Medicament medicament) throws ServicesException;
}
