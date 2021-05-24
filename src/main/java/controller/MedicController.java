package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.*;
import service.IService;
import service.Service;
import service.ServicesException;
import utils.events.Event;
import utils.observer.Observer;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MedicController implements RolController, Observer {

    public MedicController() {
    }

    @FXML
    private TextField nameTextField;
    @FXML
    private TableView<Comanda> tableComenzi;
    @FXML
    private TableColumn<Comanda, Integer> columnId;
    @FXML
    private TableColumn<Comanda, LocalDate> columnData;
    @FXML
    private TableColumn<Comanda, TipStatus> columnStatus;


    ObservableList<Comanda> comenziModel = FXCollections.observableArrayList();

    private Service service;
    private Medic medic;

    @Override
    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
    }

    @Override
    public void setUser(User user) {
        this.medic = new Medic(user);
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
            List<Comanda> comenzi = StreamSupport.stream(service.findMyOrders(medic).spliterator(), false).collect(Collectors.toList());
            comenziModel.setAll(comenzi);
        } catch (ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }


    public void initializeComenziTable() {
        columnId.setCellValueFactory(new PropertyValueFactory<Comanda, Integer>("Id"));
        columnData.setCellValueFactory(new PropertyValueFactory<Comanda, LocalDate>("Data"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<Comanda, TipStatus>("Status"));
        tableComenzi.setItems(comenziModel);
    }


    @FXML
    public void handleAnuleaza() {
        Comanda comanda = tableComenzi.getSelectionModel().getSelectedItem();
        if(comanda == null)
            MessageAlert.showErrorMessage(null, "Trebuie sa selectati o comanda!");
        else{
            try {

                service.anuleazaComanda(comanda);
            } catch(ServicesException ex) {
                MessageAlert.showErrorMessage(null, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleInregistreaza() {
        try {
            loadComenziStage();
        } catch (IOException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    private void loadComenziStage() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/inregistreazaComanda.fxml"));
        AnchorPane rootLayout = loader.load();
        stage.setScene(new Scene(rootLayout));
        ComandaController controller = (ComandaController)loader.getController();
        controller.setService(service);
        controller.setMedic(medic);
        stage.show();
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

    @Override
    public void update(Event e) {
        initComenziModel();
    }
}
