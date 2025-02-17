package com.sofka.ms_accounts.controller;

import com.sofka.ms_accounts.dto.MovementDTO;
import com.sofka.ms_accounts.service.MovementService;
import com.sofka.ms_accounts.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;

    @PostMapping
    public ResponseEntity<ApiResponse<MovementDTO>> createMovement(@RequestBody MovementDTO movementDTO) {
        System.out.println("aqui: "+movementDTO);
        return ResponseEntity.ok(new ApiResponse<>("Movimiento registrado con éxito", movementService.createMovement(movementDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovementDTO>> getMovementById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>("Movimiento obtenido", movementService.getMovementById(id)));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<MovementDTO>>> getMovementsByAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(new ApiResponse<>("Movimientos obtenidos", movementService.getMovementsByAccount(accountId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovementDTO>>> getAllMovements() {
        System.out.println("isdsdsjdjskds");
        return ResponseEntity.ok(new ApiResponse<>("Todos los movimientos obtenidos", movementService.getAllMovements()));
    }
}
