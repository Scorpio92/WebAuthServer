package ru.scorpio92.authserver.api;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

import ru.scorpio92.authserver.ServerConfigStore;
import ru.scorpio92.authserver.tools.Logger;

/**
 * Базовый API класс поддерживающий выполнение в отдельном потоке
 * и содержащий инстансы HTTP и WebSocket серверов
 */
public abstract class API extends Thread {

    @Override
    public void run() {
        try {
            Logger.log("Start API: " + this.getClass().getSimpleName());
            callable();
        } catch (Exception e) {
            Logger.error("Failed start API: " + this.getClass().getSimpleName());
            Logger.error(e);
        }
    }

    /**
     * Метод в котором запускается код API
     *
     * @throws Exception
     */
    protected abstract void callable() throws Exception;

    public static HttpServer getHttpServerInstance(HttpHandler httpHandler) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(ServerConfigStore.SERVER_PORT), 0);
        server.createContext("/", httpHandler);
        return server;
    }
}
