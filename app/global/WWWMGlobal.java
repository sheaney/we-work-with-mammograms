package global;

import play.GlobalSettings;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class WWWMGlobal extends GlobalSettings {
	public Injector injector = Guice.createInjector(new AbstractModule() {

		@Override
		protected void configure() {
		}
	});

	@Override
	public <A> A getControllerInstance(Class<A> clazz) throws Exception {
		return injector.getInstance(clazz);
	}

}
