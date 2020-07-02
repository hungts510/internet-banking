package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class TransactionMetaData {
    @JsonProperty("list_transfers")
    List<TransactionInfo> listTransfers = new LinkedList<>();

    @JsonProperty("list_pay_in")
    List<TransactionInfo> listPayIn = new LinkedList<>();

    @JsonProperty("list_debts")
    List<TransactionInfo> listDebts = new LinkedList<>();
}
