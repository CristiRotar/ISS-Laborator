package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import service.Service;
import service.ServicesException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ComandaController {

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
    private TableView<MedicamentComanda> tableComanda;
    @FXML
    private TableColumn<MedicamentComanda, String> columnNumeComanda;
    @FXML
    private TableColumn<MedicamentComanda, String> columnProducatorComanda;
    @FXML
    private TableColumn<MedicamentComanda, TipMedicament> columnTipComanda;
    @FXML
    private TableColumn<MedicamentComanda, Integer> columnCantitateComanda;


    @FXML
    private TextField textFieldNume;
    @FXML
    private Spinner<Integer> spinnerCantitate;

    private Service service;
    private Medic medic;
    ObservableList<Medicament> medicamenteDModel = FXCollections.observableArrayList();
    ObservableList<MedicamentComanda> medicamenteAModel = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
    }

    public void setMedic(Medic user) {
        this.medic = user;
        initMedicamenteDTable();
        initCantitateSpinner();
    }

    private void initCantitateSpinner() {
        SpinnerValueFactory<Integer> value = new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(1);
                else {
                    Integer value = getValue();
                    if(value>1)
                        setValue(value-steps);
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(1);
                else {
                    Integer value = getValue();
                    setValue(value+steps);
                }
            }
        };
        spinnerCantitate.setValueFactory(value);
    }

    public void initMedicamenteDTable() {
        initMedicamenteDModel();
        initializeMedicamenteTable();
    }

    public void initMedicamenteDModel() {
        try {
            medicamenteDModel.clear();
            List<Medicament> medicamente = StreamSupport.stream(service.findAllMedicamente().spliterator(), false).collect(Collectors.toList());
            medicamenteDModel.setAll(medicamente);
        } catch (ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }


    public void initializeMedicamenteTable() {
        columnNume.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Nume"));
        columnProducator.setCellValueFactory(new PropertyValueFactory<Medicament, String>("Producator"));
        columnTip.setCellValueFactory(new PropertyValueFactory<Medicament, TipMedicament>("Tip"));
        columnCantitate.setCellValueFactory(new PropertyValueFactory<Medicament, Integer>("CantitateTotala"));
        tableMedicamente.setItems(medicamenteDModel);
    }


    public void initializeMedicamenteATable() {
        columnNumeComanda.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, String>("Nume"));
        columnProducatorComanda.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, String>("Producator"));
        columnTipComanda.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, TipMedicament>("Tip"));
        columnCantitateComanda.setCellValueFactory(new PropertyValueFactory<MedicamentComanda, Integer>("Cantitate"));
        tableComanda.setItems(medicamenteAModel);
    }

    @FXML
    public void handleCauta() {
        String nume = textFieldNume.getText();
        try {
            List<Medicament> medicamente = nume.equals("") ? StreamSupport.stream(service.findAllMedicamente().spliterator(), false).collect(Collectors.toList())
                    : service.findMedicamenteByNume(nume);
            medicamenteDModel.clear();
            medicamenteDModel.setAll(medicamente);
        } catch(ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    public void handleAdauga() {
        Medicament medicament = tableMedicamente.getSelectionModel().getSelectedItem();
        if(medicament.getCantitateTotala() < spinnerCantitate.getValue()){
            MessageAlert.showErrorMessage(null, "Cantitatea aleasa pentru medicamentul " + medicament.getNume() + " este prea mare!");
        }
        else{
            if (spinnerCantitate.getValue() != null && medicament != null) {
                MedicamentComanda medicamentComanda = new MedicamentComanda(spinnerCantitate.getValue());
                Tuple<Integer, Integer> id = new Tuple<Integer, Integer>(-1, medicament.getId());
                medicamentComanda.setId(id);
                medicamentComanda.setMedicament(medicament);
                if (!medicamenteAModel.contains(medicamentComanda)) {
                    medicamenteAModel.add(medicamentComanda);
                    initializeMedicamenteATable();
                }
                return;
            }
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Warning", "Datele sunt invalide!");
        }
    }

    @FXML
    public void handleActualizeaza() {
        MedicamentComanda medicament = tableComanda.getSelectionModel().getSelectedItem();
        if(medicament == null){
            MessageAlert.showErrorMessage(null, "Trebuie sa selectati un medicament!");
        }
        else{
            medicament.setCantitate(spinnerCantitate.getValue());
            int index = medicamenteAModel.indexOf(medicament);
            if (index >= 0) {
                medicamenteAModel.set(index, medicament);
                tableComanda.setItems(medicamenteAModel);
            }
        }
    }

    @FXML
    public void handleElimina() {
        MedicamentComanda medicament = tableComanda.getSelectionModel().getSelectedItem();
        medicamenteAModel.remove(medicament);
        initializeMedicamenteATable();
    }

    @FXML
    public void handleTrimite() {
        try {
            service.addComanda(LocalDate.now(), TipStatus.pending, medic.getUsername(), medicamenteAModel);
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Success", "Comanda a fost adaugata");
        } catch (ServicesException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }
}
