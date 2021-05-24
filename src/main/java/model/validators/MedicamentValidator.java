package model.validators;

import model.Medicament;

public class MedicamentValidator implements Validator<Medicament> {
    @Override
    public void validate(Medicament entity) throws ValidationException {
        StringBuilder err = new StringBuilder();
        if(entity.getNume().length() == 0)
            err.append("Nume invalid!\n");
        if(entity.getProducator().length() == 0)
            err.append("Producator invalid!\n");
        if(entity.getCantitateTotala()== null || entity.getCantitateTotala() < 0)
            err.append("Cantitate invalida");

        if(err.length() > 0)
            throw new ValidationException(err.toString());
    }
}
