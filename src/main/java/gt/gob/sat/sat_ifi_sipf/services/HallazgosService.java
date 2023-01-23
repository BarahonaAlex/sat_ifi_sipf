    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;


import gt.gob.sat.sat_ifi_sipf.dtos.HallazgosEncontradosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfHallazgoDenunciaGabinete;
import gt.gob.sat.sat_ifi_sipf.models.SipfHallazgosEncontrados;
import gt.gob.sat.sat_ifi_sipf.models.SipfRubroHallazgo;
import gt.gob.sat.sat_ifi_sipf.models.SipfRubroHallazgoDenunciaGabinete;
import gt.gob.sat.sat_ifi_sipf.projections.HallazgoDetalleProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.HallazgoDenunciaGabineteRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HallazgosEncontradosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.RubroHallazgoDenunciaGabineteRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.RubroHallazgoRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ruarcuse
 */
@Service
@Slf4j
public class HallazgosService {

    @Autowired
    private HallazgosEncontradosRepository findingRepository;

    @Autowired
    private RubroHallazgoRepository rubroHallazgo;
    
    @Autowired
    private HallazgoDenunciaGabineteRepository findingCabinetRepository;
    
    @Autowired
    private RubroHallazgoDenunciaGabineteRepository rubroCabinetRepository;
    
    

    @Transactional(rollbackFor = {Exception.class})
    public void createFinding(HallazgosEncontradosDto dto) {
        SipfHallazgosEncontrados finding = findingRepository.save(
                SipfHallazgosEncontrados.builder()
                        .nombre(dto.getNombre())
                        .descripcion(dto.getDescripcion())
                        .idCaso(dto.getCaso())
                        .build()
        );

        dto.getRubros().forEach(idRubro -> {
            rubroHallazgo.save(new SipfRubroHallazgo(idRubro, finding.getIdHallazgo()));
        });
    }

    @Transactional(rollbackFor = {Exception.class})
    public void editFinding(Integer idFinding, HallazgosEncontradosDto dto) {
        SipfHallazgosEncontrados finding = findingRepository.findById(idFinding)
                .orElseThrow(()
                        -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el hallazgo"
                ));

        finding.setDescripcion(dto.getDescripcion());
        finding.setNombre(dto.getNombre());

        rubroHallazgo.deleteByIdHallazgo(idFinding);

        dto.getRubros().forEach(idRubro -> {
            rubroHallazgo.save(new SipfRubroHallazgo(idRubro, finding.getIdHallazgo()));
        });
    }

    @Transactional(readOnly = true)
    public List<HallazgoDetalleProjection> getDetailedFoundings(String nitPro, Integer idCase) {
        return findingRepository.getDetailFindings(nitPro, idCase);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteFinding(Integer idFinding) {
        rubroHallazgo.deleteByIdHallazgo(idFinding);
        findingRepository.deleteById(idFinding);
    }
    
    
    //Hallazgos de denuncias gabinete
    @Transactional(rollbackFor = {Exception.class})
    public void createFindingCabinet(HallazgosEncontradosDto dto, UserLogged logged) {
        SipfHallazgoDenunciaGabinete findingCabinet = findingCabinetRepository.save(
               SipfHallazgoDenunciaGabinete.builder()
                        .nombre(dto.getNombre())
                        .descripcion(dto.getDescripcion())
                        .correlativo(dto.getCorrelativo())
                        .usuarioModifica(logged.getLogin())
                        .ipModifica(logged.getIp())
                        .fechaModifica(new Date())
                        .build()
        );

        dto.getRubros().forEach(idRubro -> {
            rubroCabinetRepository.save(
                    new SipfRubroHallazgoDenunciaGabinete(
                            idRubro, 
                            findingCabinet.getIdHallazgo(),
                            logged.getLogin(),
                            new Date(),
                            logged.getIp()
                    ));
        });
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public void editFindingCabinet(Integer idFindingCabinet, HallazgosEncontradosDto dto, UserLogged logged) {
        SipfHallazgoDenunciaGabinete findingCabinet = findingCabinetRepository.findById(idFindingCabinet)
                .orElseThrow(()
                        -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el hallazgo"
                ));

        findingCabinet.setDescripcion(dto.getDescripcion());
        findingCabinet.setNombre(dto.getNombre());
        findingCabinet.setUsuarioModifica(logged.getLogin());
        findingCabinet.setFechaModifica(new Date());
        findingCabinet.setIpModifica(logged.getIp());

        rubroCabinetRepository.deleteByIdHallazgo(idFindingCabinet);

        dto.getRubros().forEach(idRubro -> {
            rubroCabinetRepository.save(
                    new SipfRubroHallazgoDenunciaGabinete(
                            idRubro, 
                            findingCabinet.getIdHallazgo(),
                            logged.getLogin(),
                            new Date(),
                            logged.getIp()                            
                    ));
        });
    }
    
    @Transactional(readOnly = true)
    public List<HallazgoDetalleProjection> getDetailedFoundingsCabinet(String nitPro, String correlativo) {
        return findingCabinetRepository.getDetailFindings(nitPro, correlativo);
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public void deleteFindingCabinet(Integer idFinding) {
        rubroCabinetRepository.deleteByIdHallazgo(idFinding);
        findingCabinetRepository.deleteById(idFinding);
    }
}
