/*
 */
package com.egg.Biblioteca.repositorios;

import com.egg.Biblioteca.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fitog
 */
@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {

}