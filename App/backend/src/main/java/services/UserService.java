package services;

import dtos.UserDto;
import entities.Role;
import entities.Roles;
import exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import mappers.UserMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.RoleRepository;
import repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public UserDto getUserByIdForRequester(Long requestedUserId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            Long authenticatedUserId = resolveAuthenticatedUserId(authentication);
            if (!requestedUserId.equals(authenticatedUserId)) {
                throw new AccessDeniedException("You can only access your own user record.");
            }
        }

        var user = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new UserNotFoundException(requestedUserId));

        return userMapper.toUserDto(user);
    }

    public List<UserDto> getUsersByRole(String roleValue) {
        Roles role;
        try {
            role = Roles.valueOf(roleValue.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role: " + roleValue + ". Allowed values: ADMIN, CLIENT, PHARMACY.");
        }

        return userRepository.findByRole(role).stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Transactional
    public UserDto updateUserRole(Long userId, String roleValue) {
        Roles requestedRole;
        try {
            requestedRole = Roles.valueOf(roleValue.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role: " + roleValue + ". Allowed values: ADMIN, CLIENT, PHARMACY.");
        }

        Role role = roleRepository.findByCode(requestedRole.name())
                .orElseThrow(() -> new IllegalStateException("Role seed data is missing for: " + requestedRole.name()));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setRoles(new HashSet<>(Set.of(role)));
        var savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
    }

    private Long resolveAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        if (principal instanceof String userIdValue) {
            try {
                return Long.parseLong(userIdValue);
            } catch (NumberFormatException ex) {
                throw new AccessDeniedException("Invalid authenticated user identity.");
            }
        }

        throw new AccessDeniedException("Invalid authenticated user identity.");
    }
}
