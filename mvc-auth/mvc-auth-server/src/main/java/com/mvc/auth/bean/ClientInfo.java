package com.mvc.auth.bean;


import com.mvc.auth.common.util.jwt.IJwtInfo;

/**
 * Created by ace on 2017/9/10.
 */
public class ClientInfo implements IJwtInfo {
    String clientId;
    String name;
    String address;
    public ClientInfo(String clientId, String name, String id) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
    }

    public ClientInfo(String clientId, String name, String id, String address) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return clientId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
