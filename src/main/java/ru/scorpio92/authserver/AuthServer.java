package ru.scorpio92.authserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.Security;

import ru.scorpio92.authserver.crypto.KeyStorage;
import ru.scorpio92.authserver.entity.message.ErrorMessage;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.usecase.AuthorizeUsecase;
import ru.scorpio92.authserver.usecase.GetServerKeyUsecase;
import ru.scorpio92.authserver.usecase.RegisterUsecase;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;


public class AuthServer {

    public static void main(String[] args) throws Throwable {

        Security.addProvider(new BouncyCastleProvider());

        ServerConfigStore.init();

        KeyStorage.init();

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(ServerConfigStore.SERVER_PORT), 0);
        server.createContext("/", new ConnectionHandler());
        server.start();
    }

    static class ConnectionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            /*
            System.out.println("client: " + exchange.getRemoteAddress().getHostName() + ":" + exchange.getRemoteAddress().getPort());

            System.out.println("protocol: " + exchange.getProtocol());

            Headers headers = exchange.getRequestHeaders();
            for (String header : headers.keySet()) {
                System.out.println(header + "=" + headers.getFirst(header));
            }
            */

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = exchange.getRequestBody().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            String requestBody = result.toString("UTF-8");
            result.flush();
            result.close();

            try {
                sendMessageToClient(exchange, handleMessage(requestBody));
            } catch (Exception e) {
                Logger.error("handleMessage error", e);
                sendMessageToClient(exchange, new ErrorMessage(ErrorMessage.WTF));
            }
        }
    }

    /**
     * обработчик сообщений
     *
     * @param requestBody
     * @return вернет ответное сообщение
     * @throws Exception
     */
    static BaseMessage handleMessage(String requestBody) throws Exception {
        Logger.log("handleMessage", requestBody);
        if (requestBody != null && !requestBody.isEmpty()) {
            BaseMessage baseMessage = JsonWorker.getDeserializeJson(requestBody, BaseMessage.class);
            Logger.log("received message type", baseMessage.getType().name());

            MessageBaseUsecase usecase;

            switch (baseMessage.getType()) {
                case GET_SERVER_KEY:
                    usecase = new GetServerKeyUsecase();
                    break;
                case REGISTER:
                    usecase = new RegisterUsecase();
                    break;
                case AUTHORIZE:
                    usecase = new AuthorizeUsecase();
                    break;
                default:
                    usecase = null;
            }

            if (usecase != null)
                return usecase.handle(requestBody);

        } else {
            throw new IllegalArgumentException();
        }

        return null;
    }

    //отсылаем ответ клиенту
    static void sendMessageToClient(HttpExchange exchange, BaseMessage message) {
        OutputStream os = exchange.getResponseBody();

        try {
            exchange.sendResponseHeaders(200, 0);

            if (message != null)
                os.write(message.getBytes());
            else
                os.write(new ErrorMessage(ErrorMessage.WTF).getBytes());

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }


}