package com.mvc.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qyc
 */
public interface IUserPrincipal {
    /**
     * getName
     *
     * @param request
     * @return
     */
    String getName(HttpServletRequest request);
}
