package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Comanda;
import model.User;
import service.IService;
import service.ServicesException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FarmacistController {
    @FXML
    private TableView<Comanda> tableComenzi;
    @FXML
    private TableColumn<Comanda, String> columnFromComanda;
    @FXML
    private TableColumn<Comanda, String> columnStatusComanda;
    @FXML
    private TableColumn<Comanda, String> columnData;

    ObservableList<Comanda> comenziModel = FXCollections.observableArrayList();

    private IService service;

    public void setService(IService service){
        this.service = service;
    }

    public void setUser(User crtAgent) {
    }

    public void initComenziModel(){
        try{
            comenziModel.clear();
            List<Comanda> excursii =
                    StreamSupport.stream(service.findAllOrders().spliterator(), false)
                            .collect(Collectors.toList());
            comenziModel.setAll(excursii);
        }catch(ServicesException ex){
            showNotification("Date invalide!", Alert.AlertType.ERROR);
        }

    }

    public void initializeComenziTable(){
        columnFromComanda.setCellValueFactory(new PropertyValueFactory<Comanda, String>("MedicUsername"));
        columnStatusComanda.setCellValueFactory(new PropertyValueFactory<Comanda, String>("Status"));
        columnData.setCellValueFactory(new PropertyValueFactory<Comanda, String>("Data"));
        tableComenzi.setItems(comenziModel);

    }

    public void handleOnoreaza(ActionEvent actionEvent) {
    }

    public void handleRefuza(ActionEvent actionEvent) {
    }

    public void handleGestioneaza(ActionEvent actionEvent) {
        try{
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/gestioneazaMedicamente.fxml"));
            AnchorPane rootLayout = loader.load();
            newStage.setScene(new Scene(rootLayout));
            GestionareController controller = loader.getController();
            controller.setService(service);
            controller.initMedicamenteModel();
            controller.initializeMedicamenteTable();
            controller.initComboList();
            newStage.show();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Can't open window");
            alert.showAndWait();
        }

    }

    private void showNotification(String message, Alert.AlertType type){
        Alert alert=new Alert(type);
        alert.setTitle("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
