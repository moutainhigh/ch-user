package com.ai.ch.user.service.business.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ai.ch.user.api.shopinfo.params.InsertShopInfoRequst;
import com.ai.ch.user.api.shopinfo.params.InsertShopStatDataRequest;
import com.ai.ch.user.api.shopinfo.params.QueryDepositRuleRequest;
import com.ai.ch.user.api.shopinfo.params.QueryDepositRuleResponse;
import com.ai.ch.user.api.shopinfo.params.QueryShopDepositRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopInfoBatchRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopInfoBatchResponse;
import com.ai.ch.user.api.shopinfo.params.QueryShopInfoByIdRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopInfoRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopInfoResponse;
import com.ai.ch.user.api.shopinfo.params.QueryShopRankRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopScoreKpiRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopScoreKpiResponse;
import com.ai.ch.user.api.shopinfo.params.QueryShopStatDataRequest;
import com.ai.ch.user.api.shopinfo.params.QueryShopStatDataResponse;
import com.ai.ch.user.api.shopinfo.params.SaveShopAuditInfoRequest;
import com.ai.ch.user.api.shopinfo.params.ShopInfoVo;
import com.ai.ch.user.api.shopinfo.params.ShopScoreKpiVo;
import com.ai.ch.user.api.shopinfo.params.UpdateShopInfoRequest;
import com.ai.ch.user.api.shopinfo.params.UpdateShopStatDataRequest;
import com.ai.ch.user.api.shopinfo.params.UpdateShopStatusRequest;
import com.ai.ch.user.dao.mapper.bo.CtDepositRule;
import com.ai.ch.user.dao.mapper.bo.CtDepositRuleCriteria;
import com.ai.ch.user.dao.mapper.bo.ShopInfo;
import com.ai.ch.user.dao.mapper.bo.ShopInfoCriteria;
import com.ai.ch.user.dao.mapper.bo.ShopInfoLog;
import com.ai.ch.user.dao.mapper.bo.ShopInfoLogCriteria;
import com.ai.ch.user.dao.mapper.bo.ShopRankRule;
import com.ai.ch.user.dao.mapper.bo.ShopRankRuleCriteria;
import com.ai.ch.user.dao.mapper.bo.ShopScoreKpi;
import com.ai.ch.user.dao.mapper.bo.ShopScoreKpiCriteria;
import com.ai.ch.user.dao.mapper.bo.ShopStatData;
import com.ai.ch.user.dao.mapper.bo.ShopStatDataCriteria;
import com.ai.ch.user.service.atom.interfaces.IDepositRuleAtomSV;
import com.ai.ch.user.service.atom.interfaces.IShopInfoAtomSV;
import com.ai.ch.user.service.atom.interfaces.IShopInfoLogAtomSV;
import com.ai.ch.user.service.atom.interfaces.IShopRankRuleAtomSV;
import com.ai.ch.user.service.atom.interfaces.IShopScoreKpiAtomSV;
import com.ai.ch.user.service.atom.interfaces.IShopStatDataAtomSV;
import com.ai.ch.user.service.business.interfaces.IShopInfoBusiSV;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.base.vo.BaseResponse;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.constants.ExceptCodeConstants;
import com.ai.opt.sdk.util.CollectionUtil;
import com.ai.opt.sdk.util.DateUtil;
import com.ai.opt.sdk.util.StringUtil;

@Component
@Transactional
public class ShopInfoBusiSVImpl implements IShopInfoBusiSV {

	//电商平台位置
	//static private String[] shopOwner = {"京东","天猫","淘宝","苏宁","一号店","自有电商平台"};
	
	@Autowired
	private IShopInfoAtomSV shopInfoAtomSV;

	@Autowired
	private IDepositRuleAtomSV depositRuleAtomSV;

	@Autowired
	private IShopScoreKpiAtomSV shopScoreKpiAtomSV;

	@Autowired
	private IShopStatDataAtomSV shopStatDataAtomSV;

	@Autowired
	private IShopRankRuleAtomSV shopRankRuleAtomSV;
	
	@Autowired
	private IShopInfoLogAtomSV shopInfoLogAtomSV;

	@Override
	public QueryShopInfoResponse queryShopInfo(QueryShopInfoRequest request) throws BusinessException, SystemException {
		QueryShopInfoResponse response = new QueryShopInfoResponse();
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		if (request.getUserId() != null && !"".equals(request.getUserId())) {
			criteria.andUserIdEqualTo(request.getUserId());
		}
		if (request.getShopName() != null && !"".equals(request.getShopName())) {
			criteria.andShopNameEqualTo(request.getShopName());
		}
		List<ShopInfo> list = shopInfoAtomSV.selectByExample(example);
		if (!list.isEmpty())
			BeanUtils.copyProperties(list.get(0), response);
		String ecommOwner="";
		/*if(response.getEcommOwner()!=null)
			for (int index=0;index<response.getEcommOwner().length();index++) {
				if('1'==response.getEcommOwner().charAt(index))
					ecommOwner+=shopOwner[index]+"/";
			}
		ecommOwner = ecommOwner.substring(0,ecommOwner.length()-1);*/
		response.setEcommOwner(ecommOwner);
		return response;
	}

	@Override
	public int insertShopInfo(InsertShopInfoRequst request) throws BusinessException, SystemException {
		ShopInfo shopInfo = new ShopInfo();
		BeanUtils.copyProperties(request, shopInfo);
		shopInfo.setCreateTime(DateUtil.getSysDate());
		shopInfo.setStatus(0);
		//插入日志表
		ShopInfoLog shopInfoLog = new ShopInfoLog();
		BeanUtils.copyProperties(request, shopInfoLog);
		shopInfoLog.setOperId(request.getOperId());
		shopInfoLog.setOperName(request.getOperName());
		shopInfo.setCreateTime(DateUtil.getSysDate());
		shopInfoLogAtomSV.insert(shopInfoLog);
		return shopInfoAtomSV.insert(shopInfo);
	}

	@Override
	public int updateShopInfo(UpdateShopInfoRequest request) throws BusinessException, SystemException {
		ShopInfo shopInfo = new ShopInfo();
		BeanUtils.copyProperties(request, shopInfo);
		if (request.getStatus() != null) {
			if ("1".equals(request.getStatus())) {
				shopInfo.setCloseTime(DateUtil.getSysDate());
			} else if ("2".equals(request.getStatus())) {
				shopInfo.setCloseTime(DateUtil.getSysDate());
			} else if ("0".equals(request.getStatus())) {
				shopInfo.setStatus(request.getStatus());
				shopInfo.setOpenTime(DateUtil.getSysDate());
			} else
				shopInfo.setStatus(null);
		}
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andUserIdEqualTo(request.getUserId());
		
		//更新日志表
		ShopInfoLog shopInfoLog = new ShopInfoLog();
		BeanUtils.copyProperties(shopInfo, shopInfoLog);
		shopInfoLog.setUpdateTime(DateUtil.getSysDate());
		return shopInfoAtomSV.updateByExampleSelective(shopInfo, example);
	}

	@Override
	public QueryDepositRuleResponse queryDepositRule(QueryDepositRuleRequest request)
			throws BusinessException, SystemException {
		CtDepositRuleCriteria example = new CtDepositRuleCriteria();
		CtDepositRuleCriteria.Criteria criteria = example.createCriteria();
		criteria.andProductCatIdEqualTo(request.getProductCatId());
		List<CtDepositRule> list = depositRuleAtomSV.selectByExample(example);
		QueryDepositRuleResponse response = new QueryDepositRuleResponse();
		if (!list.isEmpty()) {
			BeanUtils.copyProperties(list.get(0), response);
		}
		return response;
	}

	@Override
	public QueryShopScoreKpiResponse queryShopScoreKpi(QueryShopScoreKpiRequest request)
			throws BusinessException, SystemException {
		QueryShopScoreKpiResponse response = new QueryShopScoreKpiResponse();
		ShopScoreKpiCriteria example = new ShopScoreKpiCriteria();
		ShopScoreKpiCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		List<ShopScoreKpi> list = shopScoreKpiAtomSV.selectByExample(example);
		List<ShopScoreKpiVo> responseList = new ArrayList<ShopScoreKpiVo>();
		for (ShopScoreKpi shopScoreKpi : list) {
			ShopScoreKpiVo shopScoreKpiVo = new ShopScoreKpiVo();
			BeanUtils.copyProperties(shopScoreKpi, shopScoreKpiVo);
			responseList.add(shopScoreKpiVo);
		}
		response.setList(responseList);
		return response;
	}

	@Override
	public int updateShopStatData(UpdateShopStatDataRequest request) throws BusinessException, SystemException {
		ShopStatDataCriteria example = new ShopStatDataCriteria();
		ShopStatDataCriteria.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(request.getUserId());
		ShopStatData shopStatData = new ShopStatData();
		BeanUtils.copyProperties(request, shopStatData);
		return shopStatDataAtomSV.updateByExampleSelective(shopStatData, example);
	}

	@Override
	public int insertShopStatData(InsertShopStatDataRequest request) throws BusinessException, SystemException {
		ShopStatData record = new ShopStatData();
		BeanUtils.copyProperties(request, record);
		return shopStatDataAtomSV.insert(record);
	}

	@Override
	public QueryShopStatDataResponse queryShopStatData(QueryShopStatDataRequest request)
			throws BusinessException, SystemException {
		QueryShopStatDataResponse response = new QueryShopStatDataResponse();
		ShopStatDataCriteria example = new ShopStatDataCriteria();
		ShopStatDataCriteria.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(request.getUserId());
		List<ShopStatData> list = shopStatDataAtomSV.selectByExample(example);
		if (!list.isEmpty())
			BeanUtils.copyProperties(list.get(0), response);
		return response;
	}

	@Override
	public Integer queryShopRank(QueryShopRankRequest request) throws BusinessException, SystemException {
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andUserIdEqualTo(request.getUserId());

		// 店铺评级统计数据
		ShopStatDataCriteria shopStatDataExample = new ShopStatDataCriteria();
		ShopStatDataCriteria.Criteria shopStatDataCriteria = shopStatDataExample.createCriteria();
		shopStatDataCriteria.andUserIdEqualTo(request.getUserId());
		List<ShopStatData> shopStatDataList = shopStatDataAtomSV.selectByExample(shopStatDataExample);
		Long servCharge = 0L;
		// 获取佣金
		if (shopStatDataList.isEmpty())
			throw new BusinessException("统计数据不存在");
		else
			servCharge = shopStatDataList.get(0).getServCharge();
		// 店铺评级指标
		BigDecimal h = new BigDecimal("0");
		BigDecimal a = new BigDecimal("0");
		ShopScoreKpiCriteria shopScoreKpiExample = new ShopScoreKpiCriteria();
		ShopScoreKpiCriteria.Criteria shopScoreKpiCriteria = shopScoreKpiExample.createCriteria();
		shopScoreKpiCriteria.andTenantIdEqualTo(request.getTenantId());
		List<String> strList = new ArrayList<String>();
		strList.add("h");
		strList.add("a");
		shopScoreKpiCriteria.andKpiNameIn(strList);
		List<ShopScoreKpi> shopScoreList = shopScoreKpiAtomSV.selectByExample(shopScoreKpiExample);
		for (ShopScoreKpi shopScoreKpi : shopScoreList) {
			if ("h".equals(shopScoreKpi.getKpiName()))
				h = shopScoreKpi.getWeight();
			else if ("a".equals(shopScoreKpi.getKpiName()))
				a = shopScoreKpi.getWeight();
		}
		// 计算分数公式 score=h(a*佣金+b*订单+.....)
		BigDecimal score = h.multiply((a.multiply(BigDecimal.valueOf(servCharge))));

		Integer rank = 0;
		// 查询score在平台评级规则表中的rank
		ShopRankRuleCriteria shopRankRuleExample = new ShopRankRuleCriteria();
		ShopRankRuleCriteria.Criteria shopRankRuleCriteria = shopRankRuleExample.createCriteria();
		shopRankRuleCriteria.andTenantIdEqualTo(request.getTenantId());
		shopRankRuleCriteria.andMinScoreLessThan(score.longValue());
		shopRankRuleCriteria.andMaxScoreGreaterThan(score.longValue());
		List<ShopRankRule> shopRankRuleList = shopRankRuleAtomSV.selectByExample(shopRankRuleExample);
		if (shopRankRuleList.isEmpty())
			throw new BusinessException("评分不在规则之内");
		else
			rank = shopRankRuleList.get(0).getRank();
		return rank;
	}

	@Override
	public Long queryShopDeposit(QueryShopDepositRequest request) throws BusinessException, SystemException {
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andUserIdEqualTo(request.getUserId());
		List<ShopInfo> list = shopInfoAtomSV.selectByExample(example);
		Long deposit = 0L;
		if (list.isEmpty())
			throw new BusinessException("店铺信息不存在");
		else {
			if (list.get(0).getDepositBalance() != null)
				deposit = list.get(0).getDepositBalance();
			else {
				/*
				 * CtDepositRuleCriteria ctDepositRuleExample = new
				 * CtDepositRuleCriteria(); CtDepositRuleCriteria.Criteria
				 * ctDepositRuleCriteria =
				 * ctDepositRuleExample.createCriteria();
				 * ctDepositRuleCriteria.andProductCatIdEqualTo(list.get(0).
				 * getBusiType()); List<CtDepositRule> ctDepositRules =
				 * depositRuleAtomSV.selectByExample(ctDepositRuleExample);
				 * if(list.isEmpty()) throw new BusinessException("默认保证金不存在");
				 * else deposit = ctDepositRules.get(0).getDefaultDeposit();
				 */
				// 若保证金未设置,默认0
				deposit = 0L;
			}
		}
		return deposit;
	}

	@Override
	public QueryShopInfoBatchResponse queryShopInfoBatch(QueryShopInfoBatchRequest request)
			throws BusinessException, SystemException {
		QueryShopInfoBatchResponse response = new QueryShopInfoBatchResponse();
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andCreateTimeGreaterThan(request.getBeginTime());
		criteria.andCreateTimeLessThan(request.getEndTime());
		List<ShopInfo> list = shopInfoAtomSV.selectByExample(example);
		List<ShopInfoVo> responseList = new ArrayList<ShopInfoVo>();
		for (ShopInfo shopInfo : list) {
			ShopInfoVo shopInfoVo = new ShopInfoVo();
			BeanUtils.copyProperties(shopInfo, shopInfoVo);
			responseList.add(shopInfoVo);
		}
		response.setList(responseList);
		return response;
	}

	@Override
	public BaseResponse checkShopNameOnly(QueryShopInfoRequest request) throws BusinessException, SystemException {
		
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		if (StringUtil.isBlank(request.getTenantId())) {
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:租户ID不能为空");
		}

		if (StringUtil.isBlank(request.getShopName())) {
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:店铺名称不能为空");
		}
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andShopNameEqualTo(request.getShopName());

		List<ShopInfo> list = shopInfoAtomSV.selectByExample(example);

		BaseResponse baseResponse = new BaseResponse();
		ResponseHeader responseHeader = new ResponseHeader();
		if (CollectionUtil.isEmpty(list)) {
			responseHeader = new ResponseHeader(true, ExceptCodeConstants.Special.SUCCESS, "操作成功");
			baseResponse.setResponseHeader(responseHeader);
			return baseResponse;
		} else if (!StringUtil.isBlank(request.getUserId())) {
			ShopInfo shopInfo = list.get(0);
			if (shopInfo.getUserId().equals(request.getUserId())) {
				responseHeader = new ResponseHeader(true, ExceptCodeConstants.Special.SUCCESS, "操作成功");
				baseResponse.setResponseHeader(responseHeader);
				return baseResponse;
			}else{
				responseHeader = new ResponseHeader(true, ExceptCodeConstants.Special.SYSTEM_ERROR, "店铺名称已注册");
				baseResponse.setResponseHeader(responseHeader);
				return baseResponse;
			}
		}
		return baseResponse;
	}
	
	
	@Override
	public int saveShopAuditInfo(SaveShopAuditInfoRequest request) throws BusinessException, SystemException {
		ShopInfo shopInfo = new ShopInfo();
		if(StringUtil.isBlank(request.getTenantId()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:租户Id不能为空");
		if(StringUtil.isBlank(request.getUserId()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:商户Id不能为空");
		if(StringUtil.isBlank(request.getBusiType()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:经营类型不能为空");
		if(StringUtil.isBlank(request.getGoodsNum()+""))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:可售商品数量不能为空");
		if(StringUtil.isBlank(request.getMerchantNo()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:商户编号不能为空");
		if(StringUtil.isBlank(request.getShopDesc()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:商户简介不能为空");
		if(StringUtil.isBlank(request.getShopName()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:商户名称不能为空");
		if(StringUtil.isBlank(request.getGoodsNum()+""))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:商品数量不能为空");
		if(StringUtil.isBlank(request.getHasExperi()+""))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:有无电商经验是否不能为空");
		BeanUtils.copyProperties(request, shopInfo);
		//0/1/2:未开通/已开通/注销
		shopInfo.setStatus(0);
		shopInfo.setCreateTime(DateUtil.getSysDate());
		//插入日志表
		ShopInfoLog shopInfoLog = new ShopInfoLog();
		BeanUtils.copyProperties(shopInfo, shopInfoLog);
		shopInfo.setCreateTime(DateUtil.getSysDate());
		shopInfoLogAtomSV.insert(shopInfoLog);
		return shopInfoAtomSV.insert(shopInfo);
	}

	@Override
	public int updateShopStatus(UpdateShopStatusRequest request) throws BusinessException, SystemException {
		if(StringUtil.isBlank(request.getTenantId()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:租户id是否不能为空");
		if(StringUtil.isBlank(request.getUserId()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:用户id是否不能为空");
		if(StringUtil.isBlank(request.getStatus()+""))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:状态是否不能为空");
		ShopInfo shopInfo = new ShopInfo();
		BeanUtils.copyProperties(request, shopInfo);
			//判断更新时间
			if (request.getStatus()==1) {
				shopInfo.setOpenTime(DateUtil.getSysDate());
			} else if (request.getStatus()==2) {
				shopInfo.setCloseTime(DateUtil.getSysDate());
			} else if (request.getStatus()==0) {
				shopInfo.setStatus(request.getStatus());
				shopInfo.setCreateTime(DateUtil.getSysDate());
			} else
				shopInfo.setStatus(null);
			
		    //店铺日志表
			ShopInfoLog shopInfoLog = new ShopInfoLog();
			BeanUtils.copyProperties(shopInfo, shopInfoLog);
			shopInfoLog.setUpdateTime(DateUtil.getSysDate());
			ShopInfoLogCriteria shopLogExample = new ShopInfoLogCriteria();
			ShopInfoLogCriteria.Criteria shopLogCriteria = shopLogExample.createCriteria();
			shopLogCriteria.andTenantIdEqualTo(request.getTenantId());
			shopLogCriteria.andUserIdEqualTo(request.getUserId());
			shopInfoLogAtomSV.updateByExample(shopInfoLog, shopLogExample);
			
			ShopInfoCriteria shopExample = new ShopInfoCriteria();
		    ShopInfoCriteria.Criteria shopCriteria = shopExample.createCriteria();
		    shopCriteria.andTenantIdEqualTo(request.getTenantId());
		    shopCriteria.andUserIdEqualTo(request.getUserId());
		    
		    return shopInfoAtomSV.updateByExample(shopInfo, shopExample);
	}

	@Override
	public QueryShopInfoResponse queryShopInfoById(QueryShopInfoByIdRequest request)
			throws BusinessException, SystemException {
		if(StringUtil.isBlank(request.getTenantId()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:租户id是否不能为空");
		if(StringUtil.isBlank(request.getUserId()))
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL, "获取参数失败:状态是否不能为空");
		QueryShopInfoResponse response = new QueryShopInfoResponse();
		ShopInfoCriteria example = new ShopInfoCriteria();
		ShopInfoCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andUserIdEqualTo(request.getUserId());
		List<ShopInfo> list = shopInfoAtomSV.selectByExample(example);
		if(list.isEmpty())
			throw new BusinessException(ExceptCodeConstants.Special.NO_RESULT, "查询数据不存在");
		BeanUtils.copyProperties(list.get(0), response);
		return response;
	}
}
