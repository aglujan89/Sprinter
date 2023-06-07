/*
 */
package com.egg.Biblioteca.controladores;

import com.egg.Biblioteca.entidades.Usuario;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
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
@RequestMapping("/")
public class PortalControlador {
 
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index(){
    return "index.html";
}   
    
    @GetMapping("/registrar")
    public String registrar(){
        return "registro.html";
    }
       
    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelo) throws MiException, ValidationException{
        try {
            usuarioServicio.registrar(archivo, nombre, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente!");
            return "index.html";
        }  catch (MiException | ValidationException ex) {
        modelo.put("error", ex.getMessage());
        modelo.put("nombre", nombre);
        modelo.put("email",email);
        return "registro.html";
        }
    }
    @GetMapping("/login")
    public String login(@RequestParam(required=false) String error, ModelMap modelo){
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }
        return "login.html";
    } 
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session){
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMIN")){
         return "redirect:/admin/dashboard";   
        }
        return "inicio.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/perfil")
public String miPerfil (HttpSession session, ModelMap modelo){
    Usuario usuario = (Usuario) session.getAttribute("usuariosession");
    modelo.put("usuario", usuario);
    return "perfil_usuario.html";
}
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
public String actualizarPerfil (HttpSession session, MultipartFile archivo,@PathVariable String id, @RequestParam String nombre,@RequestParam String email, 
            @RequestParam String password,@RequestParam String password2, ModelMap modelo) {

        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "Se actualizo el usuario con exito!");
            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return miPerfil (session, modelo);
        }

    }
    
}
