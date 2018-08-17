package project.baptisteq.projectlillenopendata.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Baptiste on 12/05/18.
 * Classes utils à l'affichage de logs
 * Utile également à l'envoi d'exception à Crashlytics
 */

public class LogUtils {

    public enum TypeLog {
        URL("TAG_URL"), ECRAN("TAG_SCREEN"), EXCEPTION("TAG_EXCEPTION"), TEMP("TAG_TEMP");

        TypeLog(String tag) {

            this.tag = tag;
        }

        private String tag;

        @Override
        public String toString() {
            return tag;
        }
    }

    public static void log(TypeLog typeLog, String message) {
            Log.w(typeLog.toString(), message);
    }

    public static void logException(Exception exception) {
        log(TypeLog.EXCEPTION, exception.getMessage());

        Crashlytics.logException(exception);
    }

    public static void logTemp(String message) {
        log(TypeLog.TEMP, message);
    }

    public static void logScreen(String message) {
        log(TypeLog.ECRAN, message);
    }

    public static void refreshInformation() {
        Crashlytics.setUserIdentifier("admin");
        Crashlytics.setUserEmail("admin@admin.admin");
        Crashlytics.setUserName("admin");
    }
}