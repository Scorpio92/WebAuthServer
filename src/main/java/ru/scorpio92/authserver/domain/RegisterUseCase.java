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
import ru.scorpio92.authserver.tools.ValidateUtils;
import ru.scorpio92.authserver.tools.security.SHA;

public class RegisterUseCase implements UseCase {

    @Override
    public BaseMessage execute(BaseMessage requestMessage) {
        BaseMessage response;

        try {
            RegisterServerData registerServerData = JsonWorker.getDeserializeJson(requestMessage.getServerData(), RegisterServerData.class);

            if (!ValidateUtils.validateParam(registerServerData.getLogin(), RegisterServerData.LOGIN_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Register.INCORRECT_LOGIN);

            if (!ValidateUtils.validateParam(registerServerData.getPassword(), RegisterServerData.PASSWORD_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Register.INCORRECT_PASSWORD);

            if (!ValidateUtils.validateParam(registerServerData.getNickname(), RegisterServerData.NICKNAME_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Register.INCORRECT_NICKNAME);

            AccountsTable accountsTable = new AccountsTable();

            if (accountsTable.checkLoginExists(registerServerData.getLogin())) {
                throw new ExceptionWithErrorCode(ErrorCode.Register.LOGIN_ALREADY_EXISTS);
            }

            if (accountsTable.checkNicknameExists(registerServerData.getNickname())) {
                throw new ExceptionWithErrorCode(ErrorCode.Register.NICKNAME_ALREADY_EXISTS);
            }

            Account account = new Account(
                    registerServerData.getLogin(),
                    SHA.getSHA1(registerServerData.getPassword()),
                    (registerServerData.getNickname() != null) ? registerServerData.getNickname() : registerServerData.getLogin()
            );

            accountsTable.insertAccount(account);

            response = new SuccessMessage(BaseMessage.Type.REGISTER);

        } catch (Exception e) {
            Logger.error(e);
            if (e instanceof ExceptionWithErrorCode) {
                response = new ErrorMessage(BaseMessage.Type.REGISTER, ((ExceptionWithErrorCode) e).getErrorCode());
            } else {
                response = new ErrorMessage(BaseMessage.Type.REGISTER, ErrorCode.General.WTF);
            }
        }

        return response;
    }
}
