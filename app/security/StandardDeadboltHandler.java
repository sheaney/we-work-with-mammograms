package security;

import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;

public class StandardDeadboltHandler implements DeadboltHandler{

	@Override
	public Promise<SimpleResult> beforeAuthCheck(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicResourceHandler getDynamicResourceHandler(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subject getSubject(Context arg0) {
		return null;//arg0.session();
	}
	
	@Override
	public Promise<SimpleResult> onAuthFailure(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}