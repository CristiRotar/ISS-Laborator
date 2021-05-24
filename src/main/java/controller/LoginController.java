package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Rol;
import model.User;
import service.IService;
import service.Service;
import service.ServicesException;
import java.io.IOException;
import java.util.HashMap;

public class LoginController {

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;

    private Service service;

    public void setService(Service service) {
        this.service = service;
    }


    private void loadMainStage(String path, User user) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        AnchorPane rootLayout = loader.load();
        stage.setScene(new Scene(rootLayout));
        RolController controller = loader.getController();
        controller.setService(service);
        controller.setUser(user);
        stage.show();
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        try {
            HashMap<String, Object> data = service.findUser(username, password);
            User user = (User)data.get("user");
            Rol rol = (Rol)data.get("rol");
            String path = "/views/" + rol.toString() + "View.fxml";
            loadMainStage(path, user);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (ServicesException | IOException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }
}
