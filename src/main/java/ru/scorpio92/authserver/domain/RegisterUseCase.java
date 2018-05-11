package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.data.db.AccountsTable;
import ru.scorpio92.authserver.data.model.Account;
import ru.scorpio92.authserver.data.model.message.base.BaseMessage;
import ru.scorpio92.authserver.data.model.message.base.ErrorCode;
import ru.scorpio92.authserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.authserver.data.model.message.base.SuccessMessage;
import ru.scorpio92.authserver.data.model.message.request.RegisterServerData;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.tools.security.SHA;

public class RegisterUseCase implements UseCase {

    @Override
    public BaseMessage execute(BaseMessage requestMessage) {
        BaseMessage response;
        try {
            RegisterServerData registerServerData = JsonWorker.getDeserializeJson(requestMessage.getServerData(), RegisterServerData.class);

            Account account = new Account(
                    registerServerData.getLogin(),
                    SHA.getSHA1(registerServerData.getPassword()),
                    registerServerData.getNickname()
            );

            AccountsTable accountsTable = new AccountsTable();

            if (accountsTable.checkAccountExists(account.getLogin(), account.getPasswordHash())) {
                response = new ErrorMessage(BaseMessage.Type.REGISTER, ErrorCode.Register.LOGIN_ALREADY_EXISTS);
            } else {
                accountsTable.insertAccount(account);
                response = new SuccessMessage(BaseMessage.Type.REGISTER);
            }
        } catch (Exception e) {
            Logger.error(e);
            response = new ErrorMessage(BaseMessage.Type.REGISTER, ErrorCode.General.WTF);
        }
        return response;
    }
}
