package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.data.db.AuthInfoTable;
import ru.scorpio92.authserver.data.model.message.base.BaseMessage;
import ru.scorpio92.authserver.data.model.message.base.ErrorCode;
import ru.scorpio92.authserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.authserver.data.model.message.base.SuccessMessage;
import ru.scorpio92.authserver.data.model.message.request.DeauthServerData;
import ru.scorpio92.authserver.tools.JsonWorker;
import ru.scorpio92.authserver.tools.Logger;

public class DeauthorizeUseCase implements UseCase {

    @Override
    public BaseMessage execute(BaseMessage requestMessage) {
        BaseMessage response;
        try {
            DeauthServerData deauthServerData = JsonWorker.getDeserializeJson(requestMessage.getServerData(), DeauthServerData.class);

            AuthInfoTable authInfoTable = new AuthInfoTable();

            if (!authInfoTable.checkAuthTokenExists(deauthServerData.getAuthToken()))
                throw new ExceptionWithErrorCode(ErrorCode.Deauthorize.INCORRECT_AUTH_TOKEN);

            authInfoTable.deleteAuthInfo(deauthServerData.getAuthToken());

            response = new SuccessMessage(BaseMessage.Type.DEAUTHORIZE);

        } catch (Exception e) {
            Logger.error(e);
            if (e instanceof ExceptionWithErrorCode) {
                response = new ErrorMessage(BaseMessage.Type.DEAUTHORIZE, ((ExceptionWithErrorCode) e).getErrorCode());
            } else {
                response = new ErrorMessage(BaseMessage.Type.DEAUTHORIZE, ErrorCode.General.WTF);
            }
        }

        return response;
    }
}
