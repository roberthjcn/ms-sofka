package com.sofka.ms_accounts.controller;

import com.sofka.ms_accounts.dto.AccountStatementDTO;
import com.sofka.ms_accounts.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<AccountStatementDTO>> getAccountStatement(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> fecha) {
        if (fecha.size() != 2) {
            throw new IllegalArgumentException("El rango de fechas debe contener exactamente dos fechas");
        }

        LocalDate startDate = fecha.get(0);
        LocalDate endDate = fecha.get(1);

        List<AccountStatementDTO> report = reportService.generateAccountStatement(startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
