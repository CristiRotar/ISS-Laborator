package repository;

import model.Comanda;
import model.MedicamentComanda;
import model.TipStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BDComandaRepositoryClasic implements ComandaRepository{
    private JdbcUtils dbUtils;
    private MedicamentComandaRepository medicamentComandaRepository;

    public BDComandaRepositoryClasic(Properties props, MedicamentComandaRepository medicamentComandaRepository) {
        dbUtils = new JdbcUtils(props);
        this.medicamentComandaRepository = medicamentComandaRepository;
    }

    @Override
    public List<Comanda> findMyOrders(String username) {
        Connection con = dbUtils.getConnection();
        List<Comanda> excursii = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Comenzi where medicUsername = ?")) {
            preStmt.setString(1, username);
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    LocalDate data = LocalDate.parse(result.getString("data"));
                    TipStatus status = TipStatus.valueOf(result.getString("status"));
                    List<MedicamentComanda> medicamenteComanda = medicamentComandaRepository.getMedicamentComandaByIdComanda(id);
                    Comanda comanda = new Comanda(username, data, status, medicamenteComanda);
                    comanda.setId(id);
                    excursii.add(comanda);
                }
            }
        } catch (SQLException e) {

            System.err.println("Error DB " + e);
        }

        return excursii;
    }

    @Override
    public void update(Integer id, TipStatus status) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("update Comenzi set status = ? where id = ?")){
            preStmt.setString(1, status.toString());
            preStmt.setInt(2, id);

            int result = preStmt.executeUpdate();

        } catch (SQLException ex) {

            System.err.println("Error DB " + ex);
        }

    }

    @Override
    public Integer getMaxId() {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT MAX(id) from Comenzi")){
            try(ResultSet result = preStmt.executeQuery()){
                Integer id = result.getInt("max(id)");
                return id;
            }

        } catch (SQLException ex) {

            System.err.println("Error DB " + ex);
            return null;
        }
    }

    @Override
    public Comanda findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Comanda> findAll() {
        Connection con = dbUtils.getConnection();
        List<Comanda> excursii = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Comenzi ")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    LocalDate data = LocalDate.parse(result.getString("data"));
                    TipStatus status = TipStatus.valueOf(result.getString("status"));
                    String medicUsername = result.getString("medicUSername");
                    List<MedicamentComanda> medicamenteComanda = medicamentComandaRepository.getMedicamentComandaByIdComanda(id);
                    Comanda comanda = new Comanda(medicUsername, data, status, medicamenteComanda);
                    comanda.setId(id);
                    excursii.add(comanda);
                }
            }
        } catch (SQLException e) {

            System.err.println("Error DB " + e);
        }

        return excursii;
    }

    @Override
    public void save(Comanda entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("insert into Comenzi (data, status, medicUsername) values (?,?,?)")){
            preStmt.setString(1, entity.getData().toString());
            preStmt.setString(2, entity.getStatus().toString());
            preStmt.setString(3, entity.getMedicUsername());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error DB " + ex);
        }
    }
}
