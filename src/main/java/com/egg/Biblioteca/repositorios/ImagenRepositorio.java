/*
 */
package com.egg.Biblioteca.repositorios;

import com.egg.Biblioteca.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fitog
 */
public interface ImagenRepositorio extends JpaRepository<Imagen, String> {
    
}
