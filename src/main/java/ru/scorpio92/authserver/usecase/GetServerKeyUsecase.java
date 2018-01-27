package ru.scorpio92.authserver.usecase;

import ru.scorpio92.authserver.crypto.KeyStorage;
import ru.scorpio92.authserver.entity.ServerKeyPair;
import ru.scorpio92.authserver.entity.message.GetServerKeyMessage;
import ru.scorpio92.authserver.entity.message.GetServerKeyPayload;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;

/**
 * Created by scorpio92 on 1/15/18.
 */

public class GetServerKeyUsecase extends MessageBaseUsecase {

    @Override
    public BaseMessage handleAndReturnResponse(BaseMessage requestMessage) {
        BaseMessage message = null;

        try {
            //получаем новую пару публичный-закрытый ключ
            ServerKeyPair serverKeyPair = KeyStorage.getNewPair();
            //отдаем id пары и сам публичный ключ в пэйлоаде
            message = new GetServerKeyMessage(new GetServerKeyPayload(serverKeyPair.getPairId(), serverKeyPair.getPublicKeyStr()));
        } catch (Exception e) {
            Logger.error("GetServerKeyUsecase", e.getMessage());
        }

        return message;
    }
}
