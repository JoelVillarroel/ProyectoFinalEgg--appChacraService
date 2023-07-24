/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.GrupoE.WebAppServicios;

import com.GrupoE.WebAppServicios.servicios.ProveedorServicio;
import com.GrupoE.WebAppServicios.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @Autowired
    public ProveedorServicio proveedorServicio;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
                auth.userDetailsService(proveedorServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
    
    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http
                .authorizeRequests()
                    .antMatchers("/login", "/logincheck").permitAll()
                    .antMatchers("/css/*","/js/*","/img/*","/**")
                    .permitAll()
                    .antMatchers("/inicio").hasAnyRole("USER", "PROVEEDOR") // Permite el acceso a /inicio solo si el usuario tiene el rol USER o PROVIDER
                    .anyRequest().authenticated()
                .and().formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/inicio")
                        .permitAll()
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("http://www.google.com")
                    .permitAll()
                .and().csrf()
                        .disable();
    }
    
    
}
