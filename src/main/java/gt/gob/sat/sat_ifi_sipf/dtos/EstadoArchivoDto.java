/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.administrativo.consultas_sipf_ws.dto.respuesta;

/**
 *
 * @author lfvillag
 */
public class EstadoArchivoDto {
  private Boolean  valido;
  private String mensaje;

    public EstadoArchivoDto() {
    }

    public EstadoArchivoDto(Boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }
    
    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
  
  
}
