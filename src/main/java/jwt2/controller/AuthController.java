package jwt2.controller;

import jakarta.validation.Valid;
import jwt2.DTO.AuthenticationDTO;
import jwt2.DTO.PersonDTO;
import jwt2.model.Person;
import jwt2.security.JWTTokenGeneration;
import jwt2.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
//@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTTokenGeneration jwtTokenGeneration;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, JWTTokenGeneration jwtTokenGeneration,
                          ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtTokenGeneration = jwtTokenGeneration;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String getLogin(){
        return "auth_login";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "auth_register";
    }

//    @PostMapping("/finishRegistration")
//    public String finishRegistration(@ModelAttribute("personDTO") PersonDTO personDTO){
//        authService.savePerson(convertDTOtoModel(personDTO));
//        return "redirect: /auth/login";
//    }

    @PostMapping("/finishRegistration")
    public Map<String, String> finishRegistration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        Person person = convertDTOtoModel(personDTO);

//        if (bindingResult.hasErrors())
//            return Map.of("message", "Error with registration of a new Person !");

        authService.savePerson(person);
        String token = jwtTokenGeneration.generateToken(person.getUsername());

        return Map.of("jwt-token", token);
    }

    @PostMapping("/renewToken")
    public Map<String, String> renewToken(@RequestBody AuthenticationDTO authDTO){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authDTO.getUsername(),
                authDTO.getPassword()); // UsernamePasswordAuthenticationToken стандартный класс для инкапсуляции
                                        // логина-пароля в Spring Security
        // AuthenticationManager позволяет автоматом проводить аутентификацию через UsernamePasswordAuthenticationToken
        try {
            authenticationManager.authenticate(authToken);              // если ошибка не выбрасывается то далее создадим
        } catch (BadCredentialsException e) {                           // новый токен
            return Map.of("message", "Incorrect Credentials");
        }

        String newToken = jwtTokenGeneration.generateToken(authDTO.getUsername());
        return Map.of("new jwt Token", newToken);
    }

    private Person convertDTOtoModel(PersonDTO personDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(personDTO, Person.class);
    }
}