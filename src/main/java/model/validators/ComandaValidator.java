package model.validators;

import model.Comanda;

public class ComandaValidator implements Validator<Comanda>{
    @Override
    public void validate(Comanda entity) throws ValidationException {
        StringBuilder err = new StringBuilder();
        if(entity.getMedicUsername().length() == 0)
            err.append("Username invalid!\n");
        if(entity.getMedicamente().size() == 0)
            err.append("Trebuie sa selectati minim un medicament!");

        if(err.length() > 0)
            throw new ValidationException(err.toString());
    }
}
