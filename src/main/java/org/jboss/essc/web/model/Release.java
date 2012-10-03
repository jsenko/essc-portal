package org.jboss.essc.web.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.Format;
import java.util.Date;
import java.util.Locale;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.jboss.essc.web.util.SimpleRelativeDateFormatter;


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
public class Release implements Serializable, IHasTraits {
    
    private static final Format DF = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //@Column(unique=true)
    @ManyToOne
    @JoinColumn(updatable=false, nullable=false)
    @XmlTransient
    //@JsonIgnore
    private Product product;

    private String version;
    
    private boolean internal = true;
    
    
    @Temporal(TemporalType.DATE)
    private Date plannedFor;
    
    @Temporal(TemporalType.DATE)
    @Column(columnDefinition="TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable=false, updatable=false)
    private Date lastChanged = new Date();
    
    private Status status = Status.PLANNED;
    
    private String note;

    
    // ---- Traits ----
    
    @Embedded
    //@Basic(fetch=FetchType.EAGER, optional=false) // HHH-7610
    private ReleaseTraits traits = new ReleaseTraits();
    
    
    public Release() {
    }

    public Release(Long id, Product product, String version) {
        this.id = id;
        this.product = product;
        this.version = version;
        
        this.updateWithProductTemplates();
    }
    

    
    public final void updateWithProductTemplates(){
        if( this.product == null )  return;
        if( StringUtils.isBlank(this.version) )  return;
        
        this.traits = this.product.getTraits().clone();
        replaceVersionTokens();
    }
    
    public void replaceVersionTokens(){
        this.traits.replaceTemplatesTokens( "${ver}", this.version );
        this.traits.replaceTemplatesTokens( "${ver.lower}", this.version.toLowerCase() );
        this.traits.replaceTemplatesTokens( "${ver.upper}", this.version.toUpperCase() );        
    }
    
    /*
    private String replaceVersionIfNotNull( String template ){
        return template.replace("${ver}", this.version)
                .replace("${ver.lower}", this.version.toLowerCase())
                .replace("${ver.upper}", this.version.toUpperCase());
    }
    */


    
    //<editor-fold defaultstate="collapsed" desc="Get/set">
    public Long getId() {        return id;    }
    public void setId(Long id) { this.id = id;    }
    
    public Product getProduct() { return product; }
    public void setProduct( Product product ) { this.product = product; }
    public String getVersion() { return version; }
    public void setVersion( String version ) { this.version = version; }

    public boolean isInternal() { return internal; }
    public void setInternal( boolean internal ) { this.internal = internal; }

    public String getNote() {        return note;    }
    public void setNote(String note) { this.note = note;    }
    public Status getStatus() {        return status;    }
    public void setStatus(Status status) { this.status = status;    }
    
    public Date getPlannedFor() { return plannedFor; }
    public void setPlannedFor( Date plannedFor ) { this.plannedFor = plannedFor; }
    public Date getLastChanged() { return lastChanged; }
    public void setLastChanged( Date lastChanged ) { this.lastChanged = lastChanged; }

    //*
    public ReleaseTraits getTraits() { 
        if( traits == null )  traits = new ReleaseTraits(); // HHH-7610
        return traits; 
    }
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

    public String toStringIdentifier() {
        return (this.product == null ? "" : this.product.getName()) + "-" + this.version;
    }

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
        Release other = (Release) obj;
        
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

    public String formatPlannedFor() {
        return (this.plannedFor == null) ? "" : DF.format( this.plannedFor );
    }

    public String formatPlannedForRelative() {
        return (this.plannedFor == null) ? "" : SimpleRelativeDateFormatter.format( plannedFor );
    }

    @Deprecated // see PropertiesDownloadLink.getPropertiesAsString().
    public String getTraitsAsProperties() {
        StringBuilder sb = new StringBuilder(1024);
        Field[] fields = this.getClass().getDeclaredFields();
        for( Field field : fields ) {
            if( ! field.getType().equals(String.class) )  continue;
            sb.append( field.getName() ).append("=");
            try {
                sb.append( (String) field.get(this) );
            }
            catch( Exception ex ) {
                sb.append( ex.toString().replace("\n", " ") );
            }
        }
        return sb.toString();
    }
    
    
    /**
     *  Status of the release.
     */
    public enum Status {
        PLANNED("Planned"),
        IN_PROGRESS("In progress"),
        TAGGED("Tagged"),
        STAGED("Staged"),
        RELEASED("Released");
        
        private String statusString;

        private Status( String ss ) {
            this.statusString = ss;
        }

        public String getStatusString() {
            return statusString;
        }
        
    }
    
}// class
