package com.imdtravel.dto;

import java.time.LocalDate;

public record SellRequest(String flight, LocalDate day) {
}
