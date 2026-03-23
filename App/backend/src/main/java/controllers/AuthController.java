package controllers;

import dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import repositories.UserRepository;
import services.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name="Authentication")
public class AuthController {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/signup/client")
    @Operation(summary = "Signup a Client")
    public ResponseEntity<ClientDto> registerClient(@RequestBody @Valid RegisterClientRequest request) {
        var clientDto = authService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientDto);
    }

    @PostMapping("/signup/pharmacy")
    public ResponseEntity<PharmacyDto> registerPharmacy(@RequestBody @Valid RegisterPharmacyRequest request) {
        var pharmacyDto = authService.registerPharmacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid LoginRequestBody request,
            HttpServletResponse response
    ) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Get user
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // Generate tokens and response
        var jwtResponse = authService.generateAuthResponse(user, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Missing refresh token");
        }

        // Validate refresh token and get user
        var user = authService.validateRefreshToken(refreshToken);

        // Generate new tokens and response
        var jwtResponse = authService.generateAuthResponse(user, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> current() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user ID from authentication principal
        var principal = authentication.getPrincipal();
        if (!(principal instanceof Long userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }
}