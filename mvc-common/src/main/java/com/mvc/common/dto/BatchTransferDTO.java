package com.mvc.common.dto;

import lombok.Data;
import lombok.NonNull;
import org.json.JSONArray;

import java.util.List;


/**
 * batchTransfer dto
 *
 * @author qiyichen
 * @create 2018/1/14 14:30
 */
@Data
public class BatchTransferDTO {

    @NonNull
    private String type;
    @NonNull
    private List<TransferDTO> transferDTOS;

    private String pass;
}
