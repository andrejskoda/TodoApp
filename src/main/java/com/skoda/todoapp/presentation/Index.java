/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skoda.todoapp.presentation;

import com.skoda.todoapp.business.tasks.boundary.TodoManager;
import com.skoda.todoapp.business.tasks.entity.Todo;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author andrej
 */
@Model
public class Index {
    
    @Inject
    private TodoManager boundary;
    
    @Inject
    private Validator validator;
    
    @Inject 
    private Identity identity;
    
    Todo todo;
    
    List<Todo> todos;
    
    boolean checkAll;
    
    @PostConstruct
    public void init() {
        String loginName = getUser().getLoginName();
        this.todo = new Todo(loginName);
        this.todos = this.boundary.findAllByUser(loginName);
    }
    
    private User getUser() {
        return (User) identity.getAccount();
    }

    public Todo getTodo() {
        return todo;
    }
    
    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public boolean isCheckAll() {
        return checkAll;
    }

    public void setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
    }
    
    public void showValidationError(String s){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, s, s);
        FacesContext.getCurrentInstance().addMessage("", message);
    }
    
    public Object save() {
        Set<ConstraintViolation<Todo>> violations = this.validator.validate(this.todo);
        violations.stream().map(violation -> violation.getMessage()).forEach(this::showValidationError);
        if (violations.isEmpty()) {
            this.boundary.save(todo);
            init();//reset form
        }
        return null;
    }
    
    public void onRowEdit(RowEditEvent event) {
        Todo todo = (Todo) event.getObject();
        this.boundary.save(todo);
        init();
        FacesMessage msg = new FacesMessage("Row Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void changeStatus(String id, boolean value) {

        this.boundary.updateStatus(Long.valueOf(id), value);
        init();

        FacesMessage msg = new FacesMessage(value ? "Finished" : "Not finished");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void changeAllStatuses(boolean value) {
        getTodos().stream().forEach(todo ->{
            this.boundary.updateStatus(todo.getId(), value);
        });
        init();
        FacesMessage msg = new FacesMessage((value ? "Finished all" : "Unfinished all"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void deleteCompletedTasks() {
        getTodos().stream().
                filter(t -> t.isDone()).
                mapToLong(t -> t.getId()).
                forEach(id -> this.boundary.delete(id));
        init();
        FacesMessage msg = new FacesMessage("Deleted all complete tasks");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public boolean hasAnyCompletedTask(){
        boolean result = getTodos().stream().anyMatch(t -> t.isDone());
        return result;
    }
    
    public void deleteTask(String id) {

        this.boundary.delete(Long.valueOf(id));
        init();

        FacesMessage msg = new FacesMessage("Deleted");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public String getRequestURL()
{
    Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
    if(request instanceof HttpServletRequest)
    {
            return ((HttpServletRequest) request).getRequestURL().toString();
    }else
    {
        return "";
    }
}
    
}
