/*
 */
package com.egg.Biblioteca.servicios;

import com.egg.Biblioteca.entidades.Editorial;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.repositorios.EditorialRepositorio;
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
public class EditorialServicio {

    @Autowired
    EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearEditorial(String nombre) throws MiException{
        
        validar(nombre);
        
        Editorial editorial = new Editorial();

        editorial.setNombre(nombre);

        editorialRepositorio.save(editorial);
    }

    public List<Editorial> listarEditoriales() {

        List<Editorial> editoriales = new ArrayList();

        editoriales = editorialRepositorio.findAll();

        return editoriales;
    }
    
    @Transactional
    public void modificarEditorial(String id, String nombre) throws MiException{
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();

            editorial.setNombre(nombre);

            editorialRepositorio.save(editorial);
        }
    }
    
    @Transactional
    public void eliminar(String id) throws MiException, DataIntegrityViolationException{
        
        Optional<Editorial> resp = editorialRepositorio.findById(id);
        if (resp.isPresent()) {
            try{
                editorialRepositorio.delete(resp.get());
            }catch(DataIntegrityViolationException ex){
                throw new DataIntegrityViolationException("error");
            }
        }
        
    }
    
    public Editorial getOne(String id){
        return editorialRepositorio.getOne(id);
    } 

    private void validar(String nombre) throws MiException {
        
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre de la editorial no puede ser nulo o estar vacio");
        }
    }
}
