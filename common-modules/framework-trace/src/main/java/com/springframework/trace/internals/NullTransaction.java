package com.springframework.trace.internals;


import com.springframework.trace.spi.Transaction;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class NullTransaction implements Transaction {
  @Override
  public void setStatus(String status) {
  }

  @Override
  public void setStatus(Throwable e) {
  }

  @Override
  public void addData(String key, Object value) {
  }

  @Override
  public void complete() {
  }
}
