package ru.scorpio92.authserver.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Публичная часть API
 */
public class PublicAPI extends API implements HttpHandler {

    @Override
    protected void callable() throws Exception {
        API.getHttpServerInstance(this).start();
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        //запуск обработки соеинения с клиентом
        new HttpConnectionHandler(httpExchange).start();
    }
}
