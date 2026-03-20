package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.Usuario;

public interface UsuarioService extends GenericoService <Usuario, Long>{
    List<Usuario> listarUsuarios();
    Usuario buscarPorNombreUser(String nombre);
    Usuario compararNombreUser(String nombreActual, String nombre);
    Usuario getUsuarioPassword(String usuario, String password);
}
