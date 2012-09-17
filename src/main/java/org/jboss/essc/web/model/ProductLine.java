package org.jboss.essc.web.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 *  Product line - groups releases of the same product.
 * 
 *  @author Ondrej Zizka
 */
@SuppressWarnings("serial")
@Entity @Table(name="product")
public class ProductLine implements Serializable, IHasTraits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique=true)
    private String name;
    
    private String note;
    

    
    // ---- Traits ----
    
    @Embedded 
    private ReleaseTraits traits;
    
    /*/
    // Files links
    private String linkReleasedBinaries;
    private String linkReleasedDocs;
    private String linkStagedBinaries;
    private String linkStagedDocs;
    private String linkMavenLocalRepo;
    
    // Info links
    private String linkJiraResolvedInThisVersion;
    private String linkJiraFixedInThisVersion;
    
    // Build links
    private String linkMead;
    private String linkBrew;
    private String linkGitRepo;
    /**/
    

    
    
    public ProductLine() {
    }

    public ProductLine(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="Get/set">
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    
    //*
    public ReleaseTraits getTraits() { 
        if( traits == null )  traits = new ReleaseTraits(); // HHH-7610
        return traits; 
    }
    public void setTraits( ReleaseTraits traits ) { this.traits = traits; }
        
    /*/
    public String getLinkBrew() { return linkBrew; }
    public void setLinkBrew(String linkBrew) { this.linkBrew = linkBrew; }
    public String getLinkGitRepo() { return linkGitRepo; }
    public void setLinkGitRepo(String linkGitRepo) { this.linkGitRepo = linkGitRepo; }
    public String getLinkJiraFixedInThisVersion() { return linkJiraFixedInThisVersion; }
    public void setLinkJiraFixedInThisVersion(String linkJiraFixedInThisVersion) { this.linkJiraFixedInThisVersion = linkJiraFixedInThisVersion; }
    public String getLinkJiraResolvedInThisVersion() { return linkJiraResolvedInThisVersion; }
    public void setLinkJiraResolvedInThisVersion(String linkJiraResolvedInThisVersion) { this.linkJiraResolvedInThisVersion = linkJiraResolvedInThisVersion; }
    public String getLinkMavenLocalRepo() { return linkMavenLocalRepo; }
    public void setLinkMavenLocalRepo(String linkMavenLocalRepo) { this.linkMavenLocalRepo = linkMavenLocalRepo; }
    public String getLinkMead() { return linkMead; }
    public void setLinkMead(String linkMead) { this.linkMead = linkMead; }
    public String getLinkReleasedBinaries() { return linkReleasedBinaries; }
    public void setLinkReleasedBinaries(String linkReleasedBinaries) { this.linkReleasedBinaries = linkReleasedBinaries; }
    public String getLinkReleasedDocs() { return linkReleasedDocs; }
    public void setLinkReleasedDocs(String linkReleasedDocs) { this.linkReleasedDocs = linkReleasedDocs; }
    public String getLinkStagedBinaries() { return linkStagedBinaries; }
    public void setLinkStagedBinaries(String linkStagedBinaries) { this.linkStagedBinaries = linkStagedBinaries; }
    public String getLinkStagedDocs() { return linkStagedDocs; }
    public void setLinkStagedDocs(String linkStagedDocs) { this.linkStagedDocs = linkStagedDocs; }
    /**/
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
        ProductLine other = (ProductLine) obj;
        
        if (name == null) {
            if (other.name != null)  return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }
   
}// class
