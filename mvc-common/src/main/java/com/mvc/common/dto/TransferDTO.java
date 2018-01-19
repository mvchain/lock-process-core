package com.mvc.common.dto;


import lombok.Data;
import lombok.NonNull;

import java.math.BigInteger;

/**
 * transfer dto
 *
 * @author qiyichen
 * @create 2018/1/14 14:32
 */
@Data
public class TransferDTO {

    @NonNull
    private Float value;
    @NonNull
    private String address;
    @NonNull
    private String type;

    private String pass;
}
