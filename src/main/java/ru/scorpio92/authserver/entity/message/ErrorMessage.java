package ru.scorpio92.authserver.entity.message;

import ru.scorpio92.authserver.entity.message.base.BaseMessage;

import static ru.scorpio92.authserver.entity.message.base.BaseMessage.Status.ERROR;

/**
 * Created by scorpio92 on 1/13/18.
 */

public class ErrorMessage extends BaseMessage {

    //клиент (его ip) заблокирован за слишком частую отправку запросов
    public final static int CLIENT_IP_IS_BLOCKED = -1;

    //публичный ключ сервера просрочен, требуется получить новый
    public final static int PUBLIC_KEY_IS_OVERDUE = -2;

    //неизвеcтная ошибка
    public final static int WTF = -999;

    //GetServerKey
    public final static int GET_SERVER_KEY_IS_CLOSED = 1;

    //Reqister
    //регистрация временно недоступна
    public final static int REGISTER_IS_CLOSED = 1;
    public final static int BAD_NICKNAME = 2;
    public final static int NICKNAME_EXISTS = 3;

    //Auth
    //авторизация временно недоступна
    public final static int AUTH_IS_CLOSED = 1;
    public final static int BAD_TOKEN = 2;

    public ErrorMessage(int errorCode) {
        this.Status = ERROR.name();
        this.ErrorCode = String.valueOf(errorCode);
    }
}
