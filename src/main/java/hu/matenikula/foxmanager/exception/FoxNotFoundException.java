package hu.matenikula.foxmanager.exception;

import javax.ejb.ApplicationException;

@ApplicationException()
public class FoxNotFoundException extends RuntimeException {

    public FoxNotFoundException(Long id) {
        super("Fox not found with id: " + id);
    }
}
