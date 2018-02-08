/**
 *
 */
package com.mvc.ethereum.configuration;

import com.mvc.ethereum.mapper.CoinInfoMapper;
import com.mvc.ethereum.utils.CoinUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;

/**
 * @author qyc
 */
@Configuration
@EnableConfigurationProperties
@Slf4j
public class RpcConfiguration {

    @Value("${org.ethereum.address}")
    private String ethereumAddress;
    @Autowired
    private OkHttpClient okHttpClient;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(ethereumAddress, okHttpClient, false));
    }

    @Bean
    public Admin admin() {

        return Admin.build(new HttpService(ethereumAddress, okHttpClient, false));
    }

    @Bean
    public Geth geth() {
        return Geth.build(new HttpService(ethereumAddress, okHttpClient, false));
    }

    @Bean
    public Quorum quorum() {
        return Quorum.build(new HttpService(ethereumAddress, okHttpClient, false));
    }

    @Bean
    public CoinUtil coinUtil(CoinInfoMapper coinInfoMapper) {
        coinInfoMapper.selectAll().stream().forEach(obj -> {
            CoinUtil.coinMap.put(obj.getId(), obj);
        });
        return new CoinUtil();
    }
}
