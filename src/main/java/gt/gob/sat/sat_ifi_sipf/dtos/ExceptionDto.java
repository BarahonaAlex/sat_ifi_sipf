/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.http.HttpStatus;

/**
 *
 * @author crramosl
 */
@ApiModel(value = "Excepcion")
@Getter
public class ExceptionDto {

    private final Integer httpCode;
    private final String httpTitle;
    private final LocalDateTime timestamp;
    private final String path;
    private final String userMessage;
    private final List<ErrorDto> errors;

    @Builder
    private ExceptionDto(HttpStatus httpStatus, String path, String message,
            @Singular("error") List<ErrorDto> errors) {

        this.httpCode = httpStatus.value();
        this.httpTitle = httpStatus.getReasonPhrase();
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.userMessage = message;
        this.errors = errors;
    }
}
