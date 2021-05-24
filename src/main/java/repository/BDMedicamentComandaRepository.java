package repository;

import model.MedicamentComanda;
import model.Tuple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BDMedicamentComandaRepository  implements MedicamentComandaRepository{

    private JdbcUtils dbUtils;
    private MedicamentRepository medicamentRepository;

    public BDMedicamentComandaRepository(Properties props, MedicamentRepository medicamentRepository) {
        dbUtils = new JdbcUtils(props);
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public List<MedicamentComanda> getMedicamentComandaByIdComanda(Integer idComanda) {
        Connection con = dbUtils.getConnection();
        List<MedicamentComanda> medicamenteComanda = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from MedicamentComanda where idComanda = ?")) {
            preStmt.setInt(1, idComanda);
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer idMedicament = result.getInt("idMedicament");
                    Integer cantitate =  result.getInt("cantitate");

                    MedicamentComanda medicamentComanda = new MedicamentComanda(cantitate);
                    Tuple<Integer,Integer> id = new Tuple<>(idComanda, idMedicament);
                    medicamentComanda.setId(id);
                    medicamentComanda.setMedicament(medicamentRepository.findOne(idMedicament));
                    medicamenteComanda.add(medicamentComanda);
                }
            }
        } catch (SQLException e) {

            System.err.println("Error DB " + e);
        }

        return medicamenteComanda;
    }

    @Override
    public MedicamentComanda findOne(Tuple<Integer, Integer> integerIntegerTuple) {
        return null;
    }

    @Override
    public Iterable<MedicamentComanda> findAll() {
        return null;
    }

    @Override
    public void save(MedicamentComanda entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("insert into MedicamentComanda (idComanda, idMedicament, cantitate) values (?,?,?)")){
            preStmt.setInt(1, entity.getId().getLeft());
            preStmt.setInt(2, entity.getId().getRight());
            preStmt.setInt(3, entity.getCantitate());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error DB " + ex);
        }
    }
}
