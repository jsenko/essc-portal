
package org.jboss.essc.wicket;

import java.io.Serializable;
import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;


/**
 *  BookmarkablePageLink with HTML body used as a label.
 * 
 * @author ozizka@redhat.com
 */
public class BPLinkWithBody extends BookmarkablePageLink {
    
    private IModel<String> bodyModel;

    
    public <C extends Page> BPLinkWithBody( String id, Class pageClass ) {
        super( id, pageClass );
    }
    
    public <C extends Page> BPLinkWithBody( String id, Class pageClass, PageParameters parameters ) {
        super( id, pageClass, parameters );
    }

    
    /**
     *  Use element's body as the label.
     */
    @Override
    public void onComponentTagBody( MarkupStream markupStream, ComponentTag openTag ) {
        String body = markupStream.get().toCharSequence().toString();
        this.bodyModel = new Model<String>(body);
        super.onComponentTagBody( markupStream, openTag );
    }

    
    @Override
    public IModel<?> getBody() {
        return this.bodyModel;
    }

    
}// class
