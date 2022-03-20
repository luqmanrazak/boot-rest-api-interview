package posmy.interview.boot.service;

import posmy.interview.boot.model.User;

public interface UserService{
    User findUserByUsername(String username);

    User saveUser(User user);

    Long registerUser(User user);
}