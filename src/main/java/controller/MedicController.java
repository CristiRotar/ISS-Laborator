package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Comanda;
import model.Medic;
import model.User;
import service.IService;
import service.ServicesException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MedicController {

    @FXML
    private TableView<Comanda> tableComenzi;
    @FXML
    private TableColumn<Comanda, Integer> columnId;
    @FXML
    private TableColumn<Comanda, String> columnStatus;
    @FXML
    private TableColumn<Comanda, String> columnData;

    ObservableList<Comanda> comenziModel = FXCollections.observableArrayList();

    private IService service;
    private Medic medic;

    public void setService(IService service){
        this.service = service;
    }

    public void setUser(Medic crtAgent) {
        this.medic = crtAgent;
    }

    public void handleAnuleaza(ActionEvent actionEvent) {
    }

    public void handleInregistreaza(ActionEvent actionEvent) {
    }

    @FXML
    public void initialize(){
    }

    public void initComenziModel(){
        try{
            comenziModel.clear();
            List<Comanda> excursii =
                    StreamSupport.stream(service.findMyOrders(medic).spliterator(), false)
                            .collect(Collectors.toList());
            comenziModel.setAll(excursii);
        }catch(ServicesException ex){
            showNotification("Date invalide!", Alert.AlertType.ERROR);
        }

    }

    public void initializeComenziTable(){
        columnId.setCellValueFactory(new PropertyValueFactory<Comanda, Integer>("Id"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<Comanda, String>("Status"));
        columnData.setCellValueFactory(new PropertyValueFactory<Comanda, String>("Data"));
        tableComenzi.setItems(comenziModel);

    }

    private void showNotification(String message, Alert.AlertType type){
        Alert alert=new Alert(type);
        alert.setTitle("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
