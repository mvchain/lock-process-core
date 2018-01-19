package com.mvc.user.service;

import com.mvc.common.biz.BaseBiz;
import com.mvc.user.entity.ResourceAuthority;
import com.mvc.user.mapper.ResourceAuthorityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qyc
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceAuthorityBiz extends BaseBiz<ResourceAuthorityMapper, ResourceAuthority> {
}
