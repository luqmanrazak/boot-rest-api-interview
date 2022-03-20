package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.exception.UserNotFoundException;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    List<User> findAll() {
        return repository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @GetMapping("/users/{username}")
    User findOne(@PathVariable String username) {
        return Optional.of(repository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @PutMapping("/users/{id}")
    User saveOrUpdate(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @RequestMapping("/users/register")
    @ResponseBody
    public Long register(@RequestBody User user) {
        return userService.registerUser(user);
    }

}
