/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.controllers.dao.BlueprintBody;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {
    
    @Autowired
    BlueprintsServices services;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get() {
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(services.getAllBlueprints(),HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("No se ha encontrado",HttpStatus.NOT_FOUND);
        }  
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{author}")
    public ResponseEntity<?> getByAuthor(@PathVariable String author) {
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(services.getBlueprintsByAuthor(author),HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error 404 Blueprint not found",HttpStatus.NOT_FOUND);
        }  
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/{author}/{bpname}")
    public ResponseEntity<?> getByAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(services.getBlueprint(author, bpname),HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error 404 Blueprint not found",HttpStatus.NOT_FOUND);
        }  
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<?> create(@RequestBody BlueprintBody body) {
        try {
            Point[] pts = new Point[]{new Point(body.getX1(),body.getY1()),new Point(body.getX2(), body.getY2())};
            Blueprint bp = new Blueprint(body.getAuthor(), body.getName(), pts);
            services.addNewBlueprint(bp);
            
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Could not create blueprint",HttpStatus.BAD_REQUEST);
        }  
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{author}/{bpname}")
    public ResponseEntity<?> update(@RequestBody BlueprintBody body, @PathVariable String author, @PathVariable String bpname) {
        try {
            services.updateBlueprint(author, bpname, body);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Could not update blueprint",HttpStatus.BAD_REQUEST);
        }  
    }


    
}

