
package org.jboss.essc.wicket;

import org.apache.wicket.validation.validator.PatternValidator;


/**
 *
 * @author ozizka@redhat.com
 */
public class UrlSimpleValidator extends PatternValidator {

    public UrlSimpleValidator() {
        super("^https?://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-z]{2,4}(:\\d+)?/.*");
    }
    
}
