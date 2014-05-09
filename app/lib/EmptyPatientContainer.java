package lib;

/**
 * Created by fernando on 5/8/14.
 */
public class EmptyPatientContainer extends PatientContainer {

    @Override
    public int getAccessPrivileges() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
