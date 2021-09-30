package com.example.userLogin.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor //faz com que nao precise criar os constructor
//especifica para spring security
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "USER WITH EMAIL %S NOT FOUND";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND, email)));
    }

    public String singUpUser(AppUser appUser){
        //Verifica se o usuario existe
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if (userExists){
            throw new IllegalStateException("Email already exists");
        }

        //criptografar o password
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
//Repassar a senha criptografada
        appUser.setPassword(encodedPassword);

//        Salvar o usuario
        appUserRepository.save(appUser);

        //TODO: SEND CONFIRMATION TOKEN
        return "";
    }
}
