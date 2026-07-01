package com.example.service_auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de autenticaci�n o registro")
public class AuthRequest 
{
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario", example = "admin")
    private String nombreUsuario;

    @NotBlank(message = "La contrase�a es obligatoria")
    @Schema(description = "Contrase�a del usuario", example = "admin123")
    private String contrasena;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser una direcci�n de correo v�lida")
    @Schema(description = "Correo electr�nico del usuario", example = "admin@perlitas.cl")
    private String correo;
    
    private Set<String> roles;
}