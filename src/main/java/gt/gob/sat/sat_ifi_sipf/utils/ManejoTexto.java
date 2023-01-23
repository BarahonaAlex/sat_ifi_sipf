package gt.gob.sat.sat_ifi_sipf.utils;

import java.util.Arrays;

/**
 *
 * @author asacanoes
 */
public class ManejoTexto {

    public static String encodeString(String str) {
        str = str.replaceAll("á", "&aacute;");
        str = str.replaceAll("é", "&eacute;");
        str = str.replaceAll("í", "&iacute;");
        str = str.replaceAll("ó", "&oacute;");
        str = str.replaceAll("ú", "&uacute;");
        str = str.replaceAll("Á", "&Aacute;");
        str = str.replaceAll("É", "&Eacute;");
        str = str.replaceAll("Í", "&Iacute;");
        str = str.replaceAll("Ó", "&Oacute;");
        str = str.replaceAll("Ú", "&Uacute;");
        str = str.replaceAll("ñ", "&ntilde;");
        str = str.replaceAll("Ñ", "&Ntilde;");
        return str;
    }

    public static String formatName(String nombre) {
        return Arrays.asList(nombre.replaceAll(",", " ").split(" "))
                .stream()
                .map(word -> word.length() > 0 ? String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1).toLowerCase() : "")
                .reduce("", (a, b) -> a.concat(" ".concat(b)), String::concat).trim();
    }

    public static String getSafe(Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }
}
