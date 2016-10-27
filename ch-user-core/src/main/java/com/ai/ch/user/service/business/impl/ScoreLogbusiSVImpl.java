package com.ai.ch.user.service.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ai.ch.user.api.score.param.CountScoreAvgRequest;
import com.ai.ch.user.api.score.param.CtScoreLogVo;
import com.ai.ch.user.api.score.param.InsertScoreLogRequest;
import com.ai.ch.user.api.score.param.QueryScoreLogRequest;
import com.ai.ch.user.api.score.param.QueryScoreLogResponse;
import com.ai.ch.user.dao.mapper.bo.CtCurrentScore;
import com.ai.ch.user.dao.mapper.bo.CtCurrentScoreCriteria;
import com.ai.ch.user.dao.mapper.bo.CtScoreLog;
import com.ai.ch.user.service.atom.interfaces.ICurrentScoreAtomSV;
import com.ai.ch.user.service.atom.interfaces.IScoreLogAtomSV;
import com.ai.ch.user.service.business.interfaces.IScoreLogBusiSV;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.base.vo.PageInfo;
import com.ai.opt.sdk.constants.ExceptCodeConstants;
import com.ai.opt.sdk.util.DateUtil;

@Component
@Transactional
public class ScoreLogbusiSVImpl implements IScoreLogBusiSV {

	@Autowired
	private IScoreLogAtomSV scoreLogAtomSV;
	
	@Autowired
	private ICurrentScoreAtomSV scoreAtomSV;

	@Override
	public int insertScoreLog(InsertScoreLogRequest request) throws BusinessException, SystemException {
		CtScoreLog ctScoreLog = new CtScoreLog();
		BeanUtils.copyProperties(request, ctScoreLog);
		ctScoreLog.setScoreDate(DateUtil.getSysDate());
		scoreLogAtomSV.insert(ctScoreLog);
		/*//查询当前综合评分
		CountScoreAvgRequest countScoreAvgRequest = new CountScoreAvgRequest();
		BeanUtils.copyProperties(request, countScoreAvgRequest);
		float score = countScoreAvg(countScoreAvgRequest);
		//更新当前记录
		CtCurrentScoreCriteria example = new CtCurrentScoreCriteria();
		CtCurrentScoreCriteria.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(request.getTenantId());
		criteria.andUserIdEqualTo(request.getUserId());
		CtCurrentScore ctCurrentScore = new CtCurrentScore();
		BeanUtils.copyProperties(request, ctCurrentScore);
		ctCurrentScore.setTotalScore(Integer.valueOf(score+""));
		ctCurrentScore.setScoreDate(DateUtil.getSysDate());
		scoreAtomSV.updateByExample(ctCurrentScore, example);*/
		return 0;
	}

	@Override
	public QueryScoreLogResponse queryScoreLog(QueryScoreLogRequest request) throws BusinessException, SystemException {
		QueryScoreLogResponse response = new QueryScoreLogResponse();
		int count = scoreLogAtomSV.queryCountScoreLog(request.getTenantId(), request.getUserId());

		PageInfo<CtScoreLogVo> pageInfo = new PageInfo<CtScoreLogVo>();
		pageInfo.setCount(count);

		int pageNo = request.getPageNo();
		int pageSize = request.getPageSize();
		int startPage = (pageNo - 1) * pageSize;
		int endPage = pageSize;

		List<CtScoreLog> list = scoreLogAtomSV.queryScoreLog(request.getTenantId(), request.getUserId(),
				startPage, endPage);
		List<CtScoreLogVo> responseList = new ArrayList<CtScoreLogVo>();
		for (CtScoreLog ctScoreLog : list) {
			CtScoreLogVo ctScoreLogVo = new CtScoreLogVo();
			BeanUtils.copyProperties(ctScoreLog, ctScoreLogVo);
			responseList.add(ctScoreLogVo);
		}

		pageInfo.setPageNo(pageNo);
		pageInfo.setPageSize(pageSize);
		int pageCount = count / pageSize + (count % pageSize > 0 ? 1 : 0);
		pageInfo.setPageCount(pageCount);
		pageInfo.setResult(responseList);
		response.setPageInfo(pageInfo);
		return response;
	}

	@Override
	public float countScoreAvg(CountScoreAvgRequest request) throws BusinessException, SystemException {
		
		List<CtScoreLog> list = scoreLogAtomSV.selectScoreLogMax(request.getTenantId(), request.getUserId());
		float avgScore = 0;
		if(!list.isEmpty()){
			for (CtScoreLog ctScoreLog : list) {
				avgScore+=ctScoreLog.getTotalScore();
			}
			avgScore=avgScore/list.size();
		}else{
			throw new BusinessException(ExceptCodeConstants.Special.PARAM_IS_NULL,"没有找到该供应商评分");
		}
		return avgScore;
	}

}
