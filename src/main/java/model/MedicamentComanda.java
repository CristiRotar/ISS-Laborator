package model;



public class MedicamentComanda extends Entity<Tuple<Integer,Integer>> {

    private Integer cantitate;

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


}
