package com.lambdaschool.todos.repository;

import com.lambdaschool.todos.model.Quote;
import com.lambdaschool.todos.views.CountQuotes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface QuoteRepository extends CrudRepository<Quote, Long> {
    @Query(value = "SELECT u.username as username, COUNT(*) as countquotes FROM quotes q JOIN users u ON q.userid = u.userid GROUP BY u.username", nativeQuery = true)
    ArrayList<CountQuotes> getCountQuotes();
}
