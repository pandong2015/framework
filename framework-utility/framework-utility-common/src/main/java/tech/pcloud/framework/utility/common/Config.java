package tech.pcloud.framework.utility.common;

import java.util.ResourceBundle;

public class Config {
  private String name;
  private ResourceBundle resource;

  public Config(String name) {
    this.name = name;
    resource = ResourceBundle.getBundle(name);
  }

  public String getName() {
    return name;
  }

  public String stringValue(String name){
    return resource.getString(name);
  }

  public int intValue(String name, int defaultValue){
    String value = stringValue(name);
    if(StringUtils.isNull(value)){
      return defaultValue;
    }
    return Integer.valueOf(value);
  }

  public long longValue(String name, long defaultValue){
    String value = stringValue(name);
    if(StringUtils.isNull(value)){
      return defaultValue;
    }
    return Long.valueOf(value);
  }

  public float floatValue(String name, float defaultValue){
    String value = stringValue(name);
    if(StringUtils.isNull(value)){
      return defaultValue;
    }
    return Float.valueOf(value);
  }

  public double doubleValue(String name, double defaultValue){
    String value = stringValue(name);
    if(StringUtils.isNull(value)){
      return defaultValue;
    }
    return Double.valueOf(value);
  }

  public boolean booleanValue(String name, boolean defaultValue){
    String value = stringValue(name);
    if(StringUtils.isNull(value)){
      return defaultValue;
    }
    return Boolean.valueOf(value);
  }
}
