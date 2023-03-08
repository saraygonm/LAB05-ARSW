package edu.eci.arsw.blueprints.filters;

import java.util.*;

import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.*;
import org.springframework.stereotype.Component;

@Component("RedundanceFilter")
public class RedundanceFilter implements Filter{
    
    @Override
    public void filter(Blueprint bp) {
        List<Point> pts0 = bp.getPoints();        
        ArrayList<Point> ps1 = new ArrayList();
        for(int i=0;i<pts0.size()-1;i++){
            if(pts0.get(i).getX()!=pts0.get(i+1).getX() &pts0.get(i).getY()!=pts0.get(i+1).getY() ){
                ps1.add(pts0.get(i));
            }
        }
        ps1.add(pts0.get(pts0.size()-1));
        bp.setPoints(ps1); 
    }
}
