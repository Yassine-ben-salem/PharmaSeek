package services;

import config.JwtConfig;
import dtos.*;
import entities.Client;
import entities.Pharmacy;
import entities.Role;
import entities.User;
import exceptions.CinAlreadyInUseException;
import exceptions.EmailAlreadyInUseException;
import exceptions.MatriculeFiscaleAlreadyInUseException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mappers.ClientMapper;
import mappers.PharmacyMapper;
import mappers.UserMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientRepository;
import repositories.PharmacyRepository;
import repositories.UserRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@AllArgsConstructor

public class AuthService {
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;


    public JwtResponse generateAuthResponse(User user, HttpServletResponse response) {
        // Generate tokens
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        clearLegacyRefreshCookies(response);

        // Create and configure cookie
        var cookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(cookie);

        // Return response
        return new JwtResponse(
                accessToken,
                userMapper.toUserDto(user)
        );
    }


    private Cookie createRefreshTokenCookie(String refreshToken) {
        var cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtConfig.getRefreshTokenValidity() / 1000));
        cookie.setSecure(jwtConfig.isSecureCookie());
        cookie.setAttribute("SameSite", jwtConfig.getSameSite());
        return cookie;
    }


    private void clearLegacyRefreshCookies(HttpServletResponse response) {
        Cookie legacyRoot = new Cookie(REFRESH_COOKIE_NAME, "");
        legacyRoot.setHttpOnly(true);
        legacyRoot.setPath("/");
        legacyRoot.setMaxAge(0);
        response.addCookie(legacyRoot);

        Cookie legacyAuth = new Cookie(REFRESH_COOKIE_NAME, "");
        legacyAuth.setHttpOnly(true);
        legacyAuth.setPath("/auth");
        legacyAuth.setMaxAge(0);
        response.addCookie(legacyAuth);
    }

    public User validateRefreshToken(String refreshToken) {
        var claims = jwtService.parseToken(refreshToken);
        if (claims == null || claims.getExpiration().before(new Date())) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }
        Long userId = Long.parseLong(claims.getSubject());
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }


    @Transactional
    public ClientDto registerClient(RegisterClientRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException();
        }
        if (clientRepository.existsByCin(request.getCin())) {
            throw new CinAlreadyInUseException();
        }

        Instant persistedAt = Instant.now().plus(1, ChronoUnit.HOURS);

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CLIENT);
        user.setEnabled(Boolean.TRUE);
        user.setCreatedAt(persistedAt);
        user.setUpdatedAt(persistedAt);
        User savedUser = userRepository.save(user);

        Client client = new Client();
        client.setUsers(savedUser);
        client.setFullName(request.getName());
        client.setCin(request.getCin());
        client.setPhone(request.getPhone());
        client.setCreatedAt(persistedAt);
        Client savedClient = clientRepository.save(client);

        return clientMapper.toClientDto(savedClient);
    }

    @Transactional
    public PharmacyDto registerPharmacy(RegisterPharmacyRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        if (pharmacyRepository.existsByMatriculeFiscale(request.getMatriculeFiscale())) {
            throw new MatriculeFiscaleAlreadyInUseException();
        }

        Instant persistedAt = Instant.now().plus(1, ChronoUnit.HOURS);

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PHARMACY);
        user.setEnabled(Boolean.TRUE);
        user.setCreatedAt(persistedAt);
        user.setUpdatedAt(persistedAt);
        User savedUser = userRepository.save(user);
        Pharmacy pharmacy = getPharmacy(request, savedUser, persistedAt);
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);
        return pharmacyMapper.toPharmacyDto(savedPharmacy);
    }

    private static @NonNull Pharmacy getPharmacy(RegisterPharmacyRequest request, User savedUser, Instant persistedAt) {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setUsers(savedUser);
        pharmacy.setPharmacyName(request.getPharmacyName());
        pharmacy.setMatriculeFiscale(request.getMatriculeFiscale());
        pharmacy.setAddress(request.getAddress());
        pharmacy.setLatitude(request.getLatitude() != null ? request.getLatitude() : BigDecimal.ZERO);
        pharmacy.setLongitude(request.getLongitude() != null ? request.getLongitude() : BigDecimal.ZERO);
        pharmacy.setPhone(request.getPhone());
        pharmacy.setVerified(Boolean.FALSE);
        pharmacy.setCreatedAt(persistedAt);
        return pharmacy;
    }


    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(jwtConfig.isSecureCookie());
        response.addCookie(cookie);
    }
}
