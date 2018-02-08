package com.mvc.ethereum.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qyc
 */
@Data
public class BalanceDTO implements Serializable {
    private String address;
    private String blockId;
}
