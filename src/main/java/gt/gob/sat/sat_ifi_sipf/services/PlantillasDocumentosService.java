/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.dtos.ErrorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PlantillasDocumentosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfPlantillasDocumentos;
import gt.gob.sat.sat_ifi_sipf.repositories.PlantillasDocumentosRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author ajabarrer
 */
@Transactional
@Service
public class PlantillasDocumentosService {
    
    @Autowired
    private PlantillasDocumentosRepository plantillasRepository;

    @Autowired
    private Detector detector;
    
    @Transactional(readOnly=true)
    public List<SipfPlantillasDocumentos> findAllPlantillasDocumentos(){
        return plantillasRepository.findAll();
    }
    @Transactional(readOnly=true)
    public SipfPlantillasDocumentos findPlantillasDocumentosById(Integer id){
        return  plantillasRepository.findById(id).orElse(null);
        
    }
    
    @Transactional (rollbackFor = Exception.class)
    public SipfPlantillasDocumentos createPlantillasDocumentos(PlantillasDocumentosDto dto){
        final  SipfPlantillasDocumentos  plantillaDocumentos = new SipfPlantillasDocumentos();
        plantillaDocumentos.setCatalogos(dto.getCatalogos());
        plantillaDocumentos.setEncabezado(dto.getEncabezado());
        plantillaDocumentos.setFechaModifica(dto.getFechaModifica());
        plantillaDocumentos.setIpModificia(dto.getIpModificia());
        plantillaDocumentos.setNombre(dto.getNombre());
        plantillaDocumentos.setPiePagina(dto.getPiePagina());
        plantillaDocumentos.setUsuarioModifica(dto.getUsuarioModifica());
        
        return plantillasRepository.save(plantillaDocumentos);
    }
    
    @Transactional
    public SipfPlantillasDocumentos updatePlantilla(Integer id, PlantillasDocumentosDto dto,  UserLogged user) {
        final SipfPlantillasDocumentos plantillaDocumentos = plantillasRepository.findById(id).orElse(null);
         
        List<String> variables = dto.getVaribalesIngresadas() == null ? new ArrayList() : dto.getVaribalesIngresadas().stream()
                .filter(t -> !dto.getVaribalesPermitidas().stream().collect(toSet()).contains(t)).collect(Collectors.toList());
        
        
        if(!variables.isEmpty()){
            
            final List<ErrorDto> errors = new ArrayList<>();
                variables.forEach(t -> {
                     errors.add(
                                ErrorDto.builder()
                                        .param("Variables no permitidas")
                                        .value(t)
                                        .description("")
                                        .build()
                        );
                });
               
                
             throw BusinessException.badRequestError("Variable no permitida", errors);
        }
        
        plantillaDocumentos.setCatalogos(plantillaDocumentos.getCatalogos());
        plantillaDocumentos.setEncabezado(dto.getEncabezado());
        plantillaDocumentos.setFechaModifica(new Date());
        plantillaDocumentos.setIpModificia(user.getIp());
        plantillaDocumentos.setNombre(dto.getNombre());
        plantillaDocumentos.setPiePagina(dto.getPiePagina());
        plantillaDocumentos.setUsuarioModifica(user.getLogin());
        

        plantillasRepository.save(plantillaDocumentos);


        return plantillaDocumentos;
    }
}
