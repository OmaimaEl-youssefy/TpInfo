package ma.octo.assignement.web.common;

import ma.octo.assignement.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandelingController {

    @ExceptionHandler(SoldeDisponibleInsuffisantException.class)
    public ResponseEntity<String> handleSoldeDisponibleInsuffisantException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    @ExceptionHandler(CompteNonExistantException.class)
    public ResponseEntity<String> handleCompteNonExistantException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VersementNonExistantException.class)
    public ResponseEntity<String> VersementNonExistantException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VirementNonExistantException.class)
    public ResponseEntity<String> VirementNonExistantException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> TransactionException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

}
