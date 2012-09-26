package org.jboss.essc.web.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;


/**
 *  User
 * 
 *  @author Ondrej Zizka
 */
@SuppressWarnings("serial")
@Entity @Table(name="user")
@XmlRootElement(name="user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique=true, nullable=false)
    private String name;
    
    @Column(nullable=false)
    @XmlTransient @JsonIgnore
    private String pass;
    
    @Column(unique=true)
    private String mail;
    
    @Column(nullable=false)
    private boolean showProd;
    
    
    public User() {
    }

    public User( String name, String pass ) {
        this.name = name;
        this.pass = pass;
    }
    
    

    
    
    
    //<editor-fold defaultstate="collapsed" desc="Get/set">
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMail() { return mail; }
    public void setMail( String mail ) { this.mail = mail; }
    public String getName() { return name; }
    public void setName( String name ) { this.name = name; }
    public String getPass() { return pass; }
    public void setPass( String pass ) { this.pass = pass; }

    public boolean isShowProd() { return showProd; }
    public void setShowProd( boolean showProd ) { this.showProd = showProd; }
    //</editor-fold>


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)  return true;
        if (obj == null)  return false;
        if (getClass() != obj.getClass())  return false;
        User other = (User) obj;
        
        if (name == null) {
            if (other.name != null)  return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }
   
}