/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skoda.todoapp.authentication.form;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

/**
 *
 * @author andrej
 */
@Singleton
@Startup
public class SecurityInitializer {
    @Inject
    private PartitionManager partitionManager;
    

    @PostConstruct
    public void create() {
        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        User jane = createUser("1", "jane", "jane@doe.com", "Jane", "Doe");
        identityManager.add(jane);
        identityManager.updateCredential(jane, new Password("jane123"));
        
        User bob = createUser("2", "bob", "bob@doe.com", "Bob", "Doe");
        identityManager.add(bob);
        identityManager.updateCredential(bob, new Password("bob123"));
        
    }
    
    private User createUser(String id, String loginName, String email, String firstName, String lastName) {
        User user = new User();
        user.setLoginName(loginName);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setId(id);
        return user;
    }
}
