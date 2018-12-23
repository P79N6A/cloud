package com.springframework.trace.internals;

import com.springframework.trace.internals.cat.CatMessageProducer;
import com.springframework.trace.internals.cat.CatNames;
import com.springframework.trace.spi.MessageProducer;
import com.springframework.trace.spi.MessageProducerManager;
import com.springframework.trace.util.ClassLoaderUtil;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultMessageProducerManager implements MessageProducerManager {
    private static MessageProducer producer;

    public DefaultMessageProducerManager() {
        if (ClassLoaderUtil.isClassPresent(CatNames.CAT_CLASS)) {
            producer = new CatMessageProducer();
        } else {
            producer = new NullMessageProducerManager().getProducer();
        }
    }

    @Override
    public MessageProducer getProducer() {
        return producer;
    }
}
