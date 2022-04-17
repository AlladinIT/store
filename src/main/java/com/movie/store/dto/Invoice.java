package com.movie.store.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Invoice {
    private BigDecimal totalSum;
    private List<InvoiceRow> invoiceRows;


}

