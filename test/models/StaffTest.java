package models;
import static helpers.TestSetup.testGlobalSettings;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.running;
import static play.test.Helpers.fakeApplication;
import models.Staff;

import org.junit.Test;

import com.avaje.ebean.Ebean;

public class StaffTest {

    @Test
    public void saveNewProposal() {
        running(fakeApplication(testGlobalSettings()), new Runnable() {

            @Override
            public void run() {
              Staff s = new Staff();
              s.save();
              assertThat(rowCount()).isEqualTo(1);
            }
            
        });
    }
    
    private int rowCount() {
        return Ebean.find(Staff.class).findRowCount();
    }

}
