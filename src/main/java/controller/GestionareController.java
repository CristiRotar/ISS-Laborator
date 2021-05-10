package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Comanda;
import model.Medicament;
import model.TipMedicament;
import model.validators.ValidationException;
import service.IService;
import service.ServicesException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GestionareController {

    @FXML
    private TableView<Medicament> tableMedicamente;
    @FXML
    private TableColumn<Medicament, String> columnNume;
    @FXML
    private TableColumn<Medicament, String> columnProducator;
    @FXML
    private TableColumn<Medicament, String> columnTip;
    @FXML
    private TableColumn<Medicament, Integer> columnCantitate;

    @FXML
            private TextField textFieldNume;
    @FXML
            private TextField textFieldProducator;
    @FXML
            private ComboBox<String> comboBoxTip;
    @FXML
            private Spinner<Integer> spinnerCantitate;

    ObservableList<Medicament> medicamenteModel = FXCollections.observableArrayList();
    ObservableList<String> comboModel = FXCollections.observableArrayList();

    private IService service;

    public void setService(IService service){
        this.service = service;
        initCantitateSpinner();
    }

    public void initMedicamenteModel(){
        try{
            medicamenteModel.clear();
            List<Medicament> medicamente =
                    StreamSupport.stream(service.findAllMedicamente().spliterator(), false)
                            .collect(Collectors.toList());
            medicamenteModel.setAll(medicamente);
        }catch(ServicesException ex){
            showNotification("Date invalide!", Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void initialize(){
        comboBoxTip.setItems(comboModel);
    }

    public void initComboList(){
        List<String> comboList = new ArrayList<String>();
        comboList.add("comprimat");
        comboList.add("injectabil");
        comboList.add("unguent");
        comboList.add("sirop");
        comboModel.setAll(comboList);
    }

    public void initializeMedicamenteTable(){
        columnNume.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Nume"));
        columnProducator.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Producator"));
        columnTip.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Tip"));
        columnCantitate.setCellValueFactory(new PropertyValueFactory<Medicament, Integer>("CantitateTotala"));
        tableMedicamente.setItems(medicamenteModel);
    }


    public void handleAdauga(ActionEvent actionEvent) {
        try{
            String nume = textFieldNume.getText();
            String producator = textFieldProducator.getText();
            TipMedicament tip;
            if(comboBoxTip.getValue()!= null){
                 tip = TipMedicament.valueOf(comboBoxTip.getValue());
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Add failure");
                alert.setContentText("Wrong data");
                alert.showAndWait();
                tip = null;
            }
            Integer cantitate = spinnerCantitate.getValue();
            Medicament medicament = new Medicament(nume, producator, tip, cantitate);
            service.saveMedicament(medicament);
            initMedicamenteModel();
            initializeMedicamenteTable();
        }
        catch(ServicesException e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Add failure");
            alert.setContentText("Wrong data");
            alert.showAndWait();
        }

    }

    public void handleSterge(ActionEvent actionEvent) {
    }

    public void handleActualizeaza(ActionEvent actionEvent) {
    }

    private void showNotification(String message, Alert.AlertType type){
        Alert alert=new Alert(type);
        alert.setTitle("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void fill(){
        if(spinnerCantitate.getValue() != null){
            spinnerCantitate.decrement(spinnerCantitate.getValue());
        }

        Medicament medicament = tableMedicamente.getSelectionModel().getSelectedItem();
        textFieldNume.setText(medicament.getNume());
        textFieldProducator.setText(medicament.getProducator());
        comboBoxTip.setPromptText(medicament.getTip().toString());
        spinnerCantitate.increment(0);
        spinnerCantitate.increment(medicament.getCantitateTotala());

    }

    private void initCantitateSpinner() {
        SpinnerValueFactory<Integer> value = new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(0);
                else {
                    Integer value = getValue();
                    if(value!=0)
                        setValue(value-steps);
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(0);
                else {
                    Integer value = getValue();
                    setValue(value+steps);
                }
            }
        };
        spinnerCantitate.setValueFactory(value);
    }
}
