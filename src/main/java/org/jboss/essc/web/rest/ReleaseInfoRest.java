package org.jboss.essc.web.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Release;

/**
 * @author ozizka@redhat.com
 */

@Path("/")
public class ReleaseInfoRest {
    
    @Inject private ReleaseDaoBean relDao; 

    
    @GET
    @Path("/release/{product}/{version}")
    @Produces("application/json")
    public Release getProductInfo( @PathParam("product") String product, @PathParam("project") String version, @Context SecurityContext sc ) {
        System.out.println("Release: " + product + " " + version);
        
        Release rel = relDao.getRelease(product, version);
        //return "{aaa:'bbb'}";
        return rel;
    }

}// class
