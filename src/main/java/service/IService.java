package service;

import model.*;
import org.hibernate.service.spi.ServiceException;

import java.time.LocalDate;
import java.util.List;

public interface IService {
    String login(User user) throws ServicesException;
    Iterable<Comanda> findAllOrders() throws ServicesException;
    Iterable<Comanda> findMyOrders(Medic medic) throws ServicesException;
    Iterable<Medicament> findAllMedicamente() throws ServicesException;
    Medicament saveMedicament(Medicament medicament) throws ServicesException;
    public void addComanda(LocalDate data, TipStatus status, String medicUsername, List<MedicamentComanda> medicamenteComanda) throws ServicesException;
    void onoreazaComanda(Comanda comanda) throws ServicesException;
    void refuzaComanda(Comanda comanda) throws ServiceException;
    void updateMedicament(Medicament medicament, Integer canitate) throws ServiceException;
    void deleteMedicament(Medicament medicament) throws ServiceException;
}
