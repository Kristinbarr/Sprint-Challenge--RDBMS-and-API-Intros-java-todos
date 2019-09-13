package com.lambdaschool.todos.repository;

import com.lambdaschool.todos.model.Todo;
import com.lambdaschool.todos.views.CountTodos;
import com.lambdaschool.todos.views.UserAndTodos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    @Query(value = "SELECT u.username as username, COUNT(*) as counttodos FROM quotes q JOIN users u ON q.userid = u.userid GROUP BY u.username", nativeQuery = true)
    ArrayList<CountTodos> getCountTodos();

    @Query(value = "SELECT * FROM USERS WHERE USERNAME = 'barnbarn'", nativeQuery = true)
    ArrayList<UserAndTodos> getUserAndTodos();
}
