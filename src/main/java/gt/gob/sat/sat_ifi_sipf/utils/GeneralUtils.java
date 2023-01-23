/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.utils;

import java.util.Arrays;

/**
 *
 * @author crramosl
 */
public class GeneralUtils {

    public static boolean isSameOrNull(Object arg1, Object arg2) {
        if (arg1 == null) {
            return true;
        }
        return arg1 instanceof String && arg2 instanceof String
                ? ((String) arg1).equalsIgnoreCase((String) arg2)
                : arg1.equals(arg2);
    }

    public static int countNull(Object... args) {
        return Arrays.asList(args).stream().map(arg -> arg == null ? 1 : 0).reduce(0, Integer::sum);
    }
}
