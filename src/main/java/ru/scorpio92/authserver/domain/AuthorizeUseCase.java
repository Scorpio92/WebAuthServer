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
import ru.scorpio92.authserver.data.model.message.request.AuthServerDataRequest;
import ru.scorpio92.authserver.data.model.message.request.RegisterServerDataRequest;
import ru.scorpio92.authserver.data.model.message.response.AuthServerDataResponse;
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
            AuthServerDataRequest authServerDataRequest = JsonWorker.getDeserializeJson(requestMessage.getServerData(), AuthServerDataRequest.class);

            if (!ValidateUtils.validateParam(authServerDataRequest.getLogin(), RegisterServerDataRequest.LOGIN_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Authorize.INCORRECT_LOGIN);

            if (!ValidateUtils.validateParam(authServerDataRequest.getPassword(), RegisterServerDataRequest.PASSWORD_REGEXP))
                throw new ExceptionWithErrorCode(ErrorCode.Authorize.INCORRECT_PASSWORD);

            Integer accountId = new AccountsTable().getAccountId(authServerDataRequest.getLogin(), SHA.getSHA1(authServerDataRequest.getPassword()));

            if (accountId == null)
                throw new ExceptionWithErrorCode(ErrorCode.Authorize.INCORRECT_AUTH_PAIR);

            String authToken = generateAuthToken(authServerDataRequest);
            AuthInfo authInfo = new AuthInfo(accountId, authToken);

            new AuthInfoTable().insertAuthInfo(authInfo);

            response = new SuccessMessage(BaseMessage.Type.AUTHORIZE);
            response.setServerData(JsonWorker.getSerializeJson(new AuthServerDataResponse(authToken)));

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

    private String generateAuthToken(AuthServerDataRequest authServerDataRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return SHA.getSHA512(SHA.getSHA512(authServerDataRequest.getLogin() + authServerDataRequest.getPassword()) + SecRandom.getRandomString());
    }
}
