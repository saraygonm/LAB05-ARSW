package edu.eci.arsw.blueprints.filters;

import java.util.*;

import edu.eci.arsw.blueprints.model.*;
import org.springframework.stereotype.Component;

@Component("SubmuestreoFilter")
public class SubmuestreoFilter implements Filter{

    @Override
    public void filter(Blueprint bp) {
        List<Point> pts0 = bp.getPoints();
        int i=0;
        ArrayList<Point> ps1 = new ArrayList();
        for(Point p:pts0){               
            if(i%2!=0){                
                ps1.add(p);
            }
            i++;
        }        
        bp.setPoints(ps1);
    }
}
