/*
 */
package com.egg.Biblioteca.controladores;

import com.egg.Biblioteca.entidades.Autor;
import com.egg.Biblioteca.entidades.Editorial;
import com.egg.Biblioteca.entidades.Libro;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.servicios.AutorServicio;
import com.egg.Biblioteca.servicios.EditorialServicio;
import com.egg.Biblioteca.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author fitog
 */
@Controller
@RequestMapping("/libro")
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/libro/registrar
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo) {
        try {

            libroServicio.crearLibro(archivo, isbn, titulo, ejemplares, idAutor, idEditorial);
            this.listar(modelo);
            modelo.put("exito", "El Libro fue cargado correctamente!");

        } catch (MiException ex) {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());

            return "libro_form.html";  // volvemos a cargar el formulario.
        }
       
        return "libro_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Libro> libros = libroServicio.listarLibros();

        modelo.addAttribute("libros", libros);

        return "libro_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {

        modelo.put("libro", libroServicio.getOne(isbn));

        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, ModelMap modelo, MultipartFile archivo) {
        try {

            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            libroServicio.modificarLibro(archivo, isbn, titulo, ejemplares, idAutor, idEditorial);

            modelo.put("exito", "El libro fue modificado con exito!");
            this.listar(modelo);

            return "libro_list.html";

        } catch (MiException ex) {

            this.listar(modelo);

            modelo.put("error", ex.getMessage());

            return "libro_modificar.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar/{isbn}")
    public String eliminar(@PathVariable Long isbn, ModelMap modelo) throws MiException {
        libroServicio.eliminar(isbn);
        return "libro_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar/{isbn}")
    public String eliminar(@PathVariable Long isbn, String titulo, ModelMap modelo) {
        try {
            libroServicio.eliminar(isbn);
            modelo.put("exito", "Se elimino el libro con exito!");
            this.listar(modelo);
            return "libro_list.html";
        } catch (MiException ex) {
            modelo.put("error", "El libro no pudo ser eliminado.");
            return "libro_modificar.html";
        }

    }

}
