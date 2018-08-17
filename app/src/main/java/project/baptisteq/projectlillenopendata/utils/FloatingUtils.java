package project.baptisteq.projectlillenopendata.utils;

import java.math.BigDecimal;

/**
 * Created by Baptiste on 01/06/18.
 */

/**
 * Classe utilitaire afin de réaliser des traitement sur des nombres décimaux
 */
public class FloatingUtils {

    public static final int DECIMAL_DEFAULT_ROUND = 2;

    public static float round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static float roundWithTwoDecimals(double d) {
        return round(d, DECIMAL_DEFAULT_ROUND);
    }
}
