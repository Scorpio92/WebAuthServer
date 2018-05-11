package ru.scorpio92.authserver.domain;

import ru.scorpio92.authserver.model.base.BaseMessage;

public interface UseCase {

    BaseMessage execute(BaseMessage requestMessage);
}
