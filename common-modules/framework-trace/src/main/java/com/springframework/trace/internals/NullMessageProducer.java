package com.springframework.trace.internals;


import com.springframework.trace.spi.MessageProducer;
import com.springframework.trace.spi.Transaction;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class NullMessageProducer implements MessageProducer {
  private static final Transaction NULL_TRANSACTION = new NullTransaction();

  @Override
  public void logError(Throwable cause) {
  }

  @Override
  public void logError(String message, Throwable cause) {
  }

  @Override
  public void logEvent(String type, String name) {
  }

  @Override
  public void logEvent(String type, String name, String status, String nameValuePairs) {
  }

  @Override
  public Transaction newTransaction(String type, String name) {
    return NULL_TRANSACTION;
  }
}
