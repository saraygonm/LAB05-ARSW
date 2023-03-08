/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.controllers.dao.BlueprintBody;
import edu.eci.arsw.blueprints.filters.Filter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.websocket.pojo.PojoPathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service("BlueprintsServices")
public class BlueprintsServices {
   
    @Autowired
    @Qualifier("InMemoryBlueprintPersistence")
    BlueprintsPersistence bpp;

    @Autowired
    @Qualifier("RedundanceFilter")
    Filter filter;
    
    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException{
        bpp.saveBlueprint(bp);
    }
    
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException{
        return bpp.getAllBlueprints();
    }
    
    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
        Blueprint bpt = bpp.getBlueprint(author, name);
        filter.filter(bpt);
        return bpp.getBlueprint(author, name);
    }
    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        return bpp.getBlueprintsByAuthor(author);
    }

    public void updateBlueprint(String author, String name, BlueprintBody body) throws BlueprintNotFoundException{
        if(body.getX1() == null ||
           body.getX2() == null ||
           body.getY1() == null ||
           body.getY2() == null) return;

        Blueprint bpt = bpp.getBlueprint(author, name);

        ArrayList<Point> pts = new ArrayList<>();
        pts.add(new Point(body.getX1(), body.getY1()));
        pts.add(new Point(body.getX2(), body.getY2()));
        bpt.setPoints(pts);
    }

    
}
