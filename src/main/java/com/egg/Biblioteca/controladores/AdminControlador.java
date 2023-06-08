/*
 */
package com.egg.Biblioteca.controladores;

import com.egg.Biblioteca.entidades.Usuario;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.servicios.UsuarioServicio;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin")
public class AdminControlador {

//    @GetMapping("/")
//public String index(ModelMap modelo, HttpSession session) {
//
//    Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//    if (session == null || logueado == null) {
//        return "index.html";
//    } else {
//        return "redirect:/inicio";
//    }
//}
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, ModelMap modelo) {
        usuarioServicio.cambiarRol(id);
        modelo.put("exito", "Se cambio el Rol con exito!");
        this.listar(modelo);
        return "usuario_list.html";
    }

    @GetMapping("/modificarUsuario/{id}")
    public String actualizar(@PathVariable String id, ModelMap modelo) {
        try {
            modelo.put("usuario", usuarioServicio.getOne(id));
            modelo.put("exito", "Se modifico el usuario con exito!");
            return "usuario_modificar.html";
        } catch (Exception ex) {
            modelo.put("usuario", usuarioServicio.getOne(id));
            modelo.put("error", ex.getMessage());
            this.listar(modelo);
            return "usuario_list.html";
        }
    }

    @PostMapping("/modificarUsuario/{id}")
    public String actualizar1(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) throws IOException {
        try {
            modelo.put("usuario", usuarioServicio.getOne(id));
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "Se modifico el usuario con exito!");
            this.listar(modelo);
            return "usuario_list.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
             this.listar(modelo);
            return "usuario_list.html";
        }
    }

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

//    
//    @GetMapping("/usuarios/modificar/{id]")
//    public String modificar(@PathVariable String id, ModelMap modelo){
//        modelo.put("usuario", usuarioServicio.getOne(id));
//        return "usuario_modificar.html";
//    }
}
