/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfInconsistenciaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfResProcesoNocturnoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfUpdateStatusDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfInconsistencia;
import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.repositories.CatDatoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CreditoFiscalRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.InconsistenciaRepository;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ajabarrer
 */
@Transactional
@Service
@Slf4j
public class SdcfProcesoNocturoService {
    
    @Autowired
    private ConsumosService consumoService;
    
    @Autowired
    private CreditoFiscalRepository creditoFiscal;

    @Autowired
    private InconsistenciaRepository inconsistenciaRepository;
    
    @Autowired
    private CatDatoRepository catDatoRepository;
    
    @Scheduled(cron = "0 0 1 * * ?", zone = "America/Guatemala")
    public SdcfResProcesoNocturnoDto ResultadoProcesoNocturno() throws ParseException {
        System.out.println("Inicia Proceso Nocturno de las solicitudes de devolucion de credito fiscal");
        GeneralResponseDto<String> resultado = this.consumoService.consumeCompleteUrlOracle(null,"/sdcf/iniciarProceso/", GeneralResponseDto.class, HttpMethod.GET);
        SdcfResProcesoNocturnoDto res = new Gson().fromJson(resultado.getData(), SdcfResProcesoNocturnoDto.class);
        
        List<SdcfInconsistenciaDto> listaIncosistencias = res.getInconsistencias();
          for (SdcfInconsistenciaDto inconsistencia : listaIncosistencias) {
                inconsistenciaRepository.save(SipfInconsistencia.builder()
                .numeroSolicitud(inconsistencia.getNumeroSolicitud().intValue())
                    .declaracionRepetida(inconsistencia.getDeclaracionRepetida().intValue())
                    .descripcion(inconsistencia.getDescripcion())
                    .facturaNumero(inconsistencia.getFacturaNumero())
                    .facturaProveedor(inconsistencia.getFacturaProveedor())
                    .facturaSerie(inconsistencia.getFacturaSerie())
                    .fechaGrabacion(new Date())
                    .numeroDeclaracion(inconsistencia.getIdSolicitudDeclaracion().intValue())
                    .tipoInconsistencia(catDatoRepository.getCodigoByCatalogoAndCodIngresado(inconsistencia.getIdTipoInconsistencia().toString(), 105))
                    .tipoRepetida(inconsistencia.getTipoRepetida().intValue())
                    .usuarioGrabacion("proceso nocturno")
                    .build()
                );
          }
        List<SdcfUpdateStatusDto> listaSolicitudes = res.getUpdateSolicitudes();
            for(SdcfUpdateStatusDto solicitud : listaSolicitudes){
                final SipfSolicitudCreditoFiscal updateSoli = creditoFiscal.findByNumeroSolicitud(solicitud.getNumeroSolicitud()).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.UNKNOWN_ERROR.getText(solicitud.getNumeroSolicitud().intValue())));
        
        if(solicitud.getNuevoStatus() == 3){
            updateSoli.setEstado(1076);
        }
        if(solicitud.getNuevoStatus() == 4){
            updateSoli.setEstado(1075);
        }
        updateSoli.setFechaModifica(new Date());
        updateSoli.setUsuarioModifica("proceso nocturno");

        creditoFiscal.save(updateSoli);
            }
        
        return res;
    }
}
