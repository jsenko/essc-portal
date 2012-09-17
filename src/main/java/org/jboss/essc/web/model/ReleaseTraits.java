
package org.jboss.essc.web.model;

import java.io.Serializable;
import javax.persistence.Embeddable;


/**
 *  Traits - used for 1) product trait templates 2) release trait values.
 * 
 * @author ozizka@redhat.com
 */
@Embeddable
public class ReleaseTraits implements Serializable {

    // Source links
    private String gitHash;
    
    // Files links
    private String linkReleasedBinaries;
    private String linkReleasedDocs;
    private String linkStagedBinaries;
    private String linkStagedDocs;
    private String linkMavenLocalRepo;
    
    // Info links
    private String linkIssuesFixed;
    private String linkIssuesFound;
    
    // Build links
    private String linkGitRepo;
    private String linkMead;
    private String linkBrew;

    
    //<editor-fold defaultstate="collapsed" desc="Get/Set">
    public String getGitHash() { return gitHash; }
    public void setGitHash( String gitHash ) { this.gitHash = gitHash; }
    public String getLinkBrew() { return linkBrew; }
    public void setLinkBrew( String linkBrew ) { this.linkBrew = linkBrew; }
    public String getLinkGitRepo() { return linkGitRepo; }
    public void setLinkGitRepo( String linkGitRepo ) { this.linkGitRepo = linkGitRepo; }
    public String getLinkIssuesFixed() { return linkIssuesFixed; }
    public void setLinkIssuesFixed( String linkIssuesFixed ) { this.linkIssuesFixed = linkIssuesFixed; }
    public String getLinkIssuesFound() { return linkIssuesFound; }
    public void setLinkIssuesFound( String linkIssuesFound ) { this.linkIssuesFound = linkIssuesFound; }
    public String getLinkMavenLocalRepo() { return linkMavenLocalRepo; }
    public void setLinkMavenLocalRepo( String linkMavenLocalRepo ) { this.linkMavenLocalRepo = linkMavenLocalRepo; }
    public String getLinkMead() { return linkMead; }
    public void setLinkMead( String linkMead ) { this.linkMead = linkMead; }
    public String getLinkReleasedBinaries() { return linkReleasedBinaries; }
    public void setLinkReleasedBinaries( String linkReleasedBinaries ) { this.linkReleasedBinaries = linkReleasedBinaries; }
    public String getLinkReleasedDocs() { return linkReleasedDocs; }
    public void setLinkReleasedDocs( String linkReleasedDocs ) { this.linkReleasedDocs = linkReleasedDocs; }
    public String getLinkStagedBinaries() { return linkStagedBinaries; }
    public void setLinkStagedBinaries( String linkStagedBinaries ) { this.linkStagedBinaries = linkStagedBinaries; }
    public String getLinkStagedDocs() { return linkStagedDocs; }
    public void setLinkStagedDocs( String linkStagedDocs ) { this.linkStagedDocs = linkStagedDocs; }
    //</editor-fold>
    
    
}// class
