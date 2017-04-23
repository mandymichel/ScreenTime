import static java.lang.System.out;

import java.util.List;
import java.util.Scanner;

public class ScreenActivityMenu {
	private static final ScreenActivityDAO screenActivityDAO = ScreenActivityDAO.getInstance();
	private static final ScreenEventDAO screenEventDAO = ScreenEventDAO.getInstance();

	Scanner keyboard = new Scanner(System.in);

	ScreenActivity currentActivity = null;

	public void addActivity() {
		out.println("Here are the activities already added: ");
		listActivities();

		String activityType = null;
		do {
			out.println("Name the type of activity(video/game/conversation)");
			activityType = keyboard.next();
		} while (!"video".equalsIgnoreCase(activityType) && !"game".equalsIgnoreCase(activityType)
				&& !"conversation".equalsIgnoreCase(activityType));

		out.println("Name the activity(title of game or video or person video chatting with)");
		keyboard.nextLine();
		String activityName = keyboard.nextLine();

		String activityEd = null;
		do {
			out.println("Is the activity educational(y/n)");
			activityEd = keyboard.next();
		} while (!("y".equalsIgnoreCase(activityEd)) && !("n".equalsIgnoreCase(activityEd)));

		int activityID = 0;
		ScreenActivity activity = new ScreenActivity(activityType, activityName, activityEd, activityID);
		ScreenActivityDAO instance = screenActivityDAO;
		instance.addActivity(activity);
	}

	public void modMenu() {
		out.println("Activities Modification Menu");
		out.println("1. Change the type of activity");
		out.println("2. Change the name of the activity");
		out.println("3. Change the educational setting of the activity");
		out.println("4. Stop Modifying activity");
	}

	public void listActivities() {
		ScreenActivityDAO instance = screenActivityDAO;
		List<ScreenActivity> activities = instance.getActivities();

		for (ScreenActivity a : activities) {
			out.println(a.toFormattedString());
		}
	}

	public void modActivity() {
		String modActType = null;
		String modActName = null;
		String modActEd = null;
		listActivities();
		out.println("Type the ID number of the activity you would like to modify.");
		int ID = keyboard.nextInt();
		out.println(ID + " requested");
		for (ScreenActivity s : screenActivityDAO.getActivities()) {
			if (ID == s.getActivityID()) {
				out.println(s);
				modMenu();
				int modChoice = keyboard.nextInt();
				switch (modChoice) {
				case 1:
					do {
						out.println("What type of activity is it?");
						modActType = keyboard.next();
					} while (!"video".equalsIgnoreCase(modActType) && !"game".equalsIgnoreCase(modActType)
							&& !"conversation".equalsIgnoreCase(modActType));

					s.setActivityType(modActType);
					break;
				case 2:
					out.println("What is the name of the activity?");
					modActName = keyboard.next();
					s.setActivityName(modActName);
					break;
				case 3:
					do {
						out.println("Is the activity educational(y/n)?");
						modActEd = keyboard.next();
					} while (!("y".equalsIgnoreCase(modActEd)) && !("n".equalsIgnoreCase(modActEd)));
					s.setActivityEd(modActEd);
					break;
				default:
					modChoice = 0;
					break;
				}
				screenActivityDAO.save(s);
			}
		}

	}

	public void delActivity() {
		listActivities();
		out.println("Type the ID number of the activity you would like to delete.");
		int delAct = keyboard.nextInt();
		boolean actBeingUsed = false;
		List<ScreenEvent> events = screenEventDAO.getEvents();
		for (ScreenEvent s : events) {
			if (delAct == s.getActID()) {
				out.println("You cannot delete that activity. It is in use in this event: " + s);
				actBeingUsed = true;
			}
		}
		if (!actBeingUsed) {
			screenActivityDAO.delActivity(delAct);
		}
	}

	public void searchActivities() {
		out.println("Type the name of the activity");
		String searchNameAct = keyboard.next();
		for (ScreenActivity s : screenActivityDAO.getActivities()) {
			if ((searchNameAct.equalsIgnoreCase(s.getActivityName()))
					|| (searchNameAct.substring(0, 1).equalsIgnoreCase(s.getActivityName().substring(0, 1)))) {
				out.println("The activity you requested: ");
				out.println(s);
			}
		}
	}

	public void printActivityMenu() {

		int activityChoice = 0;
		do {
			out.println("Activity Menu");
			out.println("1. Add activity");
			out.println("2. Modify activity");
			out.println("3. Delete activity");
			out.println("4. List activities");
			out.println("5. Search for activities by name");
			out.println("Press 6 to return to the main menu");
			try {
				activityChoice = keyboard.nextInt();
			} catch (Throwable ime) {
			}

			switch (activityChoice) {
			case 1:
				addActivity();
				break;
			case 2:
				modActivity();
				break;
			case 3:
				delActivity();
				break;
			case 4:
				listActivities();
				break;
			case 5:
				searchActivities();
				break;
			default:
				activityChoice = 0;
				break;
			}
		} while (0 != activityChoice);

	}
}