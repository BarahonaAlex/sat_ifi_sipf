/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.dtos.ErrorDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.dtos.ExceptionDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author crramosl
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestException
        extends ResponseEntityExceptionHandler {

    /**
     * Manejador de las excepciones especificas de la aplicacion.
     *
     * @param excepcion Excepcion del tipo {@link ExcepcionNegocio}
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ExceptionDto> negocioExcepcion(BusinessException excepcion,
            WebRequest peticion) {

        ExceptionDto respuesta = ExceptionDto.builder()
                .httpStatus(excepcion.getStatus())
                .path(this.extractPath(peticion))
                .message(excepcion.getReason())
                .errors(excepcion.getErrors())
                .build();
        return ResponseEntity.status(excepcion.getStatus()).body(respuesta);
    }

    /**
     * Manejador para las excepciones de validacion de campos por medio de
     * {@code JSR380}. La excepcion {@link ConstraintViolationException} es
     * lanzada por spring cuando se utiliza la anotacion {@code Validated}.
     *
     * @param exception Excepcion del tipo {@link ConstraintViolationException}
     * @param request Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ExceptionDto> constraintViolationException(
            ConstraintViolationException exception, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<ErrorDto> errors = exception
                .getConstraintViolations()
                .stream()
                .map(objeto
                        -> ErrorDto.builder()
                        .param(objeto.getPropertyPath().toString())
                        .value(objeto.getInvalidValue())
                        .description(objeto.getMessage())
                        .build()
                ).collect(Collectors.toList());
        ExceptionDto respuesta = ExceptionDto.builder()
                .httpStatus(httpStatus)
                .path(this.extractPath(request))
                .message(Message.INVALID_PARAMS.getText())
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus).body(respuesta);
    }

    /**
     * Manejador generico de excepciones, se ejecuta para cualquier excepcion
     * que no este manejada de forma especifica.
     *
     * @param excepcion Excepcion ocurrida
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> exception(Exception excepcion,
            WebRequest peticion) {

        HttpHeaders cabeceras = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return this.handleExceptionInternal(excepcion, null, cabeceras, httpStatus, peticion);
    }

    /**
     * Metodo para controlar las excepciones que son lanzadas por spring cuando
     * hay errors de validacion en los campos de entrada de los servicios REST.
     * Especificamente si se utiliza {@code JSR380} (Bean Validation) con la
     * anoracion {@link javax.validation.Valid}.
     *
     * @param excepcion Excepcion del tipo
     * {@link MethodArgumentNotValidException}
     * @param cabeceras Cabeceras para el {@code Response}
     * @param httpStatus Codigo de estado HTTP para el {@code Response}
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException excepcion, HttpHeaders cabeceras,
            HttpStatus httpStatus, WebRequest peticion) {

        List<ErrorDto> errors = excepcion
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(objeto -> {
                    FieldError field = (FieldError) objeto;
                    return ErrorDto.builder()
                            .param(field.getField())
                            .value(field.getRejectedValue())
                            .description(field.getDefaultMessage())
                            .build();
                }).collect(Collectors.toList());
        ExceptionDto respuesta = ExceptionDto.builder()
                .httpStatus(httpStatus)
                .path(this.extractPath(peticion))
                .message(Message.INVALID_PARAMS.getText())
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus).headers(cabeceras).body(respuesta);
    }

    /**
     * Metodo para controlar las excepciones que son lanzadas por spring cuando
     * hay errors de validacion en los campos de entrada de los servicios REST.
     * Especificamente si se utiliza {@code JSR380} (Bean Validation) con la
     * anoracion {@link javax.validation.Valid}.
     *
     * @param excepcion Excepcion del tipo {@link BindException}
     * @param cabeceras Cabeceras para el {@code Response}
     * @param httpStatus Codigo de estado HTTP para el {@code Response}
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException excepcion, HttpHeaders cabeceras,
            HttpStatus httpStatus, WebRequest peticion) {

        List<ErrorDto> errors = excepcion
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(objeto -> {
                    FieldError field = (FieldError) objeto;
                    return ErrorDto.builder()
                            .param(field.getField())
                            .value(field.getRejectedValue())
                            .description(field.getDefaultMessage())
                            .build();
                }).collect(Collectors.toList());
        ExceptionDto respuesta = ExceptionDto.builder()
                .httpStatus(httpStatus)
                .path(this.extractPath(peticion))
                .message(Message.INVALID_PARAMS.getText())
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus).headers(cabeceras).body(respuesta);
    }

    /**
     * Metodo para controlar las excepciones que son lanzadas por spring cuando
     * no es posible convertir algun param del {@code request body} al tipo de
     * dato en el dto que espera el servicio que fue solicitado.
     *
     * @param excepcion Excepcion del tipo
     * {@link HttpMessageNotReadableException}
     * @param cabeceras Cabeceras para el {@code Response}
     * @param httpStatus Codigo de estado HTTP para el {@code Response}
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException excepcion, HttpHeaders cabeceras,
            HttpStatus httpStatus, WebRequest peticion) {

        String param = null;
        Object value = null;
        Throwable causa = excepcion.getCause();
        if (causa instanceof InvalidFormatException) {
            param = ((InvalidFormatException) causa).getPath().get(0).getFieldName();
            value = ((InvalidFormatException) causa).getValue();

        } else if (causa instanceof JsonMappingException) {
            param = ((JsonMappingException) causa).getPath().get(0).getFieldName();

        }

        if (StringUtils.isNotBlank(param)) {
            ErrorDto error = ErrorDto.builder()
                    .param(param)
                    .value(value)
                    .description("Valor inválido")
                    .build();
            ExceptionDto respuesta = ExceptionDto.builder()
                    .httpStatus(httpStatus)
                    .path(this.extractPath(peticion))
                    .message(Message.INVALID_PARAMS.getText())
                    .error(error)
                    .build();
            return ResponseEntity.status(httpStatus).headers(cabeceras).body(respuesta);
        }

        return handleExceptionInternal(excepcion, null, cabeceras, httpStatus, peticion);
    }

    /**
     * Metodo para controlar las excepciones que son lanzadas por spring cuando
     * no es posible convertir algun param de la peticion no es del tipo de dato
     * esperado.
     *
     * @param excepcion Excepcion del tipo {@link TypeMismatchException}
     * @param cabeceras Cabeceras para el {@code Response}
     * @param httpStatus Codigo de estado HTTP para el {@code Response}
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException excepcion, HttpHeaders cabeceras,
            HttpStatus httpStatus, WebRequest peticion) {

        String param;
        Object value;
        if (excepcion instanceof MethodArgumentTypeMismatchException) {
            param = ((MethodArgumentTypeMismatchException) excepcion).getName();
            value = ((MethodArgumentTypeMismatchException) excepcion).getValue();
        } else {
            param = excepcion.getPropertyName();
            value = excepcion.getValue();
        }
        ErrorDto error = ErrorDto.builder()
                .param(param)
                .value(value)
                .description("Tipo del valor inválido")
                .build();
        ExceptionDto respuesta = ExceptionDto.builder()
                .httpStatus(httpStatus)
                .path(this.extractPath(peticion))
                .message(Message.INVALID_PARAMS.getText())
                .error(error)
                .build();
        return ResponseEntity.status(httpStatus).headers(cabeceras).body(respuesta);
    }

    /**
     * Metodo central para crear una respuesta de error personalizada, el metodo
     * es utilizado por todos los manejadores de excepciones establecidos en la
     * clase padre.
     *
     * @param excepcion Excepcion ocurrida
     * @param cuerpo Contenido para el {@code Response}
     * @param cabecera Cabeceras para el {@code Response}
     * @param httpStatus Codigo de estado HTTP para el {@code Response}
     * @param peticion Envoltorio de la peticion hecha por el usuario
     * @return ResponseEntity Bean que encapsula el Response para el usuario,
     * establece el codigo de estado de HTTP y el ResponseBody
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception excepcion, Object cuerpo,
            HttpHeaders cabecera, HttpStatus httpStatus, WebRequest peticion) {

        logger.error(excepcion.getClass(), excepcion);
        ExceptionDto respuesta = ExceptionDto.builder()
                .httpStatus(httpStatus)
                .path(this.extractPath(peticion))
                .message(Message.UNKNOWN_ERROR.getText())
                .build();
        return super.handleExceptionInternal(excepcion, respuesta, cabecera, httpStatus, peticion);
    }

    /**
     * Extrae la path del servicio que lanzo la excepcion.
     *
     * @param request Envoltorio de la peticion hecha por el usuario
     * @return String Ruta que lanzo la excepcion
     */
    private String extractPath(WebRequest request) {
        return StringUtils.replace(request.getDescription(false), "uri=", "");
    }
}
