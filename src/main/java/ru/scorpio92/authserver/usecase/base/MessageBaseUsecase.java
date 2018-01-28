package ru.scorpio92.authserver.usecase.base;

import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.Logger;

/**
 * Created by scorpio92 on 1/15/18.
 */

public abstract class MessageBaseUsecase<M extends BaseMessage> {

    public BaseMessage handle(String requestBody) {
        try {
            M message = getMessage(requestBody);
            return handler(message);
        } catch (Exception e) {
            Logger.error(this.getClass().getSimpleName(), e);
        }
        return null;
    }

    protected abstract BaseMessage handler(M message) throws Exception;

    protected abstract M getMessage(String requestBody) throws Exception;
}
