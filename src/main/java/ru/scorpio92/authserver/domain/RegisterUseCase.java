package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.data.db.AccountsTable;
import ru.scorpio92.authserver.data.model.Account;
import ru.scorpio92.authserver.data.model.message.base.BaseMessage;
import ru.scorpio92.authserver.data.model.message.base.ErrorCode;
import ru.scorpio92.authserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.authserver.data.model.message.base.SuccessMessage;
import ru.scorpio92.authserver.data.model.message.request.RegisterServerDataRequest;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.tools.ValidateUtils;
import ru.scorpio92.authserver.tools.security.SHA;

public class RegisterUseCase implements UseCase {

    @Override
    public BaseMessage execute(BaseMessage requestMessage) {
        BaseMessage response;

        try {
            RegisterServerDataRequest registerServerDataRequest = JsonWorker.getDeserializeJson(requestMessage.getServerData(), RegisterServerDataRequest.class);

            if (!ValidateUtils.validateParam(registerServerDataRequest.getLogin(), RegisterServerDataRequest.LOGIN_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Register.INCORRECT_LOGIN);

            if (!ValidateUtils.validateParam(registerServerDataRequest.getPassword(), RegisterServerDataRequest.PASSWORD_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Register.INCORRECT_PASSWORD);

            if (!ValidateUtils.validateParam(registerServerDataRequest.getNickname(), RegisterServerDataRequest.NICKNAME_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Register.INCORRECT_NICKNAME);

            AccountsTable accountsTable = new AccountsTable();

            if (accountsTable.checkLoginExists(registerServerDataRequest.getLogin())) {
                throw new ExceptionWithErrorCode(ErrorCode.Register.LOGIN_ALREADY_EXISTS);
            }

            if (accountsTable.checkNicknameExists(registerServerDataRequest.getNickname())) {
                throw new ExceptionWithErrorCode(ErrorCode.Register.NICKNAME_ALREADY_EXISTS);
            }

            Account account = new Account(
                    registerServerDataRequest.getLogin(),
                    SHA.getSHA1(registerServerDataRequest.getPassword()),
                    (registerServerDataRequest.getNickname() != null) ? registerServerDataRequest.getNickname() : registerServerDataRequest.getLogin()
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
