package project.baptisteq.projectlillenopendata.exception;

/**
 * Created by Baptiste on 27/05/18.
 */

/**
 * Exception levée quand l'appel à l'API Open Data est indisponible
 */
public class APIFailureException extends Exception {

    public APIFailureException() {
    }

    public APIFailureException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
