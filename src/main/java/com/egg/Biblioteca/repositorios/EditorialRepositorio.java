/*
 */
package com.egg.Biblioteca.repositorios;

import com.egg.Biblioteca.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fitog
 */
@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial,String> {

}
