package pokemon_battle_api.exception.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pokemon_battle_api.dto.ExceptionResponseDto;
import pokemon_battle_api.exception.ApplicationException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<ExceptionResponseDto> applicationException(ApplicationException ex, WebRequest request) {
        log.error("handling ApplicationException", ex);
        return ResponseEntity.status(ex.getErrCode())
                .body(ExceptionResponseDto.builder()
                        .statusErrorCode(ex.getErrCode().value())
                        .statusErrorMessage(ex.getErrCode().getReasonPhrase())
                        .errorMessage(ex.getErrMsg())
                        .build()
                );
    }
}
