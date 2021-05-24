package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;

@javax.persistence.Entity
@Table(name = "Comenzi")
public class Comanda extends Entity<Integer> {
    private String medicUsername;
    private LocalDate data;
    private TipStatus status;
    private List<MedicamentComanda> medicamente;

    public Comanda(String medicUsername, LocalDate data, TipStatus status, List<MedicamentComanda> medicamente) {
        this.medicUsername = medicUsername;
        this.data = data;
        this.status = status;
        this.medicamente = medicamente;
    }

    public Comanda() {
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy="increment")
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer integer) {
        super.setId(integer);
    }

    public String getMedicUsername() {
        return medicUsername;
    }

    public void setMedicUsername(String medicUsername) {
        this.medicUsername = medicUsername;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public TipStatus getStatus() {
        return status;
    }

    public void setStatus(TipStatus status) {
        this.status = status;
    }

    @Transient
    public List<MedicamentComanda> getMedicamente() {
        return medicamente;
    }

    public void setMedicamente(List<MedicamentComanda> medicamente) {
        this.medicamente = medicamente;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "medicUsername='" + medicUsername + '\'' +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", medicamente=" + medicamente +
                '}';
    }
}
