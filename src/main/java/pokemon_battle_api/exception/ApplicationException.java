package pokemon_battle_api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final HttpStatus errCode;
    private final String errMsg;

    public ApplicationException(HttpStatus errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
