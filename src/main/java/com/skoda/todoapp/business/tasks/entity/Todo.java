/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skoda.todoapp.business.tasks.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andrej
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Todo.findAll, query = "SELECT t from Todo t"),
    @NamedQuery(name = Todo.findAllByUser, query = "SELECT t from Todo t where t.userId= :userId")
})
@XmlRootElement // transferable over the network
@XmlAccessorType(XmlAccessType.FIELD) // I don't like getters and setters
public class Todo {

    public static final String PREFIX = "tasks.entity.Todo.";
    public static final String findAll = PREFIX + "findAll";
    public static final String findAllByUser = PREFIX + "findAllByUser";
    
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    @Size(min = 2, max = 256)
    private String description;
    private int priority;
    private boolean done;
    private String userId;
    
    //the JPA update will only be succsessfull if the version matches
    @Version
    private long version;

    public Todo(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    public Todo(String userId) {
        this.userId = userId;
    }
    
    public Todo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
