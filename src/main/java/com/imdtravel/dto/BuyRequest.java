package com.imdtravel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record BuyRequest(String flight, @JsonFormat(pattern = "dd/MM/yyyy") LocalDate day, String user) {
}
