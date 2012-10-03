package org.jboss.essc.web.rest;

import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
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
    
    @Inject private ReleaseDaoBean daoRel; 
    @Inject private ProductDaoBean daoProd; 


    @GET
    @Path("/products")
    @Produces("application/json")
    public List<Product> getProducts( @Context SecurityContext sc ) {
        List<Product> prods = daoProd.getProducts_orderName(0);
        for (Product prod : prods) {
            prod.setTraits(null);
        }
        return prods;
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
        List<Release> rel = daoRel.getReleasesOfProduct(product);
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
            return daoRel.getRelease(product, version);
        } catch (NoResultException ex){
            res.sendError(404, "No such release.");
            return null;
        }/* catch (EJBException ex){
            if( ex.getCausedByException() instanceof NoResultException)
            res.sendError(404, "No such release.");
            return null;
        }*/
    }
    
    
    @PUT
    @Path("/release/{product}/{version}")
    @Consumes("application/json")
    @Produces("application/json")
    public Release addRelease( 
            @PathParam("product") String prodName, 
            @PathParam("version") String version, 
            @Context HttpServletResponse res,
            @Context SecurityContext sc ) throws IOException
    {
        System.out.println("Release: " + prodName + " " + version);
        
        // Get product
        Product prod;
        try {
            prod = daoProd.getProductByName(prodName);
        } catch (NoResultException ex){
            res.sendError(404, "No such product: " + prodName);
            return null;
        }

        // Verify that the relese doesn't exist yet.
        try {
            daoRel.getRelease( prodName, version );
        } catch (NoResultException ex){
            res.sendError(404, "Release already exists: " + prodName + " " + version);
            return null;
        }
        
        // Add a release to it
        return daoRel.addRelease( prod, version );
    }
    
    

}// class
