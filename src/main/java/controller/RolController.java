package controller;

import javafx.fxml.FXML;
import model.User;
import service.Service;

import java.awt.event.ActionEvent;

public interface RolController {
    void setService(Service service);
    void setUser(User user);

}
