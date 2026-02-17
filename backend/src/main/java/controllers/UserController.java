package controllers;


import entities.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import repositories.UserRepository;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    @GetMapping
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
}
