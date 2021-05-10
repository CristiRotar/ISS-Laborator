package repository;

import model.Medic;

public interface MedicRepository extends Repository<Integer, Medic>{
    Medic findByUserAndPassword(String username, String password);
}
