package tech.pcloud.framework.utility.common;

import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class NetworkUtils {
  public static enum IP_VERSION {
    V4, V6
  }

  public static List<NetworkInterface> getNetworkInterfaces() {
    List<NetworkInterface> networkInterfaces = new ArrayList<>();
    try {
      Enumeration<NetworkInterface> allNetDevices = NetworkInterface.getNetworkInterfaces();
      while (allNetDevices.hasMoreElements()) {
        NetworkInterface networkInterface = allNetDevices.nextElement();
        if (networkInterface.isUp() && !networkInterface.isLoopback()) {
          networkInterfaces.add(networkInterface);
        }
      }
    } catch (SocketException e) {
      log.error(e.getMessage(), e);
    }
    return networkInterfaces;
  }

  public static NetworkInterface getDefaultNetworkInterface() {
    List<NetworkInterface> networkInterfaces = getNetworkInterfaces();
    if (networkInterfaces.size() > 0) {
      return networkInterfaces.get(0);
    }
    return null;
  }

  public static String getHardwareAddress() {
    StringBuffer mac = new StringBuffer();
    return getHardwareAddress(getDefaultNetworkInterface());
  }

  public static String getHardwareAddress(NetworkInterface networkInterface) {
    if (networkInterface == null) {
      return "";
    }
    StringBuffer mac = new StringBuffer();
    try {
      byte[] macArr = networkInterface.getHardwareAddress();
      for (byte b : macArr) {
        mac.append(Integer.toHexString(b & 0xff)).append("-");
      }
    } catch (SocketException e) {
      log.error(e.getMessage(), e);
    }
    if (mac.length() == 0) {
      return "";
    }
    return mac.substring(0, mac.length() - 1);
  }

  public static String getIP(IP_VERSION version) {
    return getIP(version, getDefaultNetworkInterface());
  }

  public static String getIP(IP_VERSION version, NetworkInterface networkInterface) {
    if (networkInterface == null) {
      return null;
    }
    switch (version) {
      case V4:
        return getIPV4(networkInterface);
      case V6:
        return getIPV6(networkInterface);
      default:
        return getIPV4(networkInterface);
    }
  }


  public static String getIPV4(NetworkInterface networkInterface) {
    if (networkInterface == null) {
      return "";
    }
    Enumeration<InetAddress> ias = networkInterface.getInetAddresses();
    InetAddress ia_v4 = null;
    while (ias.hasMoreElements()) {
      InetAddress ia = ias.nextElement();
      if (ia instanceof Inet4Address) {
        ia_v4 = ia;
        break;
      }
    }
    return ia_v4.getHostAddress();
  }

  public static String getIPV6(NetworkInterface networkInterface) {
    if (networkInterface == null) {
      return "";
    }
    Enumeration<InetAddress> ias = networkInterface.getInetAddresses();
    InetAddress ia_v6 = null;
    while (ias.hasMoreElements()) {
      InetAddress ia = ias.nextElement();
      if (ia instanceof Inet6Address) {
        ia_v6 = ia;
        break;
      }
    }
    return ia_v6.getHostAddress();
  }

  public static String getHostName(){
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      log.error(e.getMessage());
      return "UNKNOWN";
    }
  }
}
