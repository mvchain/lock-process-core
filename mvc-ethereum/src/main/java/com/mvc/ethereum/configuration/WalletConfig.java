package com.mvc.ethereum.configuration;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * walletConifg
 *
 * @author qiyichen
 * @create 2018/1/14 14:22
 */
@Component
@ConfigurationProperties(prefix = "wallet")
@Data
public class WalletConfig {

    private Map<String, String> pass;
    private Map<String, String> account;
    private Map<String, String> address;

    public String getPass(String type, String defaultPass){
        String result = defaultPass;
        if (StringUtils.isBlank(defaultPass)){
            result  = pass.get(type);
        }
        return result;
    }

    public String getAccount(String type){
        return account.get(type);
    }

    // 是否为钱包账户
    public Boolean isWalletAccount(String address){
        List<Map.Entry<String, String>> list = account.entrySet().stream().filter(set -> set.getValue().equalsIgnoreCase(address)).collect(Collectors.toList());
        return !CollectionUtils.isEmpty(list);
    }

    public List<String> getAddressList(){
        return Arrays.asList(address.values().toArray(new String[]{}));
    }

    public String getAddress(String type){
        return address.get(type);
    }

    public String getPass(String contractAddress) {
        final String[] pwd = {null};
        address.entrySet().stream().forEach(set -> {
            if(set.getValue().equalsIgnoreCase(contractAddress)){
                pwd[0] = pass.get(set.getKey());
            }
        });
        return pwd[0];
    }

    public String getType(String contractAddress) {
        final String[] type = {null};
        address.entrySet().stream().forEach(set -> {
            if(set.getValue().equalsIgnoreCase(contractAddress)){
                type[0] = set.getKey();
            }
        });
        return type[0];
    }
}
