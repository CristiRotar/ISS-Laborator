package model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "Medicamente")
public class Medicament extends Entity<Integer> {
    private String nume;
    private String producator;
    private TipMedicament tip;
    private Integer cantitateTotala;

    public Medicament() {
    }

    public Medicament(String nume, String producator, TipMedicament tip, Integer cantitateTotala) {
        this.nume = nume;
        this.producator = producator;
        this.tip = tip;
        this.cantitateTotala = cantitateTotala;
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

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getProducator() {
        return producator;
    }

    public void setProducator(String producator) {
        this.producator = producator;
    }

    public TipMedicament getTip() {
        return tip;
    }

    public void setTip(TipMedicament tip) {
        this.tip = tip;
    }

    public Integer getCantitateTotala() {
        return cantitateTotala;
    }

    public void setCantitateTotala(Integer cantitateTotala) {
        this.cantitateTotala = cantitateTotala;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "nume='" + nume + '\'' +
                ", producator='" + producator + '\'' +
                ", tip=" + tip +
                ", cantitateTotala=" + cantitateTotala +
                '}';
    }
}
