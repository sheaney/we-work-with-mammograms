package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Staff;

public class APIHelpers {
	public static List<Staff> filterMatchingStaffFromList(List<Staff> list,
			Staff staff) {
		List<Staff> result = new ArrayList<Staff>();
		boolean staffIsNull = staff == null;

		for (Staff member : list) {
			if (staffIsNull || !member.getId().equals(staff.getId())) {
				result.add(member);
			}
		}

		return result;
	}

}
