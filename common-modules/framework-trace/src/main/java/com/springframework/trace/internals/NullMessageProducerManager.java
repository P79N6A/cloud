package com.springframework.trace.internals;


import com.springframework.trace.spi.MessageProducer;
import com.springframework.trace.spi.MessageProducerManager;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class NullMessageProducerManager implements MessageProducerManager {
  private static final MessageProducer producer = new NullMessageProducer();

  @Override
  public MessageProducer getProducer() {
    return producer;
  }
}
