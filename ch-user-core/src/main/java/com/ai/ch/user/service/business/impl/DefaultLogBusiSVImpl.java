package com.ai.ch.user.service.business.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ai.ch.user.api.defaultlog.params.InsertDefaultLogRequest;
import com.ai.ch.user.api.defaultlog.params.QueryDefaultLogRequest;
import com.ai.ch.user.api.defaultlog.params.QueryDefaultLogResponse;
import com.ai.ch.user.api.defaultlog.params.QueryFullDefaultLogRequest;
import com.ai.ch.user.api.defaultlog.params.QueryFullDefaultLogResponse;
import com.ai.ch.user.constants.SequenceCodeConstants.UserSequenceCode;
import com.ai.ch.user.dao.mapper.bo.ShopDefaultLog;
import com.ai.ch.user.dao.mapper.bo.ShopDefaultLogCriteria;
import com.ai.ch.user.service.atom.interfaces.IDefaultLogAtomSV;
import com.ai.ch.user.service.business.interfaces.IDefaultLogBusiSV;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.sdk.components.sequence.util.SeqUtil;

@Component
@Transactional
public class DefaultLogBusiSVImpl implements IDefaultLogBusiSV {

	@Autowired
	private IDefaultLogAtomSV defaultLogAtomSV;
	
	@Override
	public String insertDefaultLog(InsertDefaultLogRequest request) throws SystemException, BusinessException {
		ShopDefaultLog shopDefaultLog = new ShopDefaultLog();
		BeanUtils.copyProperties(request, shopDefaultLog);
		shopDefaultLog.setSerialCode(SeqUtil.getNewId(UserSequenceCode.SHOP_DEFAULT_LOG$SERIAL_CODE_ID$SEQ,17));
		defaultLogAtomSV.insert(shopDefaultLog);
		return shopDefaultLog.getSerialCode();
	}

	@Override
	public QueryDefaultLogResponse queryDefaultLog(QueryDefaultLogRequest request)
			throws SystemException, BusinessException {
		return defaultLogAtomSV.queryDefaultLog(request);
	}

	@Override
	public QueryFullDefaultLogResponse queryFullDefaultLog(QueryFullDefaultLogRequest request)
			throws SystemException, BusinessException {
		return defaultLogAtomSV.selectPageDefaultLog(request);
	}
	
	@Override
	public int deleteDefaultLog(String serialCode) throws SystemException,
			BusinessException {
		ShopDefaultLogCriteria example = new ShopDefaultLogCriteria();
		ShopDefaultLogCriteria.Criteria criteria = example.createCriteria();
		criteria.andSerialCodeEqualTo(serialCode);
		int count =defaultLogAtomSV.deleteDefaultLog(example);
		return count;
	}

}
