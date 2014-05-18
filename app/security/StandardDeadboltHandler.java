package security;

import static play.mvc.Results.forbidden;
import static play.mvc.Results.redirect;

import play.i18n.Messages;
import play.libs.F;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import views.html.forbidden;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import controllers.routes;

public class StandardDeadboltHandler implements DeadboltHandler {

	private final Long sessionDurationInMilliseconds = 3600000L;

	@Override
	public Subject getSubject(Context arg0) {
		AuthorizableUser user = new AuthorizableUser(arg0);
		// If time of login + sessionDurationInMilliseconds is less
		// than the actual time then the session has expired
		if (user.getTimeOfLogin() + sessionDurationInMilliseconds < System.currentTimeMillis()) {
			arg0.session().clear();
			return null;
		}
		return user;
	}

	@Override
	public Promise<SimpleResult> onAuthFailure(Context arg0, String arg1) {
		// if no user is logged, go to login
		if (arg0.session().get("id") == null) {
			return (Promise.promise(new Function0<SimpleResult>() {
				@Override
				public SimpleResult apply() throws Throwable {
					return redirect(routes.Application.login());
				}
			}));
		} else {// return 403 - forbidden
			return (Promise.promise(new Function0<SimpleResult>() {
				@Override
				public SimpleResult apply() throws Throwable {
					return forbidden(forbidden.render(Messages.get("error.invalid.permissions")));
				}
			}));
		}
	}

	//required by the "implements DeadboltHandler" class annotation
    @Override
    public Promise<SimpleResult> beforeAuthCheck(Context arg0) {
        return F.Promise.pure(null);
    }
    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Context arg0) {
        return null;
    }

}