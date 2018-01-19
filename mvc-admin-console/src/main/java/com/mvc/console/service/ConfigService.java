package com.mvc.console.service;

import com.mvc.common.biz.BaseBiz;
import com.mvc.console.entity.Config;
import com.mvc.console.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConfigService extends BaseBiz<ConfigMapper,Config> {

    @Autowired
    private ConfigMapper configMapper;

    public void insertOrUpdate(Config config) {
        Config conf = new Config();
        conf.setType(config.getType());
        configMapper.delete(conf);
        configMapper.insert(config);
    }
}
