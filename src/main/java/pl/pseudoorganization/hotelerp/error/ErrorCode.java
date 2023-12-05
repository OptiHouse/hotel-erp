package pl.pseudoorganization.hotelerp.error;

public enum ErrorCode {
    USER_ALREADY_EXISTS("User already exists"),
    NOT_FOUND("Not found"),
    INVALID_STATUS("Action not allowed"),
    OTHER_ERROR("Unknown error");

    final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
