package org.jboss.essc.web.model;

import java.io.Serializable;


/**
 *
 * @author ondra
 */
public interface IHasTraits  extends Serializable {
    
    public ReleaseTraits getTraits();
    public void setTraits( ReleaseTraits traits );
    
}// class
