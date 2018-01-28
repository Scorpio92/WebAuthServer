package ru.scorpio92.authserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.security.Security;

import ru.scorpio92.authserver.crypto.KeyStorage;
import ru.scorpio92.authserver.entity.message.AuthorizeMessage;
import ru.scorpio92.authserver.entity.message.ErrorMessage;
import ru.scorpio92.authserver.entity.message.RegisterMessage;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.usecase.AuthorizeUsecase;
import ru.scorpio92.authserver.usecase.GetServerKeyUsecase;
import ru.scorpio92.authserver.usecase.RegisterUsecase;


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

            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf8"));
            StringBuilder request = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                request.append(line);
            }

            try {
                reader.close();
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }

            try {
                sendMessageToClient(exchange, handleMessage(request.toString()));
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

            switch (baseMessage.getType()) {
                case GET_SERVER_KEY:
                    return new GetServerKeyUsecase().handleAndReturnResponse(baseMessage);
                case REGISTER:
                    RegisterMessage registerMessage = JsonWorker.getDeserializeJson(requestBody, RegisterMessage.class);
                    return new RegisterUsecase().handleAndReturnResponse(registerMessage);
                case AUTHORIZE:
                    AuthorizeMessage authorizeMessage = JsonWorker.getDeserializeJson(requestBody, AuthorizeMessage.class);
                    return new AuthorizeUsecase().handleAndReturnResponse(authorizeMessage);
            }

        } else {
            throw new IllegalArgumentException();
        }

        return null;
    }

    //отсылаем ответ клиенту
    static void sendMessageToClient(HttpExchange exchange, BaseMessage message) {
        OutputStream os = exchange.getResponseBody();
        if (message != null) {
            try {
                exchange.sendResponseHeaders(200, 0);
                os.write(message.toString().getBytes(Charset.forName("UTF-8")));
                os.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                exchange.sendResponseHeaders(200, 0);
                os.write(new ErrorMessage(ErrorMessage.WTF).toString().getBytes(Charset.forName("UTF-8")));
                os.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}