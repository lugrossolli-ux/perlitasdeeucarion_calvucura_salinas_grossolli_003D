package com.example.service_auth.service;

import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.model.Rol;
import com.example.service_auth.model.Usuario;
import com.example.service_auth.repository.RolRepository;
import com.example.service_auth.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired 
    private RolRepository rolRepository;
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registrar(AuthRequest request) {
        if (usuarioRepository.findByNombreUsuario(request.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(request.getNombreUsuario());
        nuevoUsuario.setCorreo(request.getCorreo());
        nuevoUsuario.setContrasena(passwordEncoder.encode(request.getContrasena()));

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Rol rolPorDefecto = rolRepository.findByNombreRol("VENDEDOR")
                    .orElseThrow(() -> new RuntimeException("Error: El rol VENDEDOR no existe en la DB."));
            nuevoUsuario.getRoles().add(rolPorDefecto);
        } else {
            for (String nombreRol : request.getRoles()) {
                Rol rolEncontrado = rolRepository.findByNombreRol(nombreRol.toUpperCase())
                        .orElseThrow(() -> new RuntimeException("Error: El rol " + nombreRol + " no existe en la DB."));
                nuevoUsuario.getRoles().add(rolEncontrado);
            }
        }

        usuarioRepository.save(nuevoUsuario);
        return "Usuario Registrado";
    }

    @Transactional(readOnly = true)
    public String login(String nombreUsuario, String contrasena) {
    
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        List<String> rolesList = usuario.getRoles().stream()
                .map(rol -> rol.getNombreRol())
                .collect(Collectors.toList());

        return jwtService.generarToken(usuario.getNombreUsuario(), rolesList);
    }
}