
package org.jboss.essc.wicket;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.validation.*;


/**
 *
 * @author ozizka@redhat.com
 */
public class UrlHttpRequestValidator implements IValidator<String> {

    @Override public void validate( IValidatable<String> validatable )
    {
        if( StringUtils.isBlank( validatable.getValue() ) )
            return;
        
        HttpURLConnection conn = null;
        String err = null;
        try {
            URL url = new URL( validatable.getValue() );

            // Only validate HTTP.
            if( ! "http".equals( url.getProtocol() ) )  return;

            //Set up the initial connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "HEAD" );
            conn.setDoOutput( false );
            conn.setReadTimeout( 1000 );
            conn.connect();
            
            if( conn.getResponseCode() >= 400 ){
                err = "Response was " + conn.getResponseCode() + " - " + conn.getResponseMessage();
            }
        }
        catch( MalformedURLException ex ) {
            err = "Malformed URL: " + ex.getMessage();
        }
        catch( ProtocolException ex ) {
            err = "ProtocolException: " + ex.getMessage();
        }
        catch( IOException ex ) {
            err = "Couldn't connect to " + ex.getMessage();
        }
        finally {
            if( err != null ){
                final String err2 = err;
                validatable.error( new IValidationError() {
                    @Override public String getErrorMessage( IErrorMessageSource messageSource ) {
                        return err2;
                    }
                });
            }
            if( conn != null )  conn.disconnect();
        }
    }

}// class
