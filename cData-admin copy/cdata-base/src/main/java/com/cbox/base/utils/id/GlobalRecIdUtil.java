package com.cbox.base.utils.id;

/**
 * 生成全局唯一RecId的高并发工厂工具类
 * 
 * @ClassName: GlobalRecIdHelper
 * @Function:
 * 
 * @author cbox
 * @date 2017年10月13日 上午11:49:18
 * @version 1.0
 */
public class GlobalRecIdUtil {
	/** 工作机器ID(0~31) */
	private static long workerId = 0;
	/** 数据中心ID(0~31) */
	private static long datacenterId = 0;

	private static SnowflakeRecIdWorker recIdWorker = null;

	static {
		recIdWorker = new SnowflakeRecIdWorker(workerId, datacenterId);
	}

	/**
	 * 获取rec_id nextRecId:
	 *
	 * @date: 2017年10月13日 下午12:45:11
	 * @author cbox
	 * @return
	 */
	public static String nextRecId() {
		return Long.toString(recIdWorker.nextId());
	}

	public static void setWorkerId(long wId) {
		if (wId > 31 || wId < 0) {
			throw new IllegalArgumentException(String.format("wId Id can't be greater than %d or less than 0", 31));
		}
		workerId = wId;
	}

	public static void setDataCenterId(long dcId) {
		if (dcId > 31 || dcId < 0) {
			throw new IllegalArgumentException(String.format("dcId Id can't be greater than %d or less than 0", 31));
		}
		datacenterId = dcId;
	}
}
