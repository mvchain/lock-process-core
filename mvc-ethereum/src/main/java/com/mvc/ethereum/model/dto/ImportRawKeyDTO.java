package com.mvc.ethereum.model.dto;


import lombok.Data;

/**
 * @author qyc
 */
@Data
public class ImportRawKeyDTO {

    private String keydata;
    private String passphrase;
}
