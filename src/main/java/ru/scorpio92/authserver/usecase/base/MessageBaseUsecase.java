package ru.scorpio92.authserver.usecase.base;

import ru.scorpio92.authserver.entity.message.base.BaseMessage;

/**
 * Created by scorpio92 on 1/15/18.
 */

public abstract class MessageBaseUsecase {

    public abstract BaseMessage handleAndReturnResponse(BaseMessage requestMessage);
}
