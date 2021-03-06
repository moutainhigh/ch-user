package com.ai.ch.user.custfile.impl.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.ch.user.api.custfile.interfaces.ICustFileSV;
import com.ai.ch.user.api.custfile.params.CmCustFileExtVo;
import com.ai.ch.user.api.custfile.params.QueryCustFileExtRequest;
import com.ai.ch.user.api.custfile.params.UpdateCustFileExtRequest;
import com.ai.opt.base.vo.BaseResponse;
import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/context/core-context.xml" })
public class CustFileSVImpl {

	@Autowired
	private ICustFileSV custFileSV;
	
	@Test
	public void testQuery(){
		QueryCustFileExtRequest re = new QueryCustFileExtRequest();
		re.setTenantId("ch");
		System.out.println(JSON.toJSONString(custFileSV.queryCustFileExt(re)));
	}
	@Test
	public void testUpdate(){
		UpdateCustFileExtRequest re = new UpdateCustFileExtRequest();
		re.setTenantId("ch");
		List<CmCustFileExtVo> list = new ArrayList<CmCustFileExtVo>();
		CmCustFileExtVo c = new CmCustFileExtVo();
		c.setTenantId("ch");
		list.add(c);
		list.add(c);
		list.add(c);
		re.setList(list);
		System.out.println(JSON.toJSONString(custFileSV.updateCustFileExt(re)));
	}
	
	
	@Test
	public void testUpdateCondition(){
		UpdateCustFileExtRequest re = new UpdateCustFileExtRequest();
		re.setTenantId("changhong");
		List<CmCustFileExtVo> list = new ArrayList<CmCustFileExtVo>();
		CmCustFileExtVo c = new CmCustFileExtVo();
		c.setTenantId("changhong");
		c.setInfoType("30");
		c.setInfoName("er555");
		list.add(c);
		list.add(c);
		list.add(c);
		re.setList(list);
		System.out.println(JSON.toJSONString(custFileSV.updateCustFileExtBycondition(re)));
	}
	
	
	@Test
	public void testDeleteCondition(){
		QueryCustFileExtRequest request = new QueryCustFileExtRequest();
		request.setTenantId("changhong");
		request.setInfoExtId("000000000000002282");
		BaseResponse baseResponse = custFileSV.deleteCustFileExtBycondition(request);
		/*request.setAttrValue("57ebf53d702da77c8bcaa8e8");
		BaseResponse baseResponse = custFileSV.deleteCustFileExtBycondition(request);*/
		//System.out.println(baseResponse.getResponseHeader().getIsSuccess());
		
	}
}
