package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.ContentType;
import com.zx.servicecontract.model.ContentTypeExample;
import org.springframework.stereotype.Repository;

/**
 * ContentTypeDAO继承基类
 */
@Repository
public interface ContentTypeDAO extends MyBatisBaseDao<ContentType, Integer, ContentTypeExample> {
}