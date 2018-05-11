package ru.scorpio92.authserver.tools;

import ru.scorpio92.authserver.ServerConfigStore;

public class Logger {


    public static void log(String msg) {
        if (ServerConfigStore.LOGGER_ENABLED)
            System.out.println("LOGGER-> " + msg);
    }

    public static void log(String tag, String msg) {
        if (ServerConfigStore.LOGGER_ENABLED)
            System.out.println("LOGGER-> " + tag + ": " + msg);
    }

    public static void error(String error) {
        if (ServerConfigStore.LOGGER_ENABLED)
            System.err.println("LOGGER EXCEPTION-> " + error);
    }

    public static void error(Exception e) {
        if (ServerConfigStore.LOGGER_ENABLED && e != null)
            System.err.println("LOGGER EXCEPTION-> " + e.getMessage());
    }

    public static void error(String tag, Exception e) {
        if (ServerConfigStore.LOGGER_ENABLED && e != null)
            System.err.println("LOGGER EXCEPTION-> " + tag + ": " + e.getMessage());
    }
}
