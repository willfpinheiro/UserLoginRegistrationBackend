package com.example.userLogin.appuser;

import com.example.userLogin.registration.token.ConfirmationToken;
import com.example.userLogin.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

//claase criada para fornecer a interface para o usuario
@Service
@AllArgsConstructor//faz com que nao precise criar os constructor //implements UserDeta... especifica para spring security
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "USER WITH EMAIL %s NOT FOUND";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND, email)));
    }

    public String singUpUser(AppUser appUser) {
        //Verifica se o usuario existe
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists) {
            //TODO check of attributes are the same and
            //TODO if email not confirmed send confirmation email
            throw new IllegalStateException("Email already exists");
        }

        //criptografar o password
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        //Repassar a senha criptografada
        appUser.setPassword(encodedPassword);

        //Salvar o usuario
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        //SEND CONFIRMATION TOKEN
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        //TODO Send email
        return token;
    }

    public AppUser enableAppUser(String email) {
        Optional<AppUser> optionalAppUser = this.appUserRepository.findByEmail(email);
        if (optionalAppUser.isEmpty()){}
        AppUser appUser = optionalAppUser.get();
        appUser.setEnabled(true);
        return appUserRepository.save(appUser);
    }
}
