package ro.unitbv.webservicesecurity.jwtauth;

public class JwtAuthFailedException extends RuntimeException {
    private final String causeType;

    public String getCauseType() {
        return causeType;
    }

    public JwtAuthFailedException(String causeType, Throwable cause) {
        super(cause);
        this.causeType = causeType;
    }
}
