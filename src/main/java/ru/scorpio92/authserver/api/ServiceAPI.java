package ru.scorpio92.authserver.api;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import ru.scorpio92.authserver.ServerConfigStore;
import ru.scorpio92.authserver.tools.Logger;

/**
 * Сервисная часть API
 * Реализовано на быстрой java.NIO в однопоточном исполнении (пока что)
 */
public class ServiceAPI extends API {

    @Override
    protected void callable() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(ServerConfigStore.SAPI_SERVER_PORT));
        //serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer buffer = ByteBuffer.allocate(8192);

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
                    client.register(selector, SelectionKey.OP_READ);

                    Logger.log("client " + client.getRemoteAddress() + " connected to public API");

                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    StringBuilder sb = new StringBuilder();

                    buffer.clear();
                    while (client.read(buffer) > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.limit()];
                        buffer.get(bytes);
                        sb.append(new String(bytes));
                        buffer.clear();
                    }

                    Logger.log("client send data: ", sb.toString());

                    try {
                        buffer.put(handleMessage(sb.toString()));
                        buffer.flip();
                        client.write(buffer);
                    } catch (Exception e) {
                        Logger.error(e);
                    }

                    client.close();
                }

                iterator.remove();
            }
        }
    }

    private byte[] handleMessage(String s) {
        return s.getBytes();
    }
}
