package pl.pseudoorganization.hotelerp.error;

public enum ErrorCode {
    USER_ALREADY_EXISTS("Użytkownik już istnieje"),
    NOT_FOUND("Nie znaleziono"),
    OTHER_ERROR("Inny błąd");

    final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
