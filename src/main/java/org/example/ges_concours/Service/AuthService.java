package org.example.ges_concours.Service;
import lombok.RequiredArgsConstructor;
import org.example.ges_concours.Dto.AuthResponse;
import org.example.ges_concours.Dto.LoginRequest;
import org.example.ges_concours.Dto.RegisterRequest;
import org.example.ges_concours.Dto.UtilisateurDto;
import org.example.ges_concours.Entity.Utilisateur;
import org.example.ges_concours.Entity.Utilisateur.Role;
import org.example.ges_concours.Repository.AuthInterface;
import org.example.ges_concours.Repository.UtilisateurRepository;
import org.example.ges_concours.Security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone())
                .role(Role.CANDIDAT)
                .build();

        utilisateurRepository.save(utilisateur);

        return buildAuthResponse(utilisateur);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides"));

        return buildAuthResponse(utilisateur);
    }

    private AuthResponse buildAuthResponse(Utilisateur utilisateur) {

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(utilisateur.getEmail())
                        .password(utilisateur.getMotDePasse())
                        .authorities("ROLE_" + utilisateur.getRole().name())
                        .build()
        );

        UtilisateurDto userDto = UtilisateurDto.builder()
                .id(utilisateur.getId())
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .telephone(utilisateur.getTelephone())
                .role(utilisateur.getRole().name())
                .build();

        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }
}