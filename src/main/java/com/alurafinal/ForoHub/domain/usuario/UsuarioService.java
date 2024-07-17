package com.alurafinal.ForoHub.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(String nombre, String email, String contraseña) {
        Usuario usuario = new Usuario(nombre, email, contraseña);
        return usuarioRepository.save(usuario);
    }
}
