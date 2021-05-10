package repository;

import model.Medicament;
import model.TipMedicament;

public interface MedicamentRepository extends Repository<Integer, Medicament> {
    void update(Medicament medicament);

    void delete(Medicament medicament);
}
