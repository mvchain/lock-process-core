package com.mvc.ethereum.constant;

import org.web3j.tx.Contract;

import java.math.BigInteger;

/**
 * @author qyc
 */
public class EthConstants {
    public final static String RESOURCE_TYPE_MENU = "menu";
    public final static String RESOURCE_TYPE_BTN = "button";
    public static final Integer EX_TOKEN_ERROR_CODE = 40101;
    public static final Integer EX_USER_INVALID_CODE = 40102;
    public static final Integer EX_CLIENT_INVALID_CODE = 40131;
    public static final Integer EX_CLIENT_FORBIDDEN_CODE = 40331;
    public static final Integer EX_OTHER_CODE = 500;
    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentUserName";
    public static final String CONTEXT_KEY_ADDRESS = "currentUserAddress";
    public static final String CONTEXT_KEY_USER_NAME = "currentUser";
    public static final String CONTEXT_KEY_USER_TOKEN = "currentUserToken";
    public static final String JWT_KEY_USER_ID = "userId";
    public static final String JWT_KEY_NAME = "name";

    public static final String SMS_VALI_PRE = "smsValiCode_";
    public static final Integer DEPOSITE = 0;
    public static final Integer SUCCESS = 2;


    public static final BigInteger GAS_LIMIT = Contract.GAS_LIMIT;
    public static final BigInteger GAS_PRICE = Contract.GAS_PRICE.divide(BigInteger.valueOf(30));

    public static final String OTHER_BALANCE = "OTHER_BALANCE";

}
