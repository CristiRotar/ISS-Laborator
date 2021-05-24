package repository;

import model.Medicament;
import model.TipMedicament;

import java.util.List;

public interface MedicamentRepository extends Repository<Integer, Medicament> {
    void update(Integer id, Integer cantitate);
    void update(Integer id, String nume, String producator, TipMedicament tip, Integer cantitate);
    void delete(Medicament medicament);
    List<Medicament> findByName(String name);
    Medicament findByNumeAndProducator(String nume,String producator, TipMedicament tip);
}
