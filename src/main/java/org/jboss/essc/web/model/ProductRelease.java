package org.jboss.essc.web.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang.StringUtils;


/**
 *  Information about product.
 * 
 *  Status
 *  Links to staged and released binaries
 *  Links to staged and released docs
 *  Links maven-local-repo and other goodies
 *  Links to verification job runs
 *  Links to Jira - bugs resolved/found in this release, bugs with release notes...
 *  Links to MEAD build
 *  Links to git repos
 *  Basic metadata, like git hash from which the build originated,
 * 
 *  @author Ondrej Zizka
 */
@SuppressWarnings("serial")
@Entity @Table(name="`release`")
public class ProductRelease implements Serializable, IHasTraits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //@Column(unique=true)
    @ManyToOne
    @JoinColumn(updatable=false, nullable=false)
    private ProductLine product;

    private String version;
    
    
    @Temporal(TemporalType.DATE)
    private Date plannedFor;
    
    @Temporal(TemporalType.DATE)
    private Date lastChanged = new Date();
    
    private Status status = Status.PLANNED;
    
    private String note;

    
    // ---- Traits ----
    
    @Embedded 
    private ReleaseTraits traits;
    
    /*
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
    private String linkMead;
    private String linkBrew;
    private String linkGitRepo;
    */

    
    
    public ProductRelease() {
    }

    public ProductRelease(Long id, ProductLine product, String version) {
        this.id = id;
        this.product = product;
        this.version = version;
        
        this.updateWithProductTemplates();
    }
    
    private void updateWithProductTemplates(){
        if( this.product == null )  return;
        if( StringUtils.isBlank(this.version) )  return;
        
        //this.linkReleasedBinaries = this.product.getLinkReleasedBinaries().replace("${ver}", this.version);
        //this.linkStagedBinaries   = this.product.getLinkStagedBinaries().replace("${ver}", this.version);
        // TODO - the rest.
        
        Field[] fields = this.getClass().getFields();
        for( Field field : fields ) {
            String methodName = "get" + StringUtils.capitalize( field.getName() );
            try {
                Method method = this.product.getClass().getMethod(methodName);
                String tpl = (String) method.invoke(this.product);
                if( tpl == null )  continue;
                field.set( this, replaceVersionIfNotNull(tpl) );
            }
            catch( Exception ex ) { }
        }
    }
    
    private String replaceVersionIfNotNull( String template ){
        return template.replace("${ver}", this.version)
                .replace("${ver.lower}", this.version.toLowerCase())
                .replace("${ver.upper}", this.version.toUpperCase());
    }


    
    //<editor-fold defaultstate="collapsed" desc="Get/set">
    public Long getId() {        return id;    }
    public void setId(Long id) { this.id = id;    }
    
    public ProductLine getProduct() { return product; }
    public void setProduct( ProductLine product ) { this.product = product; }
    public String getVersion() { return version; }
    public void setVersion( String version ) { this.version = version; }

    public String getNote() {        return note;    }
    public void setNote(String note) { this.note = note;    }
    public Status getStatus() {        return status;    }
    public void setStatus(Status status) { this.status = status;    }
    
    public Date getPlannedFor() { return plannedFor; }
    public void setPlannedFor( Date plannedFor ) { this.plannedFor = plannedFor; }
    public Date getLastChanged() { return lastChanged; }
    public void setLastChanged( Date lastChanged ) { this.lastChanged = lastChanged; }

    //*
    public ReleaseTraits getTraits() { return traits; }
    public void setTraits( ReleaseTraits traits ) { this.traits = traits; }
        
    /*/
    public String getGitHash() {        return gitHash;    }
    public void setGitHash(String gitHash) { this.gitHash = gitHash;    }
    public String getLinkBrew() {        return linkBrew;    }
    public void setLinkBrew(String linkBrew) { this.linkBrew = linkBrew;    }
    public String getLinkGitRepo() {        return linkGitRepo;    }
    public void setLinkGitRepo(String linkGitRepo) { this.linkGitRepo = linkGitRepo;    }
    public String getLinkIssuesFixed() {        return linkIssuesFixed;    }
    public void setLinkIssuesFixed(String linkIssuesFixed) { this.linkIssuesFixed = linkIssuesFixed;    }
    public String getLinkIssuesFound() {        return linkIssuesFound;    }
    public void setLinkIssuesFound(String linkIssuesFound) { this.linkIssuesFound = linkIssuesFound;    }
    public String getLinkMavenLocalRepo() {        return linkMavenLocalRepo;    }
    public void setLinkMavenLocalRepo(String linkMavenLocalRepo) { this.linkMavenLocalRepo = linkMavenLocalRepo;    }
    public String getLinkMead() {        return linkMead;    }
    public void setLinkMead(String linkMead) { this.linkMead = linkMead;    }
    public String getLinkReleasedBinaries() {        return linkReleasedBinaries;    }
    public void setLinkReleasedBinaries(String linkReleasedBinaries) { this.linkReleasedBinaries = linkReleasedBinaries;    }
    public String getLinkReleasedDocs() {        return linkReleasedDocs;    }
    public void setLinkReleasedDocs(String linkReleasedDocs) { this.linkReleasedDocs = linkReleasedDocs;    }
    public String getLinkStagedBinaries() {        return linkStagedBinaries;    }
    public void setLinkStagedBinaries(String linkStagedBinaries) { this.linkStagedBinaries = linkStagedBinaries;    }
    public String getLinkStagedDocs() {        return linkStagedDocs;    }
    public void setLinkStagedDocs(String linkStagedDocs) { this.linkStagedDocs = linkStagedDocs;    }
    /**/
    //</editor-fold>


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)  return true;
        if (obj == null)  return false;
        if (getClass() != obj.getClass())  return false;
        ProductRelease other = (ProductRelease) obj;
        
        if (product == null) {
            if (other.product != null) return false;
        }
        else if (!product.equals(other.product)) return false;
        
        if (version == null) {
            if (other.version != null) return false;
        }
        else if (!version.equals(other.version)) return false;
        
        return true;
    }
    
    
    public enum Status {
        PLANNED("Planned"),
        IN_PROGRESS("In progress"),
        RELEASED("Released");
        
        private String statusString;

        private Status( String ss ) {
            this.statusString = ss;
        }

        public String getStatusString() {
            return statusString;
        }
        
    }
    
}