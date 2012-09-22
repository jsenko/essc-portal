package org.jboss.essc.web.rest;

import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;

/**
 * @author ozizka@redhat.com
 */

@Path("/")
public class ReleaseInfoRest {
    
    @Inject private ReleaseDaoBean relDao; 
    @Inject private ProductDaoBean prodDao; 


    @GET
    @Path("/products")
    @Produces("application/json")
    public List<Product> getProducts( @Context SecurityContext sc ) {
        List<Product> rel = prodDao.getProducts_orderName(0);
        return rel;
    }
    
    @GET
    @Path("/releases/{product}")
    @Produces("application/json")
    public List<Release> getReleases( 
            @PathParam("product") String product, 
            @Context HttpServletResponse res,
            @Context SecurityContext sc ) 
    {
        System.out.println("Releases of: " + product);
        List<Release> rel = relDao.getReleasesOfProduct(product);
        return rel;
    }
    
    @GET
    @Path("/release/{product}/{version}")
    @Produces("application/json")
    public Release getReleaseInfo( 
            @PathParam("product") String product, 
            @PathParam("version") String version, 
            @Context HttpServletResponse res,
            @Context SecurityContext sc ) throws IOException
    {
        System.out.println("Release: " + product + " " + version);
        
        try {
            return relDao.getRelease(product, version);
        } catch (NoResultException ex){
            res.sendError(404, "No such release.");
            return null;
        }
    }

}// class
