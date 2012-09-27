
package org.jboss.essc.wicket;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.DefaultExceptionMapper;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.IProvider;
import org.jboss.essc.web.pages.HomePage;


/**
 * This mapper maps the CNFEx occurring during deserialization to land at HomePage,
 * effectively being ignored.
 *  
 * @author ozizka@redhat.com
 */
public class AS74554exceptionMapperProvider implements IProvider<IExceptionMapper> {
    @Override public IExceptionMapper get() {
        return new IExceptionMapper() {
            final DefaultExceptionMapper def = new DefaultExceptionMapper();

            @Override public IRequestHandler map( Exception ex ) {
                PageParameters par = new PageParameters().add("ex", ex);

                // AS7-4554, WICKET-4785 

                // The root is: RuntimeException: "Could not deserialize object using: JavaSerializer ..."
                Throwable cause = ExceptionUtils.getRootCause( ex );

                // java.lang.ClassNotFoundException: "org.jboss.msc.service.ServiceName from ..."
                if( ClassNotFoundException.class.isInstance(cause) ){
                    if(  ((ClassNotFoundException)cause).getMessage().startsWith("org.jboss.msc.service.ServiceName"))
                        return new RenderPageRequestHandler(new PageProvider(HomePage.class, par) );
                }

                //throw new RuntimeException("Oh no: " + ex.getClass() + " " + ex.getMessage());
                //throw new RuntimeException("Oh no: " + cause.getClass() + " " + cause.getMessage());
                return def.map( ex );
            }
        };
    }
}

