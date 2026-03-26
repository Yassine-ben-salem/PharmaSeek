package controllers;

import dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import services.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name="Authentication")
public class AuthController {
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
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> current(Authentication authentication) {
        return ResponseEntity.ok(authService.getCurrentUser(authentication));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }
}