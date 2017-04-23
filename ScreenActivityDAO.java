import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ScreenActivityDAO {
	Scanner keyboard = new Scanner(System.in);
	private static ScreenActivityDAO instance;
	private int primaryKey;

	private ScreenActivityDAO() {

	}

	public static ScreenActivityDAO getInstance() {
		if (instance == null) {
			instance = new ScreenActivityDAO();
			instance.scanActivityFile();
		}
		return instance;
	}

	public List<ScreenActivity> activities = new ArrayList<>();

	public void addActivity(ScreenActivity activity) {
		activity.setActivityID(primaryKey++);
		activities.add(activity);
		String newActivity = activity.toString();
		ReadWriteFiles.writeToFile(newActivity, "Activities.txt");
	}

	public List<ScreenActivity> getActivities() {
		return activities;
	}

	private void scanActivityFile() {
		ScreenActivity activity = null;
		String[] strArray = null;
		List<String> list = new ArrayList<String>();
		String activitiesList = ReadWriteFiles.readFile("Activities.txt");
		String eachline = null;
		Scanner inputScanner = new Scanner(activitiesList);
		while (inputScanner.hasNext()) {
			eachline = inputScanner.nextLine();
			strArray = eachline.split(",");
			list = Arrays.asList(strArray);
			String activityIDString = list.get(0);
			int activityID = 0;
			try {
				activityID = Integer.parseInt(activityIDString);
				if (primaryKey <= activityID) {
					primaryKey = activityID + 1;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String activityType = list.get(1);
			String activityName = list.get(2);
			String activityEd = list.get(3);
			activity = new ScreenActivity(activityType, activityName, activityEd, activityID);
			activities.add(activity);
		}
		try {
			inputScanner.close();
		} catch (Exception e) {
		}
	}

	public String rebuildStringAfterModOrDelete(List<ScreenActivity> activities) {
		StringBuilder s = new StringBuilder();
		for (ScreenActivity t : activities) {
			String tString = t.toString();
			if (tString.length() > 0) {
				s.append(tString.trim());
				s.append("\n");
			}
		}

		return s.toString();
	}

	public void delActivity(ScreenActivity activityToRemove) {
		Iterator<ScreenActivity> iter = activities.iterator();
		while (iter.hasNext()) {
			if (iter.next().getActivityID() == activityToRemove.getActivityID()) {
				iter.remove();
			}
		}
		saveActivities();
	}

	public void delActivity(int id) {
		Iterator<ScreenActivity> iter = activities.iterator();
		while (iter.hasNext()) {
			if (iter.next().getActivityID() == id) {
				iter.remove();
			}
		}
		saveActivities();
	}

	public ScreenActivity findActivityByType(String activityRequest) {
		Iterator<ScreenActivity> iter = activities.iterator();
		while (iter.hasNext()) {
			ScreenActivity next = iter.next();
			if (next.getActivityType().equals(activityRequest)) {
				return next;
			}
		}
		return null;
	}

	public ScreenActivity findActivityByActID(int actID) {
		Iterator<ScreenActivity> iter = activities.iterator();
		while (iter.hasNext()) {
			ScreenActivity next = iter.next();
			if (next.getActivityID() == (actID)) {
				return next;
			}
		}
		return null;
	}

	public ScreenActivity findActivityByEd(String ed) {
		Iterator<ScreenActivity> iter = activities.iterator();
		while (iter.hasNext()) {
			ScreenActivity next = iter.next();
			if (next.getActivityEd().equals(ed)) {
				return next;
			}
		}
		return null;
	}

	public void saveActivities() {
		List<String> contents = new ArrayList<String>();
		for (ScreenActivity activity : activities) {
			contents.add(activity.toString());
		}
		ReadWriteFiles.writeListOfStringsToFileAfterDelete(contents, "Activities.txt");
	}

	public void save(ScreenActivity s) {
		for (int i = 0; i < activities.size(); i++) {
			ScreenActivity screenActivity = activities.get(i);
			if (screenActivity.getActivityID() == s.getActivityID()) {
				activities.set(i, s);
			}
		}
		saveActivities();
	}
}