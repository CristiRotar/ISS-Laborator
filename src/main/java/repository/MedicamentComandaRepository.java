package repository;

import model.MedicamentComanda;
import model.Tuple;

import java.util.List;

public interface MedicamentComandaRepository extends Repository<Tuple<Integer, Integer>,MedicamentComanda> {

    List<MedicamentComanda> getMedicamentComandaByIdComanda(Integer idComanda);


}
