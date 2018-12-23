package com.springframework.trace.foundation.spi;


import com.springframework.trace.foundation.spi.provider.Provider;

public interface ProviderManager {
  public String getProperty(String name, String defaultValue);

  public <T extends Provider> T provider(Class<T> clazz);
}
