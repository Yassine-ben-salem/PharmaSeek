package dtos;

import entities.Role;


public record UserDto(Long id, String email, Role role) {
}