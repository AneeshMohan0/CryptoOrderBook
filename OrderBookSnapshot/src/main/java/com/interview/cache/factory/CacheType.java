package com.interview.cache.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheType <T, R> {

  public Map<T, R> getNewMap() {
    return new ConcurrentHashMap<T, R>();
  }
}