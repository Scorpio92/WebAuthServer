package ru.scorpio92.authserver.crypto;

import java.util.HashMap;

import ru.scorpio92.authserver.entity.ServerKeyPair;

/**
 * Created by scorpio92 on 1/21/18.
 */

public class KeyStorage {

    /**
     * время жизни пары ключей
     */
    private static final long PAIR_LIFE_TIME = 1 * 30 * 1000; //30 сек

    private static volatile HashMap<String, ServerKeyPair> keys;

    public static void init() {
        if(keys == null)
            keys = new HashMap<>();
    }

    public static synchronized ServerKeyPair getNewPair() throws Exception {
        //перед генерацией новой пары, проверяем наличие протухших пар
        checkOverduePairs();

        ServerKeyPair serverKeyPair = ServerKeyPair.build();
        keys.put(serverKeyPair.getPairId(), serverKeyPair);
        return serverKeyPair;
    }

    public static synchronized ServerKeyPair getKeyPairById(String pairId) {
        //перед получением пары, проверяем наличие протухших пар
        checkOverduePairs();

        return keys.get(pairId);
    }

    private static void removeKeyPair(String pairId) {
        keys.remove(pairId);
    }

    private static void checkOverduePairs() {
        for (ServerKeyPair serverKeyPair : keys.values()) {
            if(System.currentTimeMillis() - serverKeyPair.getCreateTime() > PAIR_LIFE_TIME)
                removeKeyPair(serverKeyPair.getPairId());
        }
    }
}
