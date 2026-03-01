package com.br.finunity.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.br.finunity.api.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Usuario findByUsuarioAndSenha(String usuario, String senha);

}