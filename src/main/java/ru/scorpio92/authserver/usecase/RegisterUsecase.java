package ru.scorpio92.authserver.usecase;

import ru.scorpio92.authserver.crypto.KeyStorage;
import ru.scorpio92.authserver.db.AccountsTable;
import ru.scorpio92.authserver.entity.Account;
import ru.scorpio92.authserver.entity.ServerKeyPair;
import ru.scorpio92.authserver.entity.message.ErrorMessage;
import ru.scorpio92.authserver.entity.message.RegisterMessage;
import ru.scorpio92.authserver.entity.message.RegisterPayload;
import ru.scorpio92.authserver.entity.message.base.BaseMessage;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.usecase.base.MessageBaseUsecase;

/**
 * Created by scorpio92 on 1/16/18.
 */

public class RegisterUsecase extends MessageBaseUsecase {

    @Override
    public BaseMessage handleAndReturnResponse(BaseMessage requestMessage) {
        BaseMessage message = null;

        try {
            RegisterMessage registerMessage = (RegisterMessage) requestMessage;
            //получаем из запроса id пары ключей
            String pairId = registerMessage.getServerPublicKeyId();
            //достаем публичный ключ клиента
            String clientPublicKey = registerMessage.getClientPublicKey();
            //достаем по id пару публичный-закрытый ключ
            ServerKeyPair serverKeyPair = KeyStorage.getKeyPairById(pairId);

            if(serverKeyPair != null) {
                //достаем из запроса пэйлоад и расшифровываем его
                RegisterPayload registerPayload = registerMessage.getPayload(serverKeyPair.getPrivateKey());
                //получаем никнейм клинета
                String nickname = registerPayload.getNickname();
                if (validateNickname(nickname)) {
                    AccountsTable accountsTable = new AccountsTable();
                    if (!accountsTable.checkAccountExists(registerPayload.getNickname())) {
                        //создаем новый объект аккаунта с автогенерацией токена
                        Account account = Account.create(registerPayload.getNickname(), registerPayload.getEmail());
                        accountsTable.insertAccount(account);
                        //отдаем ответ с токеном и шуфруем пэйлоад публичным ключом клиента
                        return new RegisterMessage(new RegisterPayload(account.getAuthToken()), clientPublicKey);
                    } else {
                        message = new ErrorMessage(ErrorMessage.NICKNAME_EXISTS);
                    }
                } else {
                    message = new ErrorMessage(ErrorMessage.BAD_NICKNAME);
                }
            } else {
                message = new ErrorMessage(ErrorMessage.PUBLIC_KEY_IS_OVERDUE);
            }
        } catch (Exception e) {
            Logger.error("RegisterUsecase", e.getMessage());
        }

        return message;
    }

    private boolean validateNickname(String nickname) {
        return nickname != null && !nickname.isEmpty() && nickname.length() >= 3 && nickname.length() <= 20;
    }
}
