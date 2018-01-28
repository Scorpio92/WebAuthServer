package ru.scorpio92.authserver.usecase;

import ru.scorpio92.authserver.crypto.KeyStorage;
import ru.scorpio92.authserver.entity.ServerKeyPair;
import ru.scorpio92.authserver.entity.message.GetServerKeyMessage;
import ru.scorpio92.authserver.entity.message.GetServerKeyPayload;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;

/**
 * Created by scorpio92 on 1/15/18.
 */

public class GetServerKeyUsecase extends MessageBaseUsecase<GetServerKeyMessage> {

    @Override
    protected BaseMessage handler(GetServerKeyMessage message) throws Exception {
        //получаем новую пару публичный-закрытый ключ
        ServerKeyPair serverKeyPair = KeyStorage.getNewPair();
        //отдаем id пары и сам публичный ключ в пэйлоаде
        return new GetServerKeyMessage(new GetServerKeyPayload(serverKeyPair.getPairId(), serverKeyPair.getPublicKeyStr()));
    }

    @Override
    protected GetServerKeyMessage getMessage(String requestBody) throws Exception {
        return JsonWorker.getDeserializeJson(requestBody, GetServerKeyMessage.class);
    }
}
