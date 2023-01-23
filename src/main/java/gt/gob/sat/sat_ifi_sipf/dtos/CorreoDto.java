package gt.gob.sat.sat_ifi_sipf.dtos;

import gt.gob.sat.sat_ifi_sipf.utils.ManejoTexto;

/**
 *
 * @author crramosl
 */
public class CorreoDto {

    private String asunto;
    private String destinatarios;
    private String contenido;
    private String separadorDestinatarios;
    private String remitente;
    private boolean html;

    public CorreoDto() {
    }

    public CorreoDto(String asunto, String destinatarios, String contenido, String separadorDestinatarios, String remitente, boolean html) {
        this.asunto = asunto;
        this.destinatarios = destinatarios;
        this.contenido = ManejoTexto.encodeString(contenido);
        this.separadorDestinatarios = separadorDestinatarios;
        this.remitente = remitente;
        this.html = html;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = ManejoTexto.encodeString(contenido);
    }

    public String getSeparadorDestinatarios() {
        return separadorDestinatarios;
    }

    public void setSeparadorDestinatarios(String separadorDestinatarios) {
        this.separadorDestinatarios = separadorDestinatarios;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "CorreoDto{" + "asunto=" + asunto + ", destinatarios=" + destinatarios + ", contenido=" + contenido + ", separadorDestinatarios=" + separadorDestinatarios + ", remitente=" + remitente + ", html=" + html + '}';
    }
}
