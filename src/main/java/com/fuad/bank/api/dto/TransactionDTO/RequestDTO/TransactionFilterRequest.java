package com.fuad.bank.api.dto.TransactionDTO.RequestDTO;

import com.fuad.bank.domain.enums.TransactionStatus;
import com.fuad.bank.domain.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionFilterRequest {

    private Long accountId;
    private TransactionType type;
    private TransactionStatus status;
}
