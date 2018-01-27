package ru.scorpio92.authserver.entity.message.base;

import ru.scorpio92.authserver.tools.JsonWorker;

/**
 * Created by scorpio92 on 1/13/18.
 */

public abstract class Payload {

    @Override
    public String toString() {
        return JsonWorker.getSerializeJson(this);
    }
}
