package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Medic;
import model.User;
import service.IService;
import service.ServicesException;

import java.io.IOException;


public class LoginController {

    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    AnchorPane ap;
    Parent mainParent;
    private FarmacistController farmacistCtrl;
    private User crtAgent;
    private IService service;

    public void setService(IService service){
        this.service = service;
    }

    public void setParent(Parent p){ this.mainParent = p;}

    public void setFarmacistController(FarmacistController farmacistController) {
        this.farmacistCtrl = farmacistController;
    }

    @FXML
    public void initialize(){

    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) throws ServicesException {
        String nume = usernameTextField.getText();
        String passwd = passwordTextField.getText();
        crtAgent = new User(nume, passwd);
        try{
            String  rol = service.login(crtAgent);
            if(rol.equals("farmacist")){
                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/farmacistMainView.fxml"));
                AnchorPane rootLayout = loader.load();
                newStage.setScene(new Scene(rootLayout));
                FarmacistController controller = loader.getController();
                controller.setService(service);
                controller.initComenziModel();
                controller.initializeComenziTable();
                newStage.show();
            }
            else{
                Medic crtMedic = new Medic(nume, passwd);
                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/medicMainView.fxml"));
                AnchorPane rootLayout = loader.load();
                newStage.setScene(new Scene(rootLayout));
                MedicController controller = loader.getController();
                controller.setService(service);
                controller.setUser(crtMedic);
                controller.initComenziModel();
                controller.initializeComenziTable();
                newStage.show();

            }

        }   catch (IOException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }
}
