package ru.scorpio92.authserver.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import ru.scorpio92.authserver.domain.ServiceRouter;
import ru.scorpio92.authserver.tools.Logger;

public class WSConnectionHandler extends Thread {

    private SocketChannel channel;
    private Selector sel;

    public WSConnectionHandler(SocketChannel channel) throws IOException {
        this.channel = channel;
        sel = Selector.open();
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        try {
            Logger.log("client " + channel.getRemoteAddress() + " connected to service API");
            channel.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            boolean done = false;
            while (!done) {
                sel.select();
                Iterator it = sel.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    if (key.isReadable()) {
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

                        try {
                            buffer.put(handleMessage(sb.toString()));
                            buffer.flip();
                            client.write(buffer);
                        } finally {
                            //client.close();
                            //done = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                Logger.error(e);
            }
            try {
                sel.close();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    private byte[] handleMessage(String s) throws UnsupportedEncodingException {
        return new ServiceRouter(s).getResult();
    }
}
