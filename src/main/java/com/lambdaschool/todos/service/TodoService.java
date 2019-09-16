package com.lambdaschool.todos.service;

import com.lambdaschool.todos.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> findAll();

    Todo findTodoById(long id);

    Todo findTodoByName(String name);

    List<Todo> findByUserName(String username);

    void delete(long id);

    Todo save(Todo todo, long userid);

    Todo update(Todo todo, long id);

}
