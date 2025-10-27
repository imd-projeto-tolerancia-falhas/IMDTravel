package com.imdtravel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FlightResponse(String flight, LocalDate day, BigDecimal value) {
}
