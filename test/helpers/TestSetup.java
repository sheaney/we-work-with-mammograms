package helpers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import models.Patient;
import models.PersonalInfo;
import models.Staff;
import play.Configuration;
import play.GlobalSettings;
import play.api.mvc.RequestHeader;
import play.i18n.Lang;
import play.mvc.Http.Context;
import play.mvc.Http.Flash;
import play.mvc.Http.Request;

import com.typesafe.config.ConfigFactory;

public class TestSetup {

	public static Context testHttpContext() {
		HashMap<String, String> emptyData = new HashMap<String, String>();

		Request request = mock(Request.class);
		when(request.host()).thenReturn("localhost");
		RequestHeader rh = mock(RequestHeader.class);
		when(rh.host()).thenReturn("localhost");

		Context ctx = mock(Context.class);
		when(ctx._requestHeader()).thenReturn(rh);
		when(ctx.request()).thenReturn(request);
		when(ctx.flash()).thenReturn(new Flash(emptyData));
		when(ctx.lang()).thenReturn(Lang.forCode("en"));
		return ctx;
	}

	public static GlobalSettings testGlobalSettings() {
		return new GlobalSettings() {
			@Override
			public Configuration onLoadConfig(Configuration config, File path,
					ClassLoader classloader) {
				// ignore the default configuration
				return new Configuration(
						ConfigFactory.parseMap(testDbSettings()));
			}

		};
	}

	public static Map<String, Object> testDbSettings() {
		Map<String, Object> dbSettings = new HashMap<String, Object>();
		dbSettings.put("smtp.host", "xyz@xyz.com");
		dbSettings.put("db.default.driver", "org.h2.Driver");
		dbSettings.put("db.default.user", "sh");
		dbSettings.put("db.default.url", "jdbc:h2:mem:wwwmtest;MODE=PostgreSQL");
		dbSettings.put("ebean.default", "models.*, helpers.*, lib.permissions.*");
		dbSettings.put("datasource", "default");
		return dbSettings;
	}

}
