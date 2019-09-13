package com.lambdaschool.todos.service;

import com.lambdaschool.todos.model.Quote;
import com.lambdaschool.todos.model.User;
import com.lambdaschool.todos.model.UserRoles;
import com.lambdaschool.todos.repository.RoleRepository;
import com.lambdaschool.todos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userrepos;

    @Autowired
    private RoleRepository rolerepos;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userrepos.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }

    @Transactional
    public User findUserById(long id) throws EntityNotFoundException {
        return userrepos.findById(id).orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(long id) {
        if (userrepos.findById(id).isPresent()) {
            userrepos.deleteById(id);
        } else {
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public User save(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPasswordNoEncrypt(user.getPassword());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        for (UserRoles ur : user.getUserRoles()) {
            newRoles.add(new UserRoles(newUser, ur.getRole()));
        }
        newUser.setUserRoles(newRoles);

        for (Quote q : user.getQuotes()) {
            newUser.getQuotes().add(new Quote(q.getQuote(), newUser));
        }

        return userrepos.save(newUser);
    }

    @Override
    public User findUserByName(String name) {
        User currentUser = userrepos.findByUsername(name);

        if (currentUser != null) {
            return currentUser;
        } else {
            throw new EntityNotFoundException(name);
        }
    }

    @Transactional
    @Override
    public User update(User user, long id) {
        User currentUser = userrepos.findById(id).orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));

        if (user.getUsername() != null) {
            currentUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null) {
            currentUser.setPasswordNoEncrypt(user.getPassword());
        }

        if (user.getUserRoles().size() > 0) {
            // with so many relationships happening, I decided to go
            // with old school queries
            // delete the old ones
            rolerepos.deleteUserRolesByUserId(currentUser.getUserid());

            // add the new ones
            for (UserRoles ur : user.getUserRoles()) {
                rolerepos.insertUserRoles(id, ur.getRole().getRoleid());
            }
        }

        if (user.getQuotes().size() > 0) {
            for (Quote q : user.getQuotes()) {
                currentUser.getQuotes().add(new Quote(q.getQuote(), currentUser));
            }
        }
        return userrepos.save(currentUser);
    }
}

