package org.jboss.essc.web.pages.user;

import javax.inject.Inject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.jboss.essc.web.dao.UserDaoBean;
import org.jboss.essc.web.model.User;
import org.jboss.essc.web.pages.BaseLayoutPage;
import org.jboss.essc.web.security.EsscAuthSession;


/**
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class UserPage extends BaseLayoutPage {

    @Inject private UserDaoBean daoUser;

    //@Inject
    private UserModel model;
    
    
    // Password reset values
    public String oldPass;
    public String newPass1;
    public String newPass2;
    
    
    // Components
    private Form<User> form;
    private Form<User> resetPassForm;
    private FeedbackPanel feedback = (FeedbackPanel) new FeedbackPanel("feedback").setOutputMarkupId(true);


    //@AuthorizeAction(roles="authenticated")
    public UserPage(PageParameters params) {
        
        // If not logged in, redirect to a login page.
        User user = ((EsscAuthSession)getSession()).getUser();
        if( null == user )  throw new RestartResponseException( LoginPage.class );

        this.setDefaultModel( this.model = new UserModel( user ).setUserDao( daoUser ) );


        // Components 
        
        //this.feedback.setMarkupId("pageFeedback");
        //this.feedback.add( AttributeModifier.append("class", "feedback") );
        add(this.feedback);
        
        add( this.form = new Form<User>("form") {
            @Override protected void onSubmit() {
                User user = model.getObject();
                user = daoUser.update( user ); // Should I do this in UserModel#setObject() ?
                model.setObject( user );
                info("Saved.");
            }
        });

        //this.form.setModel( new CompoundPropertyModel<User>( model ) );
        
        // User
        this.form.add(new Label("user", new PropertyModel(model, "name")));

        // Mail
        this.form.add(new TextField("mail", new PropertyModel(model, "mail")));

        // Show productization releases?
        CheckBox showProd = new CheckBox("showProd", new PropertyModel(model, "showProd"));
        this.form.add( showProd );

        
        // Reset Password form
        final PasswordTextField oldPassField  = new PasswordTextField("oldPass",  new PropertyModel(UserPage.this, "oldPass"));
        final PasswordTextField newPass1Field = new PasswordTextField("newPass1", new PropertyModel(UserPage.this, "newPass1"));
        
        add( this.resetPassForm = new Form<User>("resetPassForm") {
            @Override protected void onSubmit() {
                User curUser = ((EsscAuthSession)getSession()).getUser();
                if( null == curUser )  throw new RestartResponseException( LoginPage.class );
                
                User user = model.getObject();
                if( ! curUser.equals( user ) ){
                    // This page's user != logged user. Should not ever happen.
                    throw new RestartResponseException( LoginPage.class );
                }
                
                String md5Old = DigestUtils.md5Hex( oldPassField.getValue().trim() );
                if( ! StringUtils.equals( user.getPass(), oldPassField.getValue() ) ){
                    error("Wrong current password.");
                    return;
                }
                String md5New = DigestUtils.md5Hex( newPass1Field.getValue().trim() );
                user.setPass( md5New );
                user = daoUser.update( user ); // Should I do this in UserModel#setObject() ?
                model.setObject( user );
                info("Password was reset.");
            }
        });
        //this.resetPassForm.setModel( new CompoundPropertyModel(UserPage.this) );

        
        this.resetPassForm.add( oldPassField );
        this.resetPassForm.add( newPass1Field );
        this.resetPassForm.add( new PasswordTextField("newPass2", new PropertyModel(UserPage.this, "newPass2"))
            .add( new AbstractValidator<String> () {
                @Override protected void onValidate( IValidatable<String> validatable ) {
                    if( ! validatable.getValue().equals( newPass1Field.getValue() ) ){
                        validatable.error( new IValidationError() {
                            @Override public String getErrorMessage( IErrorMessageSource messageSource ) {
                                return "Passwords do not match.";
                            }
                        });
                    }
                }
            })
        );
    }


}// class
