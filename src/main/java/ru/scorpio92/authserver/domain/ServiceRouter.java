package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.Constants;
import ru.scorpio92.authserver.model.base.BaseMessage;
import ru.scorpio92.authserver.model.base.ErrorCode;
import ru.scorpio92.authserver.model.base.ErrorMessage;
import ru.scorpio92.authserver.tools.JsonWorker;

/**
 * Класс скрывающий в себе логику по обработке запросов
 */
public class ServiceRouter {

    private String requestBody;

    public ServiceRouter(String requestBody) {
        this.requestBody = requestBody;
    }

    public byte[] getResult() throws Exception {
        BaseMessage requestMessage = JsonWorker.getDeserializeJson(requestBody, BaseMessage.class);
        BaseMessage responseMessage;

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
                responseMessage = new ErrorMessage(requestMessage.getType(), ErrorCode.General.WTF);
        }

        return JsonWorker.getSerializeJson(responseMessage).getBytes(Constants.CHARSET);
    }
}
