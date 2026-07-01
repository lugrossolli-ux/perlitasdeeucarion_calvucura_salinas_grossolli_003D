package com.example.service_auth.service;

import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.model.Rol;
import com.example.service_auth.model.Usuario;
import com.example.service_auth.repository.RolRepository;
import com.example.service_auth.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrar_whenUserDoesNotExist_shouldRegister() {
        AuthRequest request = new AuthRequest();
        request.setNombreUsuario("nuevo");
        request.setContrasena("pass123");
        request.setCorreo("nuevo@test.cl");
        request.setRoles(Set.of("ADMIN"));

        when(usuarioRepository.findByNombreUsuario("nuevo")).thenReturn(Optional.empty());

        Rol rolAdmin = new Rol();
        rolAdmin.setId(1L);
        rolAdmin.setNombreRol("ADMIN");
        when(rolRepository.findByNombreRol("ADMIN")).thenReturn(Optional.of(rolAdmin));

        when(passwordEncoder.encode("pass123")).thenReturn("encoded");

        String result = authService.registrar(request);

        assertEquals("Usuario Registrado", result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrar_whenUserAlreadyExists_shouldThrow() {
        AuthRequest request = new AuthRequest();
        request.setNombreUsuario("existente");

        when(usuarioRepository.findByNombreUsuario("existente")).thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.registrar(request));
        assertTrue(ex.getMessage().contains("ya existe"));
    }

    @Test
    void registrar_whenDefaultRoleVENDEDOR_shouldAssignDefault() {
        AuthRequest request = new AuthRequest();
        request.setNombreUsuario("test");
        request.setContrasena("pass");
        request.setCorreo("test@test.cl");
        request.setRoles(null);

        when(usuarioRepository.findByNombreUsuario("test")).thenReturn(Optional.empty());

        Rol rolVendedor = new Rol();
        rolVendedor.setId(2L);
        rolVendedor.setNombreRol("VENDEDOR");
        when(rolRepository.findByNombreRol("VENDEDOR")).thenReturn(Optional.of(rolVendedor));

        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        authService.registrar(request);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void login_withValidCredentials_shouldReturnToken() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("admin");
        usuario.setContrasena("encodedPass");

        Rol rol = new Rol();
        rol.setNombreRol("ADMIN");
        usuario.setRoles(Set.of(rol));

        when(usuarioRepository.findByNombreUsuario("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);
        when(jwtService.generarToken("admin", List.of("ADMIN"))).thenReturn("token123");

        String token = authService.login("admin", "pass");

        assertEquals("token123", token);
        verify(jwtService).generarToken("admin", List.of("ADMIN"));
    }

    @Test
    void login_withInvalidPassword_shouldThrow() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("admin");
        usuario.setContrasena("encodedPass");

        when(usuarioRepository.findByNombreUsuario("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login("admin", "wrong"));
        assertTrue(ex.getMessage().contains("Credenciales inv\u00e1lidas"));
    }

    @Test
    void login_withNonExistentUser_shouldThrow() {
        when(usuarioRepository.findByNombreUsuario("ghost")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login("ghost", "pass"));
        assertTrue(ex.getMessage().contains("Credenciales inv\u00e1lidas"));
    }
}
