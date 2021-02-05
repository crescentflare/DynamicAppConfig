package com.crescentflare.dynamicappconfigexample.utility;


import android.content.Context;
import android.util.Log;

import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigLogLevel;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A utility to log to the system and to a file
 * Uses the global setting to determine if the log should be written
 * Used by the log plugin to show the log from the selection menu
 */
public class Logger {

    // --
    // Constants
    // --

    private static final String LOG_TAG = "App config logger";
    private static final String LOG_FILENAME = "app_log.txt";


    // --
    // Members
    // --

    private static Context applicationContext;
    private static String logFileString = null;


    // --
    // Logging methods
    // --

    public static void setApplicationContext(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public static void log(String text) {
        if (ExampleAppConfigManager.currentConfig().getLogLevel() != ExampleAppConfigLogLevel.LogDisabled) {
            Log.d(LOG_TAG, text);
            logToFile(text);
        }
    }

    public static void logVerbose(String text) {
        if (ExampleAppConfigManager.currentConfig().getLogLevel() == ExampleAppConfigLogLevel.LogVerbose) {
            Log.v(LOG_TAG, text);
            logToFile(text);
        }
    }


    // --
    // File access
    // --

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void clear() {
        if (applicationContext == null) {
            return;
        }
        File logFile = new File(applicationContext.getCacheDir(), LOG_FILENAME);
        logFile.delete();
        logFileString = "";
    }

    public static String logString() {
        if (applicationContext == null) {
            return "";
        }
        readLogIfNeeded();
        return logFileString;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void logToFile(String text) {
        if (applicationContext == null) {
            return;
        }
        readLogIfNeeded();
        boolean haveLines = logFileString.length() > 0;
        if (logFileString.length() > 0) {
            logFileString = logFileString + "\n" + text;
        } else {
            logFileString = text;
        }
        File logFile = new File(applicationContext.getCacheDir(), LOG_FILENAME);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            if (haveLines) {
                writer.newLine();
            }
            writer.write(text);
            writer.close();
        } catch (Exception ignored) {
        }
    }

    private static void readLogIfNeeded() {
        if (logFileString == null) {
            logFileString = "";
            if (applicationContext == null) {
                return;
            }
            File logFile = new File(applicationContext.getCacheDir(), LOG_FILENAME);
            try {
                BufferedReader reader = new BufferedReader(new FileReader(logFile));
                String lineRead = null;
                logFileString = "";
                do {
                    lineRead = reader.readLine();
                    if (lineRead != null) {
                        if (logFileString.length() == 0) {
                            logFileString = lineRead;
                        } else {
                            logFileString += "\n" + lineRead;
                        }
                    }
                }
                while (lineRead != null);
                reader.close();
            } catch (Exception ignored) {
            }
        }
    }
}
