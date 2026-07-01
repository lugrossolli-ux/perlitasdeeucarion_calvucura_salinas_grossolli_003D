package com.example.service_auth.config;

import com.example.service_auth.model.Rol;
import com.example.service_auth.model.Usuario;
import com.example.service_auth.repository.RolRepository;
import com.example.service_auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedRoles(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (rolRepository.count() > 0) return;

            Rol admin = rolRepository.save(new Rol(null, "ADMIN"));
            Rol vendedor = rolRepository.save(new Rol(null, "VENDEDOR"));
            Rol cliente = rolRepository.save(new Rol(null, "CLIENTE"));

            Usuario adminUser = new Usuario();
            adminUser.setNombreUsuario("admin");
            adminUser.setContrasena(passwordEncoder.encode("admin123"));
            adminUser.setCorreo("admin@perlitas.cl");
            adminUser.setRoles(Set.of(admin));
            usuarioRepository.save(adminUser);

            Usuario vendedorUser = new Usuario();
            vendedorUser.setNombreUsuario("vendedor");
            vendedorUser.setContrasena(passwordEncoder.encode("vendedor123"));
            vendedorUser.setCorreo("vendedor@perlitas.cl");
            vendedorUser.setRoles(Set.of(vendedor));
            usuarioRepository.save(vendedorUser);
        };
    }
}
