
package org.jboss.essc.web.util;

import org.jboss.security.annotation.Authentication;
import org.jboss.security.annotation.Authorization;
import org.jboss.security.annotation.Module;
import org.jboss.security.annotation.ModuleOption;
import org.jboss.security.auth.spi.UsersRolesLoginModule;
import org.picketbox.plugins.authorization.PicketBoxAuthorizationModule;


/**
 * POJO with both Authentication and Authorization annotations 
 * 
 * @author ozizka@redhat.com
 */
@Authentication(modules={@Module(code = UsersRolesLoginModule.class, options = {
    //@ModuleOption(key="usersProperties", value="${jboss.server.config.dir}/example-users.properties"),
    //@ModuleOption(key="rolesProperties", value="${jboss.server.config.dir}/example-roles.properties"),
    //@ModuleOption(key="usersProperties", value="users.properties"),
    //@ModuleOption(key="rolesProperties", value="roles.properties")
    @ModuleOption
})})
@Authorization (modules={@Module(code = PicketBoxAuthorizationModule.class, options = {@ModuleOption(key="roles",value="admin")})})
public class PicketBoxAuthPojo {
    
}
 