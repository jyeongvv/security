package io.playdata.security.config;

import io.playdata.security.login.model.AccountDTO;
import io.playdata.security.login.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configurable // 설정을 등록해주는 어노테이션
@EnableWebSecurity // 웹 보안 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountRepository accountRepository; // **

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(
                // username을 사용한 람다식
          username -> {
              AccountDTO accountDTO = accountRepository.findByUsername(username);
              // 특정한 유저가 존재하는지 검증
              if (accountDTO != null) { // 유저가 존재한다면 // **
                  return User.withUsername(accountDTO.getUsername()) // **
                          .password(accountDTO.getPassword()) // **
                          .roles(accountDTO.getRole()) // **
                          .build(); // spring security에서 관리하는 유저를 생성 (세션에 들어감)
              } else {
                  throw new UsernameNotFoundException("해당 유저가 없습니다");
              }
          }).passwordEncoder(passwordEncoder()); // 암호를 복호화 (BCrypt)
        // 암호가 그대로 저장되면 해킹의 문제가 있어 알아보지 못하게 변형
        // 암호를 DB 저장
    }

    @Bean // spring에 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // -------------------------------------------------
    // <- spring security를 사용하기 위한 사전작업
    // -------------------------------------------------

    // configure -> http 요청이 들어왔을 때의 설정 (위는 검증요청)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .antMatchers("/login").permitAll() // 인증없이 로그인페이지 사용 가능
                .antMatchers("/register").permitAll()
                .antMatchers("/contents/basic").hasAnyRole("premium", "premium")
                .antMatchers("/contents/premium").hasRole("premium")
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
                .and()
                .formLogin() // form 사용해서 로그인
                .loginPage("/login") // 로그인 페이지 url
                .defaultSuccessUrl("/home") // 로그인 성공 후 이용할 url
                .permitAll()
                // logout
                .and()
                .logout()
                .logoutUrl("/logout") // logout url
                .logoutSuccessUrl("/login?logout") // logout 성공 시 이동할 페이지
                .invalidateHttpSession(true) // logout 시 세션 무효화
                .deleteCookies("JSESSIONID") // logout 시 삭제할 쿠키 이름 지정
                .permitAll();
    }
}
