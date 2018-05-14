package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.data.db.AccountsTable;
import ru.scorpio92.authserver.data.db.AuthInfoTable;
import ru.scorpio92.authserver.data.model.Account;
import ru.scorpio92.authserver.data.model.message.base.BaseMessage;
import ru.scorpio92.authserver.data.model.message.base.ErrorCode;
import ru.scorpio92.authserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.authserver.data.model.message.base.SuccessMessage;
import ru.scorpio92.authserver.data.model.message.request.CheckTokenServerDataRequest;
import ru.scorpio92.authserver.data.model.message.response.CheckTokenServerDataResponse;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;

public class CheckTokenUseCase implements UseCase {

    @Override
    public BaseMessage execute(BaseMessage requestMessage) {
        BaseMessage response;
        String authToken = null;

        try {
            CheckTokenServerDataRequest checkTokenServerDataRequest = JsonWorker.getDeserializeJson(requestMessage.getServerData(), CheckTokenServerDataRequest.class);
            authToken = checkTokenServerDataRequest.getAuthToken();

            AuthInfoTable authInfoTable = new AuthInfoTable();

            if(!authInfoTable.checkAuthTokenExists(authToken))
                throw new ExceptionWithErrorCode(ErrorCode.CheckToken.INVALID_TOKEN);

            int accountId = authInfoTable.getAccountIdByToken(authToken);

            Account account = new AccountsTable().getAccountByAccountId(accountId);

            response = new SuccessMessage(BaseMessage.Type.CHECK_TOKEN);
            response.setServerData(JsonWorker.getSerializeJson(new CheckTokenServerDataResponse(
                    authToken,
                    String.valueOf(accountId),
                    account.getLogin(),
                    account.getNickname()
            )));
        } catch (Exception e) {
            Logger.error(e);
            if (e instanceof ExceptionWithErrorCode) {
                response = new ErrorMessage(BaseMessage.Type.CHECK_TOKEN, ((ExceptionWithErrorCode) e).getErrorCode());
            } else {
                response = new ErrorMessage(BaseMessage.Type.CHECK_TOKEN, ErrorCode.General.WTF);
            }
            response.setServerData(JsonWorker.getSerializeJson(new CheckTokenServerDataResponse(authToken)));
        }

        return response;
    }
}
