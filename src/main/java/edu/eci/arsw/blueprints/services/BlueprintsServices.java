/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.filters.BlueprintsFilters;

import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {

    @Autowired
    BlueprintsPersistence bpp=null;

    @Autowired
    BlueprintsFilters bpf;

    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException {
        bpp.saveBlueprint(filterBluePrint(bp));
    }

    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        return bpf.filters(bpp.getAllBlueprint());
    }

    /**
     *
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{

        return filterBluePrint(bpp.getBlueprint(author,name));
    }

    public Blueprint filterBluePrint(Blueprint bp) throws BlueprintNotFoundException {

        return bpf.filter(bp);
    }

    /**
     *
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        return bpf.filters(bpp.getBlueprintsByAuthor(author));
    }

    public Set<Blueprint> filter(Set<Blueprint> blueprints) throws BlueprintNotFoundException{
        return bpf.filters(blueprints);
    }
}
