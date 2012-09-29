
package org.jboss.essc.web._cp;

import java.io.IOException;
import java.util.Properties;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.lang.Bytes;


/**
 *
 * @author ozizka@redhat.com
 */
public class PropertiesUploadForm extends Form {
    
    FileUploadField upload;

    
    public PropertiesUploadForm( String id ) {
        super( id );
        add( this.upload = new FileUploadField("file") );
        setMultiPart(true);
        setMaxSize(Bytes.kilobytes(50));
    }

    
    protected Properties processPropertiesFromUploadedFile() throws IOException {
        FileUpload fu = this.upload.getFileUpload();
        if( null == fu ) return null;

        Properties props = new Properties();
        try {
            props.load( fu.getInputStream() );
        }
        catch( IOException ex ) {
            throw ex;
        }
        return props;
    }

}// class
