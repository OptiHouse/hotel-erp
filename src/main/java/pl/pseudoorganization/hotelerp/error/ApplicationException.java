package pl.pseudoorganization.hotelerp.error;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception {

    private final ErrorCode errorCode;
    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.message);
        this.errorCode = errorCode;
    }
}
