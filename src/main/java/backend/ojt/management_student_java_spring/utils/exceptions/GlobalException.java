package backend.ojt.management_student_java_spring.utils.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import backend.ojt.management_student_java_spring.domain.dto.res.RestResponse;

@RestControllerAdvice
public class GlobalException {
    /*
     * Handle exception for resource not found
     */
    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NotFoundException ne) {
        var statusCode = HttpStatus.NOT_FOUND.value();
        var res = new RestResponse<>();
        res.setStatusCode(statusCode);
        res.setError("Not found!");
        res.setMessage(ne.getMessage());
        return ResponseEntity.status(statusCode).body(res);
    }

    /*
     * Handle exception for server confict
     */
    @ExceptionHandler(value = { ConflictException.class })
    public ResponseEntity<RestResponse<Object>> handleConflictException(ConflictException ne) {
        var statusCode = HttpStatus.CONFLICT.value();
        var res = new RestResponse<>();
        res.setStatusCode(statusCode);
        res.setError("Conflict!");
        res.setMessage(ne.getMessage());
        return ResponseEntity.status(statusCode).body(res);
    }

    @ExceptionHandler(value = { BadDataException.class })
    public ResponseEntity<RestResponse<Object>> handleBadDateException(BadDataException ne) {
        var statusCode = HttpStatus.BAD_REQUEST.value();
        var res = new RestResponse<>();
        res.setStatusCode(statusCode);
        res.setError("Bad request!");
        res.setMessage(ne.getMessage());
        return ResponseEntity.status(statusCode).body(res);
    }

    /*
     * Handle exception for valid request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        var res = new RestResponse<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    errors.put(
                            error.getField(),
                            error.getDefaultMessage());
                });
        res.setError("Bad request data!");
        res.setMessage(errors);
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = { DenyException.class })
    public ResponseEntity<RestResponse<Object>> handleDenyException(DenyException ne) {
        var statusCode = HttpStatus.UNAUTHORIZED.value();
        var res = new RestResponse<>();
        res.setStatusCode(statusCode);
        res.setError("Not logged in!");
        res.setMessage(ne.getMessage());
        return ResponseEntity.status(statusCode).body(res);
    }

}
