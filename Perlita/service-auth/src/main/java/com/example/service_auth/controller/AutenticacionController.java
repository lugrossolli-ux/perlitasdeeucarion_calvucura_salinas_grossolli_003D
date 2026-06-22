package com.example.service_auth.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AutenticacionController
{
    @Autowired
    private AuthService authService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Guarda el usuario mapeando sus roles desde el DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody AuthRequest request)
    {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @Operation(summary = "Iniciar sesión", description = "Retorna el Token JWT si las credenciales son válidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token JWT generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest request)
    {
        String token = authService.login(request.getNombreUsuario(), request.getContrasena());
        return ResponseEntity.ok(token);
    }
}
