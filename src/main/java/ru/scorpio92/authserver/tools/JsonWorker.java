package ru.scorpio92.authserver.tools;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonWorker {

    /**
     * <p>Сериализует исходный объект в json-строку</p>
     *
     * @param obj объект для сериализации
     * @return возвращает json-строку
     */
    public static String getSerializeJson(Object obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    /**
     * <p>Десериализует исходную json-строку в указываемый объект</p>
     *
     * @param jsonString исходная json-строка
     * @param classOfT   класс в который нужно преобразовать, например MyClass.class
     * @return возвращает объект указанного класса, построенный на основе данных из исходной json-строки
     */
    public static <T> T getDeserializeJson(String jsonString, Class<T> classOfT) throws IllegalStateException {
        return new Gson().fromJson(jsonString, classOfT);
    }
}