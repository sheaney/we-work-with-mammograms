package models;

import static helpers.TestSetup.testGlobalSettings;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import org.junit.Test;

public class StaffTestJava {

    
	@Test
    public void tryAuthenticateUser() {
		/*running(fakeApplication(testGlobalSettings()), new Runnable() {
			
            @Override
            public void run() {*/
            	Staff staff = new Staff();
            	
            	staff.setEmail("sheaney@gmail.com");
            	staff.setEmail("123");
            	
            	staff.save();
                
                assertNotNull(Staff.authenticate(staff.getEmail(), staff.getPassword()));
                assertNull(Staff.authenticate(staff.getEmail(), "badpassword"));
                assertNull(Staff.authenticate("nottheemail@gmail.com", staff.getPassword()));
         /*  }
            
        });*/

    }
}
