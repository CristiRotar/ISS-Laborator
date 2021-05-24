package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import service.Service;
import service.ServicesException;
import utils.events.Event;
import utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GestioneazaMedicamenteController implements Observer {

    @FXML
    private TableView<Medicament> tableMedicamente;
    @FXML
    private TableColumn<Medicament, String> columnNume;
    @FXML
    private TableColumn<Medicament, String> columnProducator;
    @FXML
    private TableColumn<Medicament, TipMedicament> columnTip;
    @FXML
    private TableColumn<Medicament, Integer> columnCantitate;

    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldProducator;
    @FXML
    private ComboBox<TipMedicament> comboBoxTip;
    @FXML
    private Spinner<Integer> spinnerCantitate;

    ObservableList<Medicament> medicamenteModel = FXCollections.observableArrayList();
    ObservableList<TipMedicament> comboModel = FXCollections.observableArrayList();
    private Service service;
    private Farmacist farmacist;

    public void setService(Service service){
        this.service = service;
        service.addObserver(this);
    }

    public void setFarmacist(Farmacist user){
        this.farmacist = farmacist;
        initMedicamenteTable();
        initCantitateSpinner();
        initComboBox();
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

    private void initComboBox(){
        comboBoxTip.setItems(FXCollections.observableArrayList(TipMedicament.values()));
    }

    public void initMedicamenteTable(){
        initMedicamenteModel();
        initializeMedicamenteTable();
    }

    public void initMedicamenteModel(){
        try{
            medicamenteModel.clear();
            List<Medicament> medicamente = StreamSupport.stream(service.findAllMedicamente().spliterator(), false).collect(Collectors.toList());
            medicamenteModel.setAll(medicamente);
        }
        catch(ServicesException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    public void initializeMedicamenteTable(){
        columnNume.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Nume"));
        columnProducator.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Producator"));
        columnTip.setCellValueFactory(new PropertyValueFactory<Medicament, TipMedicament>("Tip"));
        columnCantitate.setCellValueFactory(new PropertyValueFactory<Medicament, Integer>("CantitateTotala"));
        tableMedicamente.setItems(medicamenteModel);
    }

    @FXML
    public void fill(){

        if(spinnerCantitate.getValue() != null){
            spinnerCantitate.decrement(spinnerCantitate.getValue());
        }

        Medicament medicament = tableMedicamente.getSelectionModel().getSelectedItem();
        textFieldNume.setText(medicament.getNume());
        textFieldProducator.setText(medicament.getProducator());
        comboBoxTip.setValue(medicament.getTip());
        spinnerCantitate.increment(0);
        spinnerCantitate.increment(medicament.getCantitateTotala());
    }

    @FXML
    public void handleAdauga(){
        try{
            String nume = textFieldNume.getText();
            String producator = textFieldProducator.getText();
            TipMedicament tip = comboBoxTip.getValue();
            Integer cantitate = spinnerCantitate.getValue();
            Medicament medicament = new Medicament(nume, producator, tip, cantitate);
            service.saveMedicament(medicament);
        }
        catch(ServicesException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    public void handleActualizeaza(){
        Medicament medicament = tableMedicamente.getSelectionModel().getSelectedItem();
        if(medicament == null)
            MessageAlert.showErrorMessage(null, "Trebuie sa selectati un medicament!");
        else{
            try{
                String nume = textFieldNume.getText();
                String producator = textFieldProducator.getText();
                TipMedicament tip = comboBoxTip.getValue();
                Integer cantitate = spinnerCantitate.getValue();
                service.updateMedicament(medicament.getId(), nume, producator, tip, cantitate);
            }
            catch(ServicesException ex){
                MessageAlert.showErrorMessage(null, ex.getMessage());
            }
        }

    }

    @FXML
    public void handleSterge(){
        Medicament medicament = tableMedicamente.getSelectionModel().getSelectedItem();
        if(medicament == null)
            MessageAlert.showErrorMessage(null, "Trebuie sa selectati un medicament!");
        else{
            try{

                service.deleteMedicament(medicament);
            }
            catch(ServicesException ex){
                MessageAlert.showErrorMessage(null, ex.getMessage());
            }
        }

    }


    @Override
    public void update(Event e) {
        initMedicamenteModel();
    }
}
