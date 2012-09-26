
package org.jboss.essc.web.security;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.jboss.essc.web.dao.UserDaoBean;
import org.jboss.essc.web.model.User;


/**
 * wicket-auth-roles -based session auth.
 * 
 * @author ozizka@redhat.com
 */
public class EsscAuthSession extends AuthenticatedWebSession {
    
    
    @Inject private UserDaoBean userDao;
    
    private User user;
    
    

    public EsscAuthSession( Request request ) {
        super( request );
    }

    
    @Override
    public void signOut() {
        super.signOut();
    }


    @Override
    public boolean authenticate( String name, String pass ) {
        if( this.user != null )  return true;

        try {
            User user_ = new User( name, pass );
            this.user = userDao.loadUserIfPasswordMatches( user_ );
            return true;
        } catch (NoResultException ex){
            return false;
        }
    }
    
    
    

    @Override
    public Roles getRoles() {
        if( ! isSignedIn() )  return null;
        
        // If the user is signed in, they have these roles
        return new Roles( Roles.ADMIN );
    }

    
    
    
    
    public User getUser() { return user; }
    public void setUser( User user ) { this.user = user; }
    
    

}