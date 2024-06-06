package org.boot.registersession.service;

import org.boot.registersession.exception.user.UserAlreadyExistsExceptionClient;
import org.boot.registersession.exception.user.UserAuthenticationResponse;
import org.boot.registersession.exception.user.UserLoginRequestBody;
import org.boot.registersession.exception.user.UserNotFoundExceptionClient;
import org.boot.registersession.model.entity.UserEntity;
import org.boot.registersession.model.user.User;
import org.boot.registersession.model.user.UserSignUpRequestBody;
import org.boot.registersession.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserEntityRepository userEntityRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserEntityByUsername(username);
    }

    public User signUp(UserSignUpRequestBody userSignUpRequestBody) {
        userEntityRepository.findByUsername(userSignUpRequestBody.username())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsExceptionClient();
                });

        var userEntity = userEntityRepository.save(
                UserEntity.of(
                        userSignUpRequestBody.username(),
                        passwordEncoder.encode(userSignUpRequestBody.password()),
                        userSignUpRequestBody.name(),
                        userSignUpRequestBody.email()
                )
        );

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(UserLoginRequestBody userLoginRequestBody) {
        UserEntity UserEntity = getUserEntityByUsername(userLoginRequestBody.username());

        if(passwordEncoder.matches(userLoginRequestBody.password(), UserEntity.getPassword())) {
            String accessToken = jwtService.generateAccessToken(UserEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundExceptionClient();
        }
    }

    private UserEntity getUserEntityByUsername(String username) {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
