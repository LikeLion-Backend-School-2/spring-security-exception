package springboot.springsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalReviewException extends RuntimeException {
    private ErrorCode errorcode;
    private String message;

    @Override
    public String toString() {
        if(message == null) return errorcode.getMessage();
        return String.format("%s. %s", errorcode.getMessage(), message);
    }
}
