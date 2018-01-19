package com.mvc.console.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mvc.common.biz.BaseBiz;
import com.mvc.console.entity.Config;
import com.mvc.console.entity.LockConfig;
import com.mvc.console.entity.WithdrawConfig;
import com.mvc.console.mapper.ConfigMapper;
import com.mvc.console.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
        updateConfig();
    }

    public void updateConfig() {
        configMapper.selectAll().stream().forEach(config -> {
            JSON.parseObject(config.getConfig(), JSONArray.class).stream().forEach(json -> {
                JSONObject jsonObject = (JSONObject)json;
                if (config.getType() == 1){
                    //  锁仓
                    LockConfig lockConfig = new LockConfig();
                    lockConfig.setInterest(Float.valueOf(jsonObject.get("interest").toString()));
                    lockConfig.setMax(  new BigDecimal(jsonObject.get("max").toString()) );
                    lockConfig.setMin(  new BigDecimal(jsonObject.get("min").toString()) );
                    lockConfig.setMonth(Integer.valueOf(jsonObject.get("month").toString()));
                    lockConfig.setSwitchKey(config.getSwitchKey());
                    lockConfig.setType(jsonObject.get("type").toString());
                    ConfigUtil.lockConfigMap.put(jsonObject.get("type").toString(), lockConfig);
                } else {
                    // 提现
                    WithdrawConfig withdrawConfig = new WithdrawConfig();
                    withdrawConfig.setMax(  new BigDecimal(jsonObject.get("max").toString()) );
                    withdrawConfig.setMin(  new BigDecimal(jsonObject.get("min").toString()) );
                    withdrawConfig.setSwitchKey(config.getSwitchKey());
                    withdrawConfig.setType(jsonObject.get("type").toString());
                    withdrawConfig.setPoundage(Float.valueOf(jsonObject.get("poundage").toString()));
                    ConfigUtil.withdrawConfigMap.put(jsonObject.get("type").toString(), withdrawConfig);
                }
            });
        });
    }
}
