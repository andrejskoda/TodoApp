/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skoda.todoapp.authentication.form;

import javax.enterprise.event.Observes;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

/**
 *
 * @author andrej
 */
public class HttpSecurityConfiguration {

    public void onInit(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();

        builder.http()
                .allPaths()
                .authenticateWith()
                .form()
                .authenticationUri("/faces/login.xhtml")
                .loginPage("/faces/login.xhtml")
                .errorPage("/faces/login.xhtml")
                .restoreOriginalRequest()
                .forPath("/logout")
                .logout()
                .redirectTo("/faces/index.xhtml");
    }
}
