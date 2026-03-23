package exceptions;

public class MatriculeFiscaleAlreadyInUseException extends RuntimeException {
    public MatriculeFiscaleAlreadyInUseException() {
        super("Matricule Fiscale already in use");
    }
}

