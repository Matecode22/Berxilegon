package edu.co.icesi.introspringboot.security;


import edu.co.icesi.introspringboot.entity.Permiso;
import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.PermisoRepository;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;


    // username = correo del usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Obteniendo la entidad Usuario
        Usuario usuario;
        try {
            usuario = usuarioService.findByCorreo(username);
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("Usuario " + username + " not found");
        }
        List<Rol> roles = rolRepository.findByUsuarioRoles_Usuario_Correo(username);
        List<Permiso> permisos = permisoRepository.findDistinctByRolPermisos_Rol_UsuarioRoles_Usuario_Correo(username);
        //List<Rol> -> List<SimpleGrantedAuthority>  (ROLE_ADMIN, ROLE_USUARIO...)
        List <SimpleGrantedAuthority> authoritiesRol = roles.stream().map(
                rol -> new SimpleGrantedAuthority(rol.getNombre())
        ).toList();

        //List<Permiso> -> List<SimpleGrantedAuthority>  (CREATE_USER, PUJAR...)
        List <SimpleGrantedAuthority> authoritiesPermiso = permisos.stream().map(
                permiso -> new SimpleGrantedAuthority(permiso.getNombre())
        ).toList();


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authoritiesRol.forEach(element -> authorities.add(element));
        authoritiesPermiso.forEach(element -> authorities.add(element));


        // getAuthorities() del UserDetails devuelve los roles y permisos
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getCorreo())
                .password(usuario.getContrasena()) // hash BCrypt guardado en la base
                .authorities(authorities)
                .disabled(!"ACTIVO".equals(usuario.getEstado())) // un usuario INACTIVO no inicia sesion
                .build();
    }
}
