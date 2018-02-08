package com.mvc.auth.client.feign;

import com.mvc.common.msg.ObjectRestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author qyc
 */
@FeignClient(value = "${auth.serviceId}", configuration = {})
public interface ServiceAuthFeign {

    /**
     * get allowed client
     *
     * @param serviceId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/myClient")
    public ObjectRestResponse<List<String>> getAllowedClient(@RequestParam("serviceId") String serviceId, @RequestParam("secret") String secret);

    /**
     * getAccessToken
     *
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/token", method = RequestMethod.POST)
    public ObjectRestResponse getAccessToken(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    /**
     * getServicePublicKey
     *
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/servicePubKey", method = RequestMethod.POST)
    public ObjectRestResponse<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    /**
     * getUserPublicKey
     *
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/userPubKey", method = RequestMethod.POST)
    public ObjectRestResponse<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

}
