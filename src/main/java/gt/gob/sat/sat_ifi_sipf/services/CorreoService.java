/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.dtos.CorreoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RespuestaCorreoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 *
 * @author crramosl
 */
@Service
@Slf4j
public class CorreoService {

    @Autowired
    ConsumosService service;

    /**
     * Metodo para enviar corresos desde el servicio
     *
     * @author crramosl
     * @param destinatario Direccion de correo electronico del destinatario.
     * @param cuerpo Contenido del correo a enviar.
     * @param asunto Titulo del correo a enviar.
     * @return respuesta del correo
     */
    public RespuestaCorreoDto envioCorreo(String destinatario, String cuerpo, String asunto) {
        CorreoDto correo = new CorreoDto(
                asunto,
                destinatario,
                cuerpo,
                "",
                "",
                true
        );

        RespuestaCorreoDto respuestaCorreo = service.consume(correo, "sat_rtu/envios/correos", RespuestaCorreoDto.class, HttpMethod.POST);
        return respuestaCorreo;
    }
}
