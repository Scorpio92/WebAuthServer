package ru.scorpio92.authserver.usecase;

import ru.scorpio92.authserver.crypto.KeyStorage;
import ru.scorpio92.authserver.db.AccountsTable;
import ru.scorpio92.authserver.db.AuthInfoTable;
import ru.scorpio92.authserver.entity.ServerKeyPair;
import ru.scorpio92.authserver.entity.SessionKey;
import ru.scorpio92.authserver.entity.message.AuthorizeMessage;
import ru.scorpio92.authserver.entity.message.AuthorizePayload;
import ru.scorpio92.authserver.entity.message.ErrorMessage;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;

/**
 * Created by scorpio92 on 1/22/18.
 */

public class AuthorizeUsecase extends MessageBaseUsecase {

    @Override
    public BaseMessage handleAndReturnResponse(BaseMessage requestMessage) {
        BaseMessage message = null;

        try {
            AuthorizeMessage authorizeMessage = (AuthorizeMessage) requestMessage;
            //получаем из запроса id пары ключей
            String pairId = authorizeMessage.getServerPublicKeyId();
            //достаем публичный ключ клиента
            String clientPublicKey = authorizeMessage.getClientPublicKey();
            //достаем по id пару публичный-закрытый ключ
            ServerKeyPair serverKeyPair = KeyStorage.getKeyPairById(pairId);

            if(serverKeyPair != null) {
                //достаем из запроса пэйлоад и расшифровываем его
                AuthorizePayload authorizePayload = authorizeMessage.getPayload(serverKeyPair.getPrivateKey());
                AccountsTable accountsTable = new AccountsTable();
                if (accountsTable.checkAuthTokenExists(authorizePayload.getAuthToken())) { //проверяем валидность токена
                    //генерируем новый сессионный ключ
                    SessionKey sessionKey = SessionKey.build(authorizePayload.getAuthToken(), clientPublicKey);
                    AuthInfoTable authInfoTable = new AuthInfoTable();
                    //записываем информацию об авторизации в таблицу
                    authInfoTable.setAuthInfo(authorizePayload.getAuthToken(), sessionKey);
                    message = new AuthorizeMessage(sessionKey.getIV(), new AuthorizePayload(sessionKey.getSessionKey()), clientPublicKey);
                } else {
                    message = new ErrorMessage(ErrorMessage.BAD_TOKEN);
                }
            } else {
                message = new ErrorMessage(ErrorMessage.PUBLIC_KEY_IS_OVERDUE);
            }
        } catch (Exception e) {
            Logger.error("AuthorizeUsecase", e.getMessage());
        }

        return message;
    }
}
