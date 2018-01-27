package ru.scorpio92.authserver.tools;

public class Logger {

    public static void log(String msg) {
        System.out.println("LOGGER-> " + msg);
    }

    public static void log(String tag, String msg) {
        System.out.println("LOGGER-> " + tag + ": " + msg);
    }

    public static void error(String error) {
        System.err.println("LOGGER EXCEPTION-> " + error);
    }

    public static void error(String tag, String error) {
        System.err.println("LOGGER EXCEPTION-> " + tag + ": " + error);
    }

    public static void error(Exception e) {
        System.err.println("LOGGER EXCEPTION-> " + e.getMessage());
    }
}
