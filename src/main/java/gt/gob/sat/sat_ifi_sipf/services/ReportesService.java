/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceMasivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.AniosPlanAnualProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CedulaVerificacionProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReporteAlcanceMasivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReporteAlcanceSelectivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReporteAlcanceSolicitudProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ReportesAlcancesRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ruarcuse
 */
@Transactional
@Service
@Slf4j
public class ReportesService {

    @Autowired(required = true)
    private ReportesAlcancesRepository sipfReportesRepository;

    @Autowired
    Detector detector;

    @Transactional(readOnly = true)
    public List<AlcanceProjection> getAlcanceId(String pId) {
        //log.debug("Obtener datos de Alcance");
        return sipfReportesRepository.getAlcance(pId);
    }

    public List<AlcanceMasivoProjection> getAlcanceMasivoXId(Integer pId) {
        //log.debug("Obtener datos de Alcance");
        return sipfReportesRepository.getAlcanceMasivo(pId);
    }

    public List<ReporteAlcanceSelectivoProjection> getAlcanceSelectivoId(String nit) {
        //log.debug("Obtener datos de Alcance Selectivo");
        return sipfReportesRepository.getReporteAlcanceSelectivo(nit);
    }

    public List<ReporteAlcanceSolicitudProjection> getAlcanceSolicitudId(String nit) {
        //log.debug("Obtener datos de Alcance Solicitud");
        return sipfReportesRepository.getReporteAlcanceSolicitud(nit);
    }

    public List<ReporteAlcanceMasivoProjection> getAlcanceMasivoId(Integer id) {
        //log.debug("Obtener datos de Alcance Masivo");
        return sipfReportesRepository.getReporteAlcanceMasivo(id);
    }
    
    public List<CedulaVerificacionProjection> getCedulaVerificacion(Integer id) {
        
        return sipfReportesRepository.getCedulaVerificacion(id);
    }
}
