package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.*;
import service.Service;
import service.ServicesException;
import utils.events.Event;
import utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FarmacistController implements RolController, Observer {

    @FXML
    private TextField nameTextField;
    @FXML
    private TableView<Comanda> tableComenzi;
    @FXML
    private TableColumn<Comanda, String> columnFromComanda;
    @FXML
    private TableColumn<Comanda, LocalDate> columnData;
    @FXML
    private TableColumn<Comanda, TipStatus> columnStatusComanda;
    @FXML
    private TableView<MedicamentComanda> tableMedicamente;
    @FXML
    private TableColumn<MedicamentComanda, String> columnNumeMedicament;
    @FXML
    private TableColumn<MedicamentComanda, String> columnProducatorMedicament;
    @FXML
    private TableColumn<MedicamentComanda, TipMedicament> columnTipMedicament;
    @FXML
    private TableColumn<MedicamentComanda, Integer> columnCantitateMedicament;

    ObservableList<Comanda> comenziModel = FXCollections.observableArrayList();
    ObservableList<MedicamentComanda> medicamenteModel = FXCollections.observableArrayList();
    private Service service;
    private Farmacist farmacist;

    @Override
    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
    }

    @Override
    public void setUser(User user) {
        this.farmacist = new Farmacist(user);
        nameTextField.setText(user.getName());
        loadComenzi();
    }

    @FXML
    public void handleLogout() throws IOException {
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList());
        open.forEach(Window::hide);
        loadLoginStage();
    }

    public void loadComenzi() {
        initComenziModel();
        initializeComenziTable();
    }

    public void initComenziModel() {
        try {
            comenziModel.clear();
            List<Comanda> comenzi = StreamSupport.stream(service.findAllOrders().spliterator(), false).collect(Collectors.toList());
            comenziModel.setAll(comenzi);
        } catch (ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    public void initMedicamenteModel(Comanda comanda) {
        medicamenteModel.clear();
        List<MedicamentComanda> medicamente = service.findMedicamenteByComanda(comanda);
        medicamenteModel.setAll(medicamente);
    }

    public void initializaMedicamenteTable() {
        columnNumeMedicament.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, String>("Nume"));
        columnProducatorMedicament.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, String>("Producator"));
        columnTipMedicament.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, TipMedicament>("Tip"));
        columnCantitateMedicament.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, Integer>("Cantitate"));
        tableMedicamente.setItems(medicamenteModel);
    }

    public void initializeComenziTable() {
        columnFromComanda.setCellValueFactory(new PropertyValueFactory<Comanda, String>("MedicUsername"));
        columnData.setCellValueFactory(new PropertyValueFactory<Comanda, LocalDate>("Data"));
        columnStatusComanda.setCellValueFactory(new PropertyValueFactory<Comanda, TipStatus>("Status"));
        tableComenzi.setItems(comenziModel);
    }

    @FXML
    public void initMedicamente() {
        Comanda comanda = tableComenzi.getSelectionModel().getSelectedItem();
        initMedicamenteModel(comanda);
        initializaMedicamenteTable();
    }

    @Override
    public void update(Event e) {
        initComenziModel();
    }

    private void loadLoginStage() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/loginView.fxml"));
        AnchorPane rootLayout = loader.load();
        stage.setScene(new Scene(rootLayout));
        LoginController controller = loader.getController();
        controller.setService(service);
        stage.show();
    }

    private void loadGestioneazaStage() throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/gestioneazaMedicamente.fxml"));
        AnchorPane rootLayout = loader.load();
        stage.setScene(new Scene(rootLayout));
        GestioneazaMedicamenteController controller = loader.getController();
        controller.setService(service);
        controller.setFarmacist(farmacist);
        stage.show();
    }

    @FXML
    public void handleOnoreaza() {
        Comanda comanda = tableComenzi.getSelectionModel().getSelectedItem();
        try {
            service.acceptaComanda(comanda);
        } catch (ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    public void handleRefuza() {
        Comanda comanda = tableComenzi.getSelectionModel().getSelectedItem();
        try {
            service.refuzaComanda(comanda);
        } catch (ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }


    @FXML
    public void handleGestioneaza(){
        try{
            loadGestioneazaStage();
        }
        catch(IOException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }
}
