package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.ContentType;
import com.zx.servicegateway.model.ContentTypeExample;
import org.springframework.stereotype.Repository;

/**
 * ContentTypeDAO继承基类
 */
@Repository
public interface ContentTypeDAO extends MyBatisBaseDao<ContentType, Integer, ContentTypeExample> {
}