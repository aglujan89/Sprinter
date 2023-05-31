/*
 */
package com.egg.Biblioteca.controladores;


import com.egg.Biblioteca.entidades.Editorial;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/editorial")
public class EditorialControlador {
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/registrar") //localhost:8080/autor/registrar
    public String registrar(){
        return "editorial_form.html";
    }
    
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        
        try {
            editorialServicio.crearEditorial(nombre);
            this.listar(modelo);
            modelo.put("exito", "La Editorial fue registrada correctamente!");
        } catch (MiException ex) {
                       
            modelo.put("error", ex.getMessage());
            return "editorial_form.html";
        }
        
        return "editorial_list.html";        
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales", editoriales);
        
        return "editorial_list.html";
    }
    
    @GetMapping("modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("editorial", editorialServicio.getOne(id));
        return "editorial_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo){
        
        try{
            editorialServicio.modificarEditorial(id, nombre);
            modelo.put("exito","Se modifico la editorial con exito!");
            this.listar(modelo);
            return "editorial_list.html";
        } catch (MiException ex){
            modelo.put("editorial", editorialServicio.getOne(id));
            modelo.put("error", ex.getMessage());
            return "editorial_modificar.html";
        } 
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, String nombre, ModelMap modelo) throws MiException, DataIntegrityViolationException{
        try{
            editorialServicio.eliminar(id);
            modelo.put("exito", "Se elimino con exito!");
            this.listar(modelo);
            return "editorial_list.html";
            
        }catch(DataIntegrityViolationException ex){
            modelo.put("error", "La editorial esta en uso y no puede ser eliminada.");
            this.listar(modelo);
            return "editorial_list.html";
        }
    }
    
}
