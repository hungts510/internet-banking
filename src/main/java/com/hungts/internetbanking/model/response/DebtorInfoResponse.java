package com.hungts.internetbanking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hungts.internetbanking.model.info.DebtorInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class DebtorInfoResponse {
    @JsonProperty(value = "list_debtors")
    List<DebtorInfo> listDebtors = new LinkedList<>();

    @JsonProperty(value = "list_debts")
    List<DebtorInfo> listDebts = new LinkedList<>();
}
