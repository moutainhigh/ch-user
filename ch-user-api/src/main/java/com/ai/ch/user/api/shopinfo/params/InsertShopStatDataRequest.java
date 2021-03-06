package com.ai.ch.user.api.shopinfo.params;

import java.io.Serializable;

/**
 * 店铺评级统计数据 Date: 2016年9月5日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 * 
 * @author zhangqiang7
 */
public class InsertShopStatDataRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 店铺Id
	 */
	private String userId;

	/**
	 * 店铺名称
	 */
	private String userName;

	/**
	 * 订单金额
	 */
	private Long orderAmount;

	/**
	 * 订单数量
	 */
	private Long orderCount;

	/**
	 * 佣金
	 */
	private Long servCharge;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Long getServCharge() {
		return servCharge;
	}

	public void setServCharge(Long servCharge) {
		this.servCharge = servCharge;
	}

}
