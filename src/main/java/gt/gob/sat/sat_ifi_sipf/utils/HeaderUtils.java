/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 *
 * @author crramosl
 */
public final class HeaderUtils {

    public static HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                set("Accept", "application/json;charset=UTF-8");
                setContentType(MediaType.APPLICATION_JSON);
            }
        };
    }

    public static HttpHeaders createHeaders() {
        return new HttpHeaders() {
            {
                set("Accept", "application/json;charset=UTF-8");
                setContentType(MediaType.APPLICATION_JSON);
            }
        };
    }
}
