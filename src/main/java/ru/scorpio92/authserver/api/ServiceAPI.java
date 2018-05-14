package ru.scorpio92.authserver.api;

import java.nio.channels.SocketChannel;

/**
 * Сервисная часть API
 * Реализовано на быстрой java.NIO в многопоточном исполнении
 */
public class ServiceAPI extends API implements API.IWSHandler {

    @Override
    protected void callable() throws Exception {
        API.startWSServerInstance(this);
    }

    @Override
    public void onNewConnection(SocketChannel client) throws Exception {
        new WSConnectionHandler(client).start();
    }
}
