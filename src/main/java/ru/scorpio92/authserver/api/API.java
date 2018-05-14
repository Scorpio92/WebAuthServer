package ru.scorpio92.authserver.api;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

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
        server.bind(new InetSocketAddress(ServerConfigStore.PAPI_SERVER_PORT), 0);
        server.createContext("/", httpHandler);
        return server;
    }

    public static void startWSServerInstance(IWSHandler hander) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        try {
            serverSocketChannel.socket().bind(new InetSocketAddress(ServerConfigStore.SAPI_SERVER_PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // Проверяем, если ли какие-либо активности -
                // входящие соединения или входящие данные в
                // существующем соединении.
                int channelCount = selector.select();

                // Если никаких активностей нет, выходим из цикла
                // и снова ждём.
                if (channelCount == 0) {
                    continue;
                }

                // Получим ключи, соответствующие активности,
                // которые могут быть распознаны и обработаны один за другим.
                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    // Получим ключ, представляющий один из битов
                    // активности ввода/вывода.
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        hander.onNewConnection(client);
                    }

                    iterator.remove();
                }
            }
        } finally {
            serverSocketChannel.close();
            selector.close();
        }
    }

    public interface IWSHandler {
        void onNewConnection(SocketChannel client) throws Exception;
    }
}
