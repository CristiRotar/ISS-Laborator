package model.validators;

import model.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        StringBuilder err = new StringBuilder();
        if(entity.getId() < 0)
            err.append("Id invalid!\n");
        if(entity.getUsername().length() == 0)
            err.append("Username invalid!\n");
        if(entity.getPassword().length() == 0)
            err.append("Parola invalida!\n");
        if(entity.getName().length() == 0)
            err.append("Nume invalid");

        if(err.length() > 0)
            throw new ValidationException(err.toString());
    }
}
