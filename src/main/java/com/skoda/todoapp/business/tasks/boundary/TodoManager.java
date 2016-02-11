/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skoda.todoapp.business.tasks.boundary;

import com.skoda.todoapp.business.tasks.entity.Todo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andrej
 */
@Stateless
public class TodoManager {

    @PersistenceContext
    EntityManager em;
    
    public Todo findById(long id) {
        return this.em.find(Todo.class, id);
    }
    
    public void delete(long id) {
        try {
            Todo reference = this.em.getReference(Todo.class, id);
            this.em.remove(reference);
        } catch (EntityNotFoundException e) {
            //we want to remove it, so this is no problem..
        }
    }
    
    public List<Todo> findAll() {
        return this.em.createNamedQuery(Todo.findAll, Todo.class).getResultList();
    }
    
    public List<Todo> findAllByUser(String userId) {
        return this.em.createNamedQuery(Todo.findAllByUser, Todo.class).setParameter("userId", userId).getResultList();
    }
    
    public Todo save(Todo todo) {
        return this.em.merge(todo); //merge works like upsert (insert or update)
    }

    public Todo updateStatus(long id, boolean done) {
        Todo todo = findById(id);
        if (todo == null) {
            return null;
        }
        todo.setDone(done);
        return todo;
    }
    
}
