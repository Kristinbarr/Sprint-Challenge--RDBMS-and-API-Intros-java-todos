package com.lambdaschool.todos.controller;

import com.lambdaschool.todos.model.Todo;
import com.lambdaschool.todos.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodosController {
    @Autowired
    TodoService todoService;

    @GetMapping(value = "/todos", produces = {"application/json"})
    public ResponseEntity<?> listAllTodos() {
        List<Todo> allTodos = todoService.findAll();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    @GetMapping(value = "/todo/{todoId}",
            produces = {"application/json"})
    public ResponseEntity<?> getTodo(@PathVariable Long todoId) {
        Todo q = todoService.findTodoById(todoId);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }


    @GetMapping(value = "/username/{userName}",
            produces = {"application/json"})
    public ResponseEntity<?> findTodoByUserName(
            @PathVariable String userName) {
        List<Todo> theTodos = todoService.findByUserName(userName);
        return new ResponseEntity<>(theTodos, HttpStatus.OK);
    }


    @PostMapping(value = "/todo")
    public ResponseEntity<?> addNewTodo(@Valid
                                        @RequestBody
                                                Todo newTodo) throws URISyntaxException {
        newTodo = todoService.save(newTodo);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newTodoURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{todoid}").buildAndExpand(newTodo.getTodoid()).toUri();
        responseHeaders.setLocation(newTodoURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    // PUT - localhost:8000/todos/todo/{todoId} -  updates a todo based on todoid
    @PutMapping(value = "/todo/{todoid}")
    public ResponseEntity<?> updateTodo(@RequestBody Todo updateTodo, @PathVariable long todoid) {
        todoService.update(updateTodo, todoid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteTodoById(
            @PathVariable long id) {
        todoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
