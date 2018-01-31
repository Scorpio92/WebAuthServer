package ru.scorpio92.authserver.usecase;

import ru.scorpio92.authserver.crypto.AES;
import ru.scorpio92.authserver.db.AuthInfoTable;
import ru.scorpio92.authserver.entity.SessionKey;
import ru.scorpio92.authserver.entity.message.ErrorMessage;
import ru.scorpio92.authserver.entity.message.ServiceAPIMessage;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;

/**
 * Created by scorpio92 on 1/31/18.
 */

public class EncryptUsecase extends MessageBaseUsecase<ServiceAPIMessage> {

    @Override
    protected BaseMessage handler(ServiceAPIMessage message) throws Exception {
        String sessionKeyId = message.getSessionKeyId();
        AuthInfoTable authInfoTable = new AuthInfoTable();
        if(authInfoTable.checkKeyIsActive(sessionKeyId)) {
            SessionKey sessionKey = authInfoTable.getSessionKey(sessionKeyId);
            String encryptedPayload = AES.build(sessionKey.getSessionKey(), sessionKey.getIV()).encryptToBase64(message.getPayload());
            Logger.log("AES", encryptedPayload);
            return new BaseMessage(BaseMessage.MessageType.ENCRYPT, encryptedPayload);
        } else {
            return new ErrorMessage(ErrorMessage.CRYPTO_SESSION_KEY_IS_OVERDUE);
        }
    }

    @Override
    protected ServiceAPIMessage getMessage(String requestBody) throws Exception {
        return JsonWorker.getDeserializeJson(requestBody, ServiceAPIMessage.class);
    }
}
