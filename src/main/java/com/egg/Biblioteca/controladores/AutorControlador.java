/*
 */
package com.egg.Biblioteca.controladores;

import com.egg.Biblioteca.entidades.Autor;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.servicios.AutorServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author fitog
 */
@Controller
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@RequestMapping("/autor") //localhost:8080/autor
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar") //localhost:8080/autor/registrar
    public String registrar() {
        return "autor_form.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
//    public String registro(@RequestParam String nombre, ModelMap modelo) {
    public String registro(@RequestParam String nombre, ModelMap modelo) {

////testear si el metodo funciona:
//System.out.println("Nombre: " + nombre);
//        return "autor_form.html";
        try {
            autorServicio.crearAutor(nombre);
            this.listar(modelo);
            modelo.put("exito", "El Autor fue registrado correctamente!");
            
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

//            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "autor_form.html";
        }

        return "autor_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Autor> autores = autorServicio.listarAutores();

        modelo.addAttribute("autores", autores);

        return "autor_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("autor", autorServicio.getOne(id));

        return "autor_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo) {
        try {
            autorServicio.modificarAutor(nombre, id);
            modelo.put("exito", "Se modifico el autor con exito!");
            this.listar(modelo);
            return "autor_list.html";
        } catch (MiException ex) {

            modelo.put("autor", autorServicio.getOne(id));
            modelo.put("error", ex.getMessage());
            return "autor_modificar.html";
        }

    }

//     @GetMapping("/eliminar/{id}")
//    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
//        autorServicio.eliminar(id);
//
//        return "autor_list.html";
//    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, String nombre, ModelMap modelo) throws MiException, DataIntegrityViolationException {
        try {
            autorServicio.eliminar(id);
            modelo.put("exito", "Se elimino con exito!");
            this.listar(modelo);
            return "autor_list.html";
            
        } catch (DataIntegrityViolationException ex) {
            modelo.put("error", "El autor esta en uso y no puede ser eliminado.");   
            this.listar(modelo);
            return "autor_list.html";
        }

    }

}
