package ru.scorpio92.authserver.domain;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import ru.scorpio92.authserver.data.db.AccountsTable;
import ru.scorpio92.authserver.data.db.AuthInfoTable;
import ru.scorpio92.authserver.data.model.AuthInfo;
import ru.scorpio92.authserver.data.model.message.base.BaseMessage;
import ru.scorpio92.authserver.data.model.message.base.ErrorCode;
import ru.scorpio92.authserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.authserver.data.model.message.base.SuccessMessage;
import ru.scorpio92.authserver.data.model.message.request.AuthServerData;
import ru.scorpio92.authserver.data.model.message.request.RegisterServerData;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;
import ru.scorpio92.authserver.tools.ValidateUtils;
import ru.scorpio92.authserver.tools.security.SHA;
import ru.scorpio92.authserver.tools.security.SecRandom;

public class AuthorizeUseCase implements UseCase {

    @Override
    public BaseMessage execute(BaseMessage requestMessage) {
        BaseMessage response;

        try {
            AuthServerData authServerData = JsonWorker.getDeserializeJson(requestMessage.getServerData(), AuthServerData.class);

            if (!ValidateUtils.validateParam(authServerData.getLogin(), RegisterServerData.LOGIN_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Authorize.INCORRECT_LOGIN);

            if (!ValidateUtils.validateParam(authServerData.getPassword(), RegisterServerData.PASSWORD_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Authorize.INCORRECT_PASSWORD);

            Integer accountId = new AccountsTable().getAccountId(authServerData.getLogin(), SHA.getSHA1(authServerData.getPassword()));

            if (accountId == null)
                throw new ExceptionWithErrorCode(ErrorCode.Authorize.INCORRECT_AUTH_PAIR);

            AuthInfo authInfo = new AuthInfo(accountId, generateAuthToken(authServerData));

            new AuthInfoTable().insertAuthInfo(authInfo);

            response = new SuccessMessage(BaseMessage.Type.AUTHORIZE);

        } catch (Exception e) {
            Logger.error(e);
            if (e instanceof ExceptionWithErrorCode) {
                response = new ErrorMessage(BaseMessage.Type.AUTHORIZE, ((ExceptionWithErrorCode) e).getErrorCode());
            } else {
                response = new ErrorMessage(BaseMessage.Type.AUTHORIZE, ErrorCode.General.WTF);
            }
        }

        return response;
    }

    private String generateAuthToken(AuthServerData authServerData) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return SHA.getSHA512(SHA.getSHA512(authServerData.getLogin() + authServerData.getPassword()) + SecRandom.getRandomString());
    }
}
