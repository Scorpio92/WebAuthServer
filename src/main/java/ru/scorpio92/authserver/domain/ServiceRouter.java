package ru.scorpio92.authserver.domain;

import java.io.UnsupportedEncodingException;

import ru.scorpio92.authserver.Constants;
import ru.scorpio92.authserver.data.model.message.base.BaseMessage;
import ru.scorpio92.authserver.data.model.message.base.ErrorCode;
import ru.scorpio92.authserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;

/**
 * Класс скрывающий в себе логику по обработке запросов
 */
public class ServiceRouter {

    private String requestBody;

    public ServiceRouter(String requestBody) {
        this.requestBody = requestBody;
    }

    public byte[] getResult() throws UnsupportedEncodingException {
        BaseMessage responseMessage;
        try {
            BaseMessage requestMessage = JsonWorker.getDeserializeJson(requestBody, BaseMessage.class);

            switch (requestMessage.getType()) {
                case REGISTER:
                    responseMessage = new RegisterUseCase().execute(requestMessage);
                    break;
                case AUTHORIZE:
                    responseMessage = new AuthorizeUseCase().execute(requestMessage);
                    break;
                case DEAUTHORIZE:
                    responseMessage = new DeauthorizeUseCase().execute(requestMessage);
                    break;
                default:
                    throw new IllegalArgumentException("unknown message type");
            }
        } catch (Exception e) {
            Logger.error(e);
            responseMessage = new ErrorMessage(ErrorCode.General.WTF);
        }

        return JsonWorker.getSerializeJson(responseMessage).getBytes(Constants.CHARSET);
    }
}
