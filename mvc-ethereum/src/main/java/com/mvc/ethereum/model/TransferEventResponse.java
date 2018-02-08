package com.mvc.ethereum.model;

import lombok.Data;

/**
 * @author qyc
 */
@Data
public class TransferEventResponse {
    private String from;
    private String to;
    private long value;

    public TransferEventResponse() {
    }

    public TransferEventResponse(
            HumanStandardToken.TransferEventResponse transferEventResponse) {
        this.from = transferEventResponse.from.toString();
        this.to = transferEventResponse.to.toString();
        this.value = transferEventResponse.value.getValue().longValueExact();
    }
}