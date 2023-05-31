/*
 */
package com.egg.Biblioteca.servicios;

import com.egg.Biblioteca.entidades.Autor;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 *
 * @author fitog
 */
@Service
public class AutorServicio {
    @Autowired
    AutorRepositorio autorRepositorio;
    
    @Transactional
    public void crearAutor(String nombre) throws MiException{
        
        validar(nombre);
        
        Autor autor = new Autor();
        
        autor.setNombre(nombre);

        autorRepositorio.save(autor);
      
    }
    

    public List<Autor> listarAutores() {

        List<Autor> autores = new ArrayList();

        autores = autorRepositorio.findAll();

        return autores;
    }
    
    @Transactional
    public void modificarAutor(String nombre, String id) throws MiException{
        
        validar(nombre);
        
        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            
            autor.setNombre(nombre);

            autorRepositorio.save(autor);

        }
    }
    
    @Transactional
    public void eliminar(String id) throws MiException, DataIntegrityViolationException{
        
        Optional<Autor> resp = autorRepositorio.findById(id);
        if (resp.isPresent()) {
            try{
                autorRepositorio.delete(resp.get());
            } catch (DataIntegrityViolationException ex){
                throw new DataIntegrityViolationException ("error");
            }
            
        }
    }
    
     public Autor getOne(String id){
        return autorRepositorio.getOne(id);
    }
    
     private void validar(String nombre) throws MiException {
        
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
    }
        
}
