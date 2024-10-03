package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/blueprints")

public class BlueprintAPIController {

    @Autowired
    private BlueprintsServices bs;

    /**
     *  Manejo de solicitudes HTTP GET que intentan obtener todos los blueprints almacenados en el sistema.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBlueprints() {
        try {
            Set<Blueprint> blueprints = bs.getAllBlueprints();
            return new ResponseEntity<>(blueprints, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al obtener los blueprints", HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  Recuperar todos los blueprints creados por un autor específico, y maneja de forma adecuada
     *  la posibilidad de que el autor no tenga blueprints almacenados
     */

    @GetMapping(path = "/{author}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> blueprints = bs.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(blueprints, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El autor: " + author + " no existe, error 404", HttpStatus.NOT_FOUND);
        }
    }

    /**
     *   Este métdo proporciona un endpoint para recuperar un blueprint específico basándose en el autor y el nombre.
     *   Maneja adecuadamente los errores si el recurso no es encontrado
     */
    @GetMapping(path = "/{author}/{bpname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlueprintByAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = bs.getBlueprint(author, bpname);
            return new ResponseEntity<>(blueprint, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El autor: " + author + " o el nombre del plano: " + bpname + " no coincide, error 404", HttpStatus.NOT_FOUND);
        }
    }



    /**
     *
     * Metodo que proporciona un endpoint REST que permite a los clientes buscar un blueprint por su autor y nombre,
     * manejando adecuadamente los errores si el recurso no es encontrado.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBlueprint(@RequestBody Blueprint blueprint) {
        try {
            bs.addNewBlueprint(blueprint);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("No se ha logrado registrar el blueprint", HttpStatus.FORBIDDEN);
        } catch (BlueprintNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/{author}/{bpname}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint blueprint) {
        try {
            Blueprint blueprintToUpdate = bs.getBlueprint(author, bpname);
            blueprintToUpdate.setAuthor(blueprint.getAuthor());
            blueprintToUpdate.setName(blueprint.getName());
            blueprintToUpdate.setPoints(blueprint.getPoints());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El plano no existe, error 404", HttpStatus.NOT_FOUND);
        }
    }
}
