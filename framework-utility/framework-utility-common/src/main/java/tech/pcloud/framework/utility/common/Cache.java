package tech.pcloud.framework.utility.common;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cache {
  private Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
  private String name;

  public Cache(String name) {
    this.name = name;
  }

  public void save(String name, Object obj) {
    if (StringUtils.isNull(name) || obj == null) {
      throw new NullPointerException("name or value is null");
    }
    cache.put(name, obj);
  }

  public Object get(String name) {
    if (StringUtils.isNull(name)) {
      throw new NullPointerException("name is null");
    }
    return cache.get(name);
  }

  public <X, T> Map<X, T> map(String name) {
    Map<X, T> map = (Map<X, T>) get(name);
    if (map == null) {
      map = new ConcurrentHashMap();
      save(name, map);
    }
    return map;
  }

  public <T> List<T> list(String name){
    List<T> list = (List<T>)get(name);
    if(list == null){
      list = new CopyOnWriteArrayList();
      save(name, list);
    }
    return list;
  }
}
