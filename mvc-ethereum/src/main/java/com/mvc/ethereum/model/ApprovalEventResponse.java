package com.mvc.ethereum.model;

import lombok.Data;

/**
 * @author qyc
 */
@Data
public class ApprovalEventResponse {
    private String owner;
    private String spender;
    private long value;

    public ApprovalEventResponse() {
    }

    public ApprovalEventResponse(
            HumanStandardToken.ApprovalEventResponse approvalEventResponse) {
        this.owner = approvalEventResponse.owner.toString();
        this.spender = approvalEventResponse.spender.toString();
        this.value = approvalEventResponse.value.getValue().longValueExact();
    }
}