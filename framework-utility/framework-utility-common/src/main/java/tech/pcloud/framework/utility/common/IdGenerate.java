package tech.pcloud.framework.utility.common;


import java.util.Calendar;

/**
 * 64位ID (42(毫秒)+10(机器码)+12(重复累加))
 */
public class IdGenerate {
  //机器码起始位置
  private static final int OFFSET_MACHINE = 12;
  //时间戳起始位置
  private static final int OFFSET_TIMESTAMP = 22;

  //机器标识，通过网卡的MAC生成
  private static final long MACHINE_ID = generateMachineId(NetworkUtils.getHardwareAddress());

  /**
   * 基于业务ID生成唯一ID
   *
   * @param id 业务ID
   * @return 唯一ID
   */
  public static long generate(long id) {
    return generate(id, MACHINE_ID);
  }

  /**
   * 基于业务ID和机器标识生成唯一ID
   *
   * @param id          业务ID
   * @param machineName 机器码
   * @return 唯一ID
   */
  public static long generate(long id, String machineName) {
    long machineId = generateMachineId(machineName);
    return generate(id, machineId);
  }

  /**
   * 基于业务ID和机器标识生成唯一ID
   *
   * @param id        业务ID
   * @param machineId 机器码ID
   * @return 唯一ID
   */
  public static long generate(long id, long machineId) {
    long time = (System.currentTimeMillis() / 1000) << OFFSET_TIMESTAMP;
    long result = (time | machineId | (id & 0XFFF));
//    System.out.println(time + " - " + machineId + " - " + (id & 0XFFF) + " --> "+result);
    return result;
  }

  public static long generateMachineId(String machine) {
    return (HashUtil.hashByMD5(machine) & 0X3FF) << OFFSET_MACHINE;
  }

  public static void main(String[] args) {
    /*AtomicLong sequence = new AtomicLong();
    for (int i = 0; i < 10; i++) {
      generate(sequence.getAndIncrement());
    }*/
    Calendar c = Calendar.getInstance();
    System.out.println(c.get(Calendar.SECOND));
  }
}
