package com.cbox.business.customer.customer.bean;

import com.cbox.base.annotation.Excel;
import com.cbox.base.core.domain.BaseEntity;

public class CustomerVo extends BaseEntity {

	/** 客户姓名 */
	@Excel(name = "客户姓名")
	private String customer_name;

	/** 客户手机号 */
	@Excel(name = "客户手机号")
	private String customer_phone;

	/** 客户身份证 */
	@Excel(name = "客户身份证")
	private String customer_id;

	/** 客户地址 */
	@Excel(name = "客户地址")
	private String customer_addr;

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_phone() {
		return customer_phone;
	}

	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_addr() {
		return customer_addr;
	}

	public void setCustomer_addr(String customer_addr) {
		this.customer_addr = customer_addr;
	}

	public String getHouse_name() {
		return house_name;
	}

	public void setHouse_name(String house_name) {
		this.house_name = house_name;
	}

	/** 房间号 */
	@Excel(name = "房间号")
	private String house_name;
}
