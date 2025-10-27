package com.imdtravel.dto;

import java.time.LocalDate;

public record BuyRequest(String flight, LocalDate day, String user) {
}
