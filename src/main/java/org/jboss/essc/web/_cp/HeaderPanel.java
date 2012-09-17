package org.jboss.essc.web._cp;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.pages.statics.AboutPage;


/**
 * @author Ondrej Zizka
 */
public class HeaderPanel extends Panel {
    
    public HeaderPanel( String id ) {
        super(id);
        this.setRenderBodyOnly(true);
        
        add( new WebMarkupContainer("menuProducts").add( new ActiveMenuItemBehavior() ) );
        add( new WebMarkupContainer("menuAbout").add( new ActiveMenuItemBehavior() ) );
    }// const
    
}// class



class ActiveMenuItemBehavior extends Behavior {
    @Override
    public void onComponentTag( Component component, ComponentTag tag ) {
        Class pageClass = component.getPage().getClass();
        
        // Decide which menu item to activate. TODO: Pass to this behavior as an argument.
        String activeItemId = "menuProducts";
        if( AboutPage.class.equals( pageClass ) )
            activeItemId = "menuAbout";
        
        // Decide what class to put to processed component.
        String cls = "";
        if( activeItemId.equals( component.getId() ) )
            cls = "active ";
        
        tag.put("class", cls + StringUtils.defaultString( tag.getAttribute("class") ) );
        super.onComponentTag( component, tag );
    }
}