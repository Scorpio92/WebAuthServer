package ru.scorpio92.authserver.model.base;

public class ErrorCode {

    /**
     * Общие коды ошибок
     */
    public static class General {
        //клиент (его ip) заблокирован за слишком частую отправку запросов
        public final static int CLIENT_IP_IS_BLOCKED = -1;

        //публичный ключ сервера просрочен, требуется получить новый
        public final static int PUBLIC_KEY_IS_OVERDUE = -2;

        //неизвеcтная ошибка
        public final static int WTF = -999;
    }

    /**
     * Регистрация
     */
    public static class Register {
        //логин не соответствует формату
        public final static int INCORRECT_LOGIN = 1;

        //логин уже есть
        public final static int LOGIN_ALREADY_EXISTS = 2;

        //пароль не соответствует формату
        public final static int INCORRECT_PASSWORD = 3;
    }

    /**
     * Авторизация
     */
    public static class Authorize {
        //логин не соответствует формату
        public final static int INCORRECT_LOGIN = 1;

        //пароль не соответствует формату
        public final static int INCORRECT_PASSWORD = 2;

        //клиентом предоставлена неверная пара логин/пароль
        public final static int INCORRECT_AUTH_PAIR = 3;
    }

    /**
     * Деавторизация
     */
    public static class Deauthorize {
        //клиентом предоставлен некорректный токен
        public final static int INCORRECT_AUTH_TOKEN = 1;
    }

    /**
     * Функция возвращающая описание ошибки по коду ошибки
     *
     * @param errorCode
     * @return
     */
    public static String getMessageForErrorCode(int errorCode) {
        return "OOOPS!";
    }
}
