package service;

import model.*;
import model.validators.MedicamentValidator;
import model.validators.ValidationException;
import repository.*;

public class Service  implements IService{

    private FarmacistRepository farmacistRepository;
    private MedicRepository medicRepository;
    private ComandaRepository comandaRepository;
    private MedicamentRepository medicamentRepository;
    private MedicamentValidator medicamentValidator;

    public Service(FarmacistRepository farmacistRepository, MedicRepository medicRepository, ComandaRepository comandaRepository, MedicamentRepository medicamentRepository, MedicamentValidator medicamentValidator) {
        this.farmacistRepository = farmacistRepository;
        this.medicRepository = medicRepository;
        this.comandaRepository = comandaRepository;
        this.medicamentRepository = medicamentRepository;
        this.medicamentValidator = medicamentValidator;
    }

    @Override
    public String login(User agent) throws ServicesException {
        User  user = farmacistRepository.findByUserAndPassword(agent.getUsername(), agent.getPassword());
        String rol = "farmacist";
        System.out.println(user);
        if (user == null){
            user = medicRepository.findByUserAndPassword(agent.getUsername(), agent.getPassword());
            rol = "medic";
        }
        if (user == null) {
            throw new ServicesException("Authentification failed!");
        }
        return rol;
    }

    @Override
    public Iterable<Comanda> findAllOrders() throws ServicesException{
        return comandaRepository.findAll();
    }

    @Override
    public Iterable<Comanda> findMyOrders(Medic medic) throws ServicesException {
        return comandaRepository.findMyOrders(medic.getUsername());
    }

    @Override
    public Iterable<Medicament> findAllMedicamente() throws ServicesException {
        return medicamentRepository.findAll();
    }

    @Override
    public Medicament saveMedicament(Medicament medicament) throws ServicesException {
        try{
            medicamentValidator.validate(medicament);
            medicamentRepository.save(medicament);
        }
        catch (ValidationException ex){
            throw new ServicesException("Wrong data");
        }
        return medicament;
    }


}
