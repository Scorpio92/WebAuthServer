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
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;

/**
 * Created by scorpio92 on 1/22/18.
 */

public class AuthorizeUsecase extends MessageBaseUsecase<AuthorizeMessage> {

    @Override
    protected BaseMessage handler(AuthorizeMessage message) throws Exception {
        //получаем из запроса id пары ключей
        String pairId = message.getServerPublicKeyId();
        //достаем публичный ключ клиента
        String clientPublicKey = message.getClientPublicKey();
        //достаем по id пару публичный-закрытый ключ
        ServerKeyPair serverKeyPair = KeyStorage.getKeyPairById(pairId);

        if(serverKeyPair != null) {
            //достаем из запроса пэйлоад и расшифровываем его
            AuthorizePayload authorizePayload = message.getPayload(serverKeyPair.getPrivateKey());
            AccountsTable accountsTable = new AccountsTable();
            if (accountsTable.checkAuthTokenExists(authorizePayload.getAuthToken())) { //проверяем валидность токена
                //генерируем новый сессионный ключ
                SessionKey sessionKey = SessionKey.build(authorizePayload.getAuthToken(), clientPublicKey);
                AuthInfoTable authInfoTable = new AuthInfoTable();
                //записываем информацию об авторизации в таблицу
                authInfoTable.setAuthInfo(authorizePayload.getAuthToken(), sessionKey);
                return new AuthorizeMessage(sessionKey.getIV(), new AuthorizePayload(sessionKey.getSessionKey()), clientPublicKey);
            } else {
                return new ErrorMessage(ErrorMessage.BAD_TOKEN);
            }
        } else {
            return new ErrorMessage(ErrorMessage.PUBLIC_KEY_IS_OVERDUE);
        }
    }

    @Override
    protected AuthorizeMessage getMessage(String requestBody) throws Exception {
        return JsonWorker.getDeserializeJson(requestBody, AuthorizeMessage.class);
    }
}
