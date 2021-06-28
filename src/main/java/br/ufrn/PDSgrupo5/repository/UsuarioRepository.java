package br.ufrn.PDSgrupo5.repository;

import br.ufrn.PDSgrupo5.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLogin(String login);
}
