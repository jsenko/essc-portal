package org.jboss.essc.web.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.essc.web.model.Product;


/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ProductDaoBean {

    @PersistenceContext
    private EntityManager em;


    public List<Product> getProducts_orderName(int limit) {
        return this.em.createQuery("SELECT p FROM Product p ORDER BY p.name").getResultList();
    }

    /**
     * Get ProductLine by ID.
     */
    public Product getProductLine(Long id) {
        return this.em.find(Product.class, id);
    }

    /**
     * Get ProductLine by name.
     * @throws  NoResultException if no such product found.
     */
    public Product getProductByName( String name ) {
        return this.em.createQuery("SELECT p FROM Product p WHERE p.name = ?", Product.class).setParameter(1, name).getSingleResult();
    }
    /**
     * Find ProductLine by name.
     * @returns null if not found.
     */
    public Product findProductByName( String name ) {
        List<Product> list = this.em.createQuery("SELECT p FROM Product p WHERE p.name = ?", Product.class).setParameter(1, name).getResultList();
        if( list.isEmpty() )  return null;
        return list.get(0);
    }

    /**
     * Add a new ProductLine.
     */
    public Product addProductLine( String name) {
        return this.em.merge( new Product( null, name ) );
    }

    /**
     * Add a new ProductLine.
     */
    public Product addProduct( Product prod ) {
        return this.em.merge( prod );
    }

    /**
     * Remove a ProductLine.
     */
    public void remove(Product prod) {
        Product managed = this.em.merge(prod);
        this.em.remove(managed);
        this.em.flush();
    }

    
    public Product update( Product product ) {
        Product managed = this.em.merge(product);
        return managed;
    }

    
    public void deleteIncludingReleases( Product prod ) {
        prod = this.em.merge(prod);
        
        // Delete releases
        // Ends up with Hibernate screwing up SQL - "cross join"
        //int up = this.em.createQuery( "DELETE FROM Release r WHERE r.product.name = ?" ).setParameter( 1, prod.getName() ).executeUpdate();
        int up = this.em.createQuery( "DELETE FROM Release r WHERE r.product IN "
                + "(SELECT p FROM Product p WHERE p.name = ?)" ).setParameter( 1, prod.getName() ).executeUpdate();
        System.out.println("Updated " + up);
        
        this.em.remove(prod);
        this.em.flush();
    }
    
}// class
