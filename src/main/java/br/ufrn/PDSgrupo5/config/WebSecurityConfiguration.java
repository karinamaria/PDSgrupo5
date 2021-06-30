package br.ufrn.PDSgrupo5.config;

import br.ufrn.PDSgrupo5.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.handler.AutenticacaoSucessoHandler;
import br.ufrn.PDSgrupo5.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    private AutenticacaoSucessoHandler autenticacaoSucessoHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        secureLogin(http);
        http.authorizeRequests()
                .antMatchers("paciente/form").permitAll()
                .antMatchers("paciente/salvar").permitAll()
                .antMatchers("paciente/editarOutroUsuario/*").hasAnyAuthority(EnumTipoPapel.VALIDADOR.getDescricao());
                //.anyRequest().hasAnyAuthority(EnumTipoPapel.VALIDADOR.getDescricao());
    }

    private void secureLogin(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/login").successHandler(autenticacaoSucessoHandler)
                .permitAll()
                .and()
                .logout().logoutSuccessUrl(pageLogoutSucess())
                .permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    protected String pageLogoutSucess() {
        return "/";
    }

}