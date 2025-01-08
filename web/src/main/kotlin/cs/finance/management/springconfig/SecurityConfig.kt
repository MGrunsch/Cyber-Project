package cs.finance.management.springconfig

import cs.finance.management.business.security.MyUserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class SecurityConfig(
    private val userDetailService: UserDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity,
                            jwtAuthenticationFilter: JwtAuthenticationFilter,
                            authenticationProvider: AuthenticationProvider): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                // Endpunkte für alle freigeben
                auth.requestMatchers("/login", "/register", "/css/**", "/api/auth/login").permitAll()
                    .anyRequest().authenticated()
            }

            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)


            /*
            .formLogin { form ->
                form.loginProcessingUrl("api/auth/login")
                form.loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/login?error=true")
                    .permitAll()

            }

            .logout { logout ->
                logout.logoutSuccessUrl("/login?logout=true")
                    .permitAll()

            }

             */




        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder())
        return authenticationManagerBuilder.build()
    }

    /*
    @Bean
    fun filterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter,
                    authenticationProvider: AuthenticationProvider): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }*/



     */
    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter = JwtAuthenticationFilter()
}