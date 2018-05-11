package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.data.model.message.base.BaseMessage;

public interface UseCase {

    BaseMessage execute(BaseMessage requestMessage);
}
