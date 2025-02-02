package gzg.momen.todolist.service;


import gzg.momen.todolist.auth.AuthenticationRequest;
import gzg.momen.todolist.auth.AuthenticationResponse;
import gzg.momen.todolist.dto.UserDTO;
import gzg.momen.todolist.entity.User;
import gzg.momen.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, AuthenticationManager authManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    public AuthenticationResponse createUser(UserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse verify(AuthenticationRequest user) {
        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail()
                        , user.getPassword()));

        if(auth.isAuthenticated()) {
            return new AuthenticationResponse(jwtService.generateToken(user.getEmail()));
        }
        return null;
    }
}
