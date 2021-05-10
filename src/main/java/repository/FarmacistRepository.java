package repository;

import model.Farmacist;

public interface FarmacistRepository extends Repository<Integer, Farmacist> {
    Farmacist findByUserAndPassword(String username, String password);
}
