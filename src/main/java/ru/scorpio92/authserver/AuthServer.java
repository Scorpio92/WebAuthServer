package ru.scorpio92.authserver;

import ru.scorpio92.authserver.api.PublicAPI;
import ru.scorpio92.authserver.tools.Logger;

/**
 * Основной класс
 */
public class AuthServer {

    public static void main(String[] args) throws Throwable {

        //инициализация глобального конфига
        ServerConfigStore.init();
        Logger.log("ServerConfigStore init complete");

        //инициализация публичной части API
        new PublicAPI().start();
    }
}
