package service;


import model.*;
import model.validators.MedicamentValidator;
import model.validators.ValidationException;
import model.validators.Validator;
import repository.*;
import utils.events.ComandaChangeEvent;
import utils.events.Event;
import utils.events.EventType;
import utils.observer.Observable;
import utils.observer.Observer;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Service  implements Observable {

    private FarmacistRepository farmacistRepository;
    private MedicRepository medicRepository;
    private ComandaRepository comandaRepository;
    private MedicamentRepository medicamentRepository;
    private MedicamentComandaRepository medicamentComandaRepository;
    private Validator<Medicament> medicamentValidator;
    private Validator<Comanda> comandaValidator;


    public Service(FarmacistRepository farmacistRepository, MedicRepository medicRepository, ComandaRepository comandaRepository, MedicamentRepository medicamentRepository, MedicamentComandaRepository medicamentComandaRepository, Validator<Medicament> medicamentValidator, Validator<Comanda> comandaValidator) {
        this.farmacistRepository = farmacistRepository;
        this.medicRepository = medicRepository;
        this.comandaRepository = comandaRepository;
        this.medicamentRepository = medicamentRepository;
        this.medicamentComandaRepository = medicamentComandaRepository;
        this.medicamentValidator = medicamentValidator;
        this.comandaValidator = comandaValidator;
    }

    //@Override
    public HashMap<String, Object> findUser(String username, String password) throws ServicesException {
        User user;
        Rol rol;
        try {
            user = farmacistRepository.findByUserAndPassword(username, password);
            rol = Rol.farmacist;
            if (user == null) {
                user = medicRepository.findByUserAndPassword(username, password);
                rol = Rol.medic;
            }
            if (user == null) {
                throw new ServicesException("Invalid username or password!");
            }
        } catch (NoResultException ex) {
            throw new ServicesException("Invalid username or password!");
        }

        User finalUser = user;
        Rol finalRol = rol;
        return new HashMap<String, Object>(){{
            put("user", finalUser);
            put("rol", finalRol);
        }};
    }

    //@Override
    public Iterable<Comanda> findAllOrders() throws ServicesException{
        return comandaRepository.findAll();
    }

    //@Override
    public Iterable<Comanda> findMyOrders(Medic medic) throws ServicesException {
        return comandaRepository.findMyOrders(medic.getUsername());
    }

    //@Override
    public Iterable<Medicament> findAllMedicamente() throws ServicesException {
        return medicamentRepository.findAll();
    }

    //@Override
    public void saveMedicament(Medicament medicament) throws ServicesException {
        if(medicamentRepository.findByNumeAndProducator(medicament.getNume(), medicament.getProducator(), medicament.getTip()) != null)
            throw new ServicesException("Medicamentul " + medicament.getNume() + " al producatorului " + medicament.getProducator() + " exista deja!");
        try{
            medicamentValidator.validate(medicament);
            medicamentRepository.save(medicament);
            notifyObservers(new ComandaChangeEvent(EventType.ADAUGA, null));
        }
        catch (ValidationException ex){
            throw new ServicesException(ex.getMessage());
        }
    }

    public void deleteMedicament(Medicament medicament) throws ServicesException{
        try{
            medicamentRepository.delete(medicament);
            notifyObservers(new ComandaChangeEvent(EventType.STERGE, null));
        }
        catch (ValidationException ex){
            throw new ServicesException(ex.getMessage());
        }
    }

    public void updateMedicament(Integer id, String nume, String producator, TipMedicament tip, Integer cantitate) throws ServicesException{

        try{
            Medicament medicament = new Medicament(nume, producator, tip, cantitate);
            medicamentValidator.validate(medicament);
            medicamentRepository.update(id, nume, producator, tip, cantitate);
            notifyObservers(new ComandaChangeEvent(EventType.ACTUALIZEAZA, null));
        }
        catch(ValidationException ex){
            throw new ServicesException(ex.getMessage());
        }
    }

    //@Override
    public List<MedicamentComanda> findMedicamenteByComanda(Comanda comanda) {
        return medicamentComandaRepository.getMedicamentComandaByIdComanda(comanda.getId());
    }

    //@Override
    public void anuleazaComanda(Comanda comanda) throws ServicesException {
        if(!comanda.getStatus().equals(TipStatus.pending)){
            throw new ServicesException("Nu puteti anula aceasta comanda!");
        }
        comandaRepository.update(comanda.getId(), TipStatus.canceled);
        notifyObservers(new ComandaChangeEvent(EventType.ANULEAZA, null));
    }

    //@Override
    public List<Medicament> findMedicamenteByNume(String nume) {
        return medicamentRepository.findByName(nume);
    }

    //@Override
    public void addComanda(LocalDate data, TipStatus status, String medicUsername, List<MedicamentComanda> medicamenteComanda) throws ServicesException {
        for (MedicamentComanda mc : medicamenteComanda){
            if(mc.getCantitate() > mc.getMedicament().getCantitateTotala()){
                throw new ServicesException("Cantitatea aleasa este prea mare!");
            }
        }
        Comanda comanda = new Comanda(medicUsername, data, status, medicamenteComanda);
        try{
            comandaValidator.validate(comanda);
            comandaRepository.save(comanda);
            Integer idComanda = comandaRepository.getMaxId();

            for (MedicamentComanda mc : medicamenteComanda){
                Tuple<Integer, Integer> id = new Tuple<Integer, Integer>(idComanda, mc.getMedicament().getId());
                mc.setId(id);
                medicamentComandaRepository.save(mc);
            }
            notifyObservers(new ComandaChangeEvent(EventType.EFECTUEAZA, null));
        }
        catch(ValidationException ex){
            throw new ServicesException("Eroare la adaugarea comenzii!");
        }
    }

    public void acceptaComanda(Comanda comanda) throws ServicesException {
        for(MedicamentComanda mc : comanda.getMedicamente()){
            if(mc.getCantitate() > mc.getMedicament().getCantitateTotala()){
                throw new ServicesException("Cantitatea este prea mare!");
            }
        }
        if(!comanda.getStatus().equals(TipStatus.pending)){
            throw new ServicesException("Nu puteti accepta aceasta comanda!");
        }
        try{
            comandaRepository.update(comanda.getId(), TipStatus.approved);
            for(MedicamentComanda mc : comanda.getMedicamente()){
                medicamentRepository.update(mc.getId().getRight(), mc.getMedicament().getCantitateTotala() - mc.getCantitate());
            }
            notifyObservers(new ComandaChangeEvent(EventType.ACCEPTA, null));
        }
        catch(ValidationException ex){
            throw new ServicesException("Eroare la acceptarea comenzii" + ex);
        }
    }

    public void refuzaComanda(Comanda comanda) throws ServicesException {
        if(!comanda.getStatus().equals(TipStatus.pending)){
            throw new ServicesException("Nu puteti refuza aceasta comanda!");
        }
        try{
            comandaRepository.update(comanda.getId(), TipStatus.rejected);
            notifyObservers(new ComandaChangeEvent(EventType.RESPINGE, null));
        }
        catch(ValidationException ex){
            throw new ServicesException("Eroare la refuzarea comenzii" + ex);
        }
    }



    private List<Observer> observers = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observers.stream().forEach(x -> x.update(t));
    }
}
