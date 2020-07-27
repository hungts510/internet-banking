package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StatisticInfo {
    @JsonProperty(value = "list_transaction_to")
    private List<TransactionInfo> listTransactionTo;

    @JsonProperty(value = "list_transaction_from")
    private List<TransactionInfo> listTransactionFrom;

    @JsonProperty(value = "total_to")
    private long totalTo;

    @JsonProperty(value = "total_from")
    private long totalFrom;

    @JsonProperty(value = "bank_name")
    private String bankName;
}
