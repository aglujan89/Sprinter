/*
 */
package com.egg.Biblioteca.servicios;

import com.egg.Biblioteca.entidades.Usuario;
import com.egg.Biblioteca.enumeraciones.Rol;
import com.egg.Biblioteca.excepciones.MiException;
import com.egg.Biblioteca.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author fitog
 */

@Service
public class UsuarioServicio implements UserDetailsService{

@Autowired
private UsuarioRepositorio usuarioRepositorio;

@Transactional
public void registrar(String nombre, String email, String password, String password2) throws MiException, ValidationException {
    
    validar(nombre, email, password, password2);
    
    Usuario usuarioExistente = usuarioRepositorio.buscarPorEmail(email);
    if (usuarioExistente != null) {
            throw new ValidationException("Ya existe un usuario registrado con ese email");
        }
    
    Usuario usuario = new Usuario();
    usuario.setNombre(nombre);
    usuario.setEmail(email);
    
    usuario.setPassword(new BCryptPasswordEncoder().encode(password));
    
    usuario.setRol(Rol.USER);
    
    usuarioRepositorio.save(usuario);
    
}

private void validar(String nombre, String email, String password, String password2) throws MiException{
    if (nombre.isEmpty() || nombre == null) {
        throw new MiException("El nombre no puede ser nulo o estrar vacio.");
    }
    if (email.isEmpty() || email == null) {
        throw new MiException("El email no puede ser nulo o estar vacio.");
    }
    if (password.isEmpty() || password == null || password.length() <= 5) {
        throw new MiException("El contraseña no puede estar vacía, y debe tener más de 5 dígitos");
    }
    if (!password.equals(password2)){
        throw new MiException("La contraseña ingresada deben ser iguales");
    }
}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
       Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
       
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString());
            
            permisos.add(p);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("usuariosession",usuario);
                    
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }else{
            return null;
        }
    }
    
}