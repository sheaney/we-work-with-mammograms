package security;

import static play.mvc.Results.unauthorized;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import models.ServiceAuth;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.SimpleResult;

/**
 * Created by fernando on 5/9/14.
 */
public class ServiceDeadboltHandler implements DeadboltHandler {


    @Override
    public Subject getSubject(Http.Context context) {
        String authToken = context.request().getHeader("Authorization");
        if(ServiceAuth.verifyService(authToken))
            return new AuthorizableService();
        return null;
    }

    @Override
    public Promise<SimpleResult> onAuthFailure(Http.Context context, String content) {
        return (Promise.promise(new F.Function0<SimpleResult>() {
            @Override
            public SimpleResult apply() throws Throwable {
                return unauthorized(Json.newObject().put("STATUS", "Not Authorized"));
            }
        }));
    }

    //required by the "implements DeadboltHandler" class annotation
    @Override
    public F.Promise<SimpleResult> beforeAuthCheck(Http.Context arg0) {
        return F.Promise.pure(null);
    }
    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context arg0) {
        return null;
    }
}
