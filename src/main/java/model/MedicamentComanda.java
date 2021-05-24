package model;


import java.util.Objects;

public class MedicamentComanda extends Entity<Tuple<Integer,Integer>> {

    private Integer cantitate;
    private Medicament medicament;

    public MedicamentComanda(Integer cantitate) {
        this.cantitate = cantitate;
    }


    public Integer getIdComanda() {
        return super.getId().getLeft();
    }

    public void setIdComanda(Integer idComanda) {
        super.getId().setLeft(idComanda);
    }

    public Integer getIdMedicament() {
        return super.getId().getRight();
    }

    public void setIdMedicament(Integer idMedicament) {
        super.getId().setRight(idMedicament);
    }

    public Integer getCantitate() {
        return cantitate;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public String getNume() {
        return medicament.getNume();
    }

    public String getProducator() {
        return medicament.getProducator();
    }

    public TipMedicament getTip() {
        return medicament.getTip();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicamentComanda tag = (MedicamentComanda) o;
        return Objects.equals(getId().getLeft(), tag.getId().getLeft())
                && Objects.equals(getId().getRight(), tag.getId().getRight());
    }
}
