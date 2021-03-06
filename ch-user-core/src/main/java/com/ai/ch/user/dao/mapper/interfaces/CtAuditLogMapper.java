package com.ai.ch.user.dao.mapper.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.ch.user.dao.mapper.bo.CtAuditLog;
import com.ai.ch.user.dao.mapper.bo.CtAuditLogCriteria;
import com.ai.ch.user.vo.CtAuditLogVo;

public interface CtAuditLogMapper {
    int countByExample(CtAuditLogCriteria example);
    
    int countByLike(CtAuditLogVo record);

    int deleteByExample(CtAuditLogCriteria example);

    int deleteByPrimaryKey(String logId);

    int insert(CtAuditLog record);

    int insertSelective(CtAuditLog record);

    List<CtAuditLog> selectByExample(CtAuditLogCriteria example);
    
    List<CtAuditLog> selectByLike(CtAuditLogVo record);

    CtAuditLog selectByPrimaryKey(String logId);

    int updateByExampleSelective(@Param("record") CtAuditLog record, @Param("example") CtAuditLogCriteria example);

    int updateByExample(@Param("record") CtAuditLog record, @Param("example") CtAuditLogCriteria example);

    int updateByPrimaryKeySelective(CtAuditLog record);

    int updateByPrimaryKey(CtAuditLog record);
}