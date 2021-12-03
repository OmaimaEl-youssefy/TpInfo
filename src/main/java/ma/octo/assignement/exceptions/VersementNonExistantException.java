package ma.octo.assignement.exceptions;

public class VersementNonExistantException extends Exception {

    private static final long serialVersionUID = 1L;

    public VersementNonExistantException() {
        super();
    }

    public VersementNonExistantException(String message) {
        super(message);
    }

}
