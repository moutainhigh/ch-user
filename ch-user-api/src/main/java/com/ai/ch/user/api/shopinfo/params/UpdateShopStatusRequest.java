package com.ai.ch.user.api.shopinfo.params;

import com.ai.opt.base.vo.BaseInfo;

/**
 * 店铺状态修改入参
 * Date: 2016年9月28日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 * 
 * @author zhangqiang7
 */
public class UpdateShopStatusRequest extends BaseInfo {

	private static final long serialVersionUID = 1L;

	/**
	 * 店铺id
	 */
	private String userId;
	
	/**
	 * 店铺状态
	 */
	private Integer status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
