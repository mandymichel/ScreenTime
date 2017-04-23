import static java.lang.System.out;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public class ScreenEventMenu {
	private static final ScreenEventDAO screenEventDAO = ScreenEventDAO.getInstance();
	private static final ScreenActivityDAO screenActivityDAO = ScreenActivityDAO.getInstance();

	Scanner keyboard = new Scanner(System.in);
	ScreenEvent currentEvent = null;

	public boolean validDate(String dateTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date d1 = null;
		DateTime dt1 = new DateTime(d1);
		try {

			d1 = format.parse(dateTime);
			dt1 = new DateTime(d1);

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void addEvent() {
		String startDateTime = null;
		String endDateTime = null;
		int actID = 0;
		out.println("Here are the events already added: ");
		listEvent();
		out.println("What is the child's first name?");
		String firstName = keyboard.next();
		out.println("Here are the currently listed activities: ");
		ScreenActivityMenu m = new ScreenActivityMenu();
		m.listActivities();
		do {
			out.println("What is the activity ID for the event?");
			actID = keyboard.nextInt();
		} while (!validateActID(actID));
		do {
			out.println("What is the start date and time?(yyyy-MM-ddHH:mm:ss)");
			startDateTime = keyboard.next();
		} while (!validDate(startDateTime));
		do {
			out.println("What is the end date and time?(yyyy-MM-ddHH:mm:ss)");
			endDateTime = keyboard.next();
		} while (!validDate(endDateTime));

		String elapsedTime = findElapsedTime(startDateTime, endDateTime);
		out.println(elapsedTime);
		out.println("Notes on the event/child(optional)");
		keyboard.nextLine();
		String notes = keyboard.nextLine();
		int eventID = 0;
		ScreenEvent event = new ScreenEvent(firstName, startDateTime, endDateTime, notes, eventID, actID, elapsedTime);
		ScreenEventDAO instance = screenEventDAO;
		instance.addEvent(event);
	}

	public boolean validateActID(int actID) {
		List<ScreenActivity> activities = screenActivityDAO.getActivities();
		for (ScreenActivity s : activities) {
			if (actID == s.getActivityID()) {
				return true;
			}
		}
		return false;
	}

	public String findElapsedTime(String startDateTime, String endDateTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		DateTime dt1 = new DateTime(d1);
		DateTime dt2 = new DateTime(d2);
		try {

			d1 = format.parse(startDateTime);
			d2 = format.parse(endDateTime);
			dt1 = new DateTime(d1);
			dt2 = new DateTime(d2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Days.daysBetween(dt1, dt2).getDays() + " days/" + Hours.hoursBetween(dt1, dt2).getHours() % 24
				+ " hours/" + Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " minutes/"
				+ Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds";
	}

	public String rebuildStringAfterModOrDelete(List<ScreenEventMenu> events) {
		StringBuilder s = new StringBuilder();
		for (ScreenEventMenu t : events) {
			String tString = t.toString();
			if (tString.length() > 0) {
				s.append(tString.trim());
				s.append("\n");
			}
		}
		return s.toString();
	}

	public void modMenu() {
		out.println("Event Modification Menu");
		out.println("1. Change the child's name");
		out.println("2. Change the activity ID");
		out.println("3. Change the start date");
		out.println("4. Change the end date");
		out.println("5. Change notes");
		out.println("Press 6 to exit modification menu");
	}

	public void modEvent() {
		String modEventName = null;
		String modNotes = null;
		String modStartDate = null;
		String modEndDate = null;
		int modActID = 0;
		listEvent();
		out.println("Type the event ID number of the event you would like to modify.");
		int eID = keyboard.nextInt();
		out.println(eID + " requested");
		for (ScreenEvent t : screenEventDAO.getEvents()) {
			if (eID == t.getEventID()) {
				out.println(t);
				modMenu();
				int modChoice = keyboard.nextInt();
				switch (modChoice) {
				case 1:
					out.println("What is the child's first name?");
					modEventName = keyboard.next();
					t.setFirstName(modEventName);
					break;
				case 2:
					out.println("What is the new activity ID");
					modActID = keyboard.nextInt();
					ScreenActivityMenu m = new ScreenActivityMenu();
					m.listActivities();
					t.setActID(modActID);
					break;
				case 3:
					do {
						out.println("What is the start date?(yyyy-MM-dd HH:mm:ss)");
						modStartDate = keyboard.next();
					} while (!validDate(modStartDate));
					t.setStartDateTime(modStartDate);
					break;
				case 4:
					do {
						out.println("What is the end date?(yyyy-MM-ddHH:mm:ss)");
						modEndDate = keyboard.next();
					} while (!validDate(modEndDate));
					t.setEndDateTime(modEndDate);
					break;
				case 5:
					out.println("Type new notes.");
					modNotes = keyboard.next();
					t.setNotes(modNotes);
				default:
					modChoice = 0;
					break;
				}
				screenEventDAO.save(t);

			}
		}
	}

	public void delEvent() {
		listEvent();
		out.println("Type the ID number of the activity you would like to delete.");
		int delAct = keyboard.nextInt();
		screenEventDAO.delEvent(delAct);

	}

	public void listEvent() {
		ScreenEventDAO instance = screenEventDAO;
		List<ScreenEvent> events = instance.getEvents();
		Collections.sort(events, new CustomSort());
		for (ScreenEvent s : events) {
			out.println(s.toFormattedString());
		}
	}

	public void searchMenu() {
		out.println("Search Menu");
		out.println("1. Search by child");
		out.println("2. Search by start date");
		out.println("3. Search by child and start date");
	}

	public void searchEvent() {
		String searchName = null;
		List<ScreenEvent> results = null;
		String searchStartDate = null;
		int searchChoice = 0;
		do {
			searchMenu();
			searchChoice = keyboard.nextInt();
			switch (searchChoice) {
			case 1:
				out.println("Type the child involved in the event");
				searchName = keyboard.next();
				for (ScreenEvent s : screenEventDAO.getEvents()) {
					if (searchName.equals(s.getFirstName())) {
						out.println("The event you requested: ");
						out.println(s.toFormattedString());
					}
				}
				break;
			case 2:
				do {
					out.println("Type the start date of the event");
					searchStartDate = keyboard.next();
				} while (!validDate(searchStartDate));
				results = screenEventDAO.findByStartDate(searchStartDate);
				for (ScreenEvent event : results) {
					out.println("The event you requested: ");
					out.println(event.toFormattedString());
				}
				break;
			case 3:
				out.println("Type the child's name");
				searchName = keyboard.next();
				do {
					out.println("Type the start date of the event");
					searchStartDate = keyboard.next();
				} while (!validDate(searchStartDate));
				results = screenEventDAO.findByNameAndDate(searchName, searchStartDate);
				if (results.isEmpty()) {
					out.println("There is no event with that start date.");
				}
				for (ScreenEvent event : results) {
					out.println("The event you requested: ");
					out.println(event.toFormattedString());
				}
				break;
			default:
				searchChoice = 0;
				break;
			}

		} while (0 != searchChoice);

	}

	public void printEventMenu() {
		int eventChoice = 0;
		do {
			eventMenu();
			eventChoice = keyboard.nextInt();
			switch (eventChoice) {
			case 1:
				addEvent();
				break;
			case 2:
				modEvent();
				break;
			case 3:
				delEvent();
				break;
			case 4:
				listEvent();
				break;
			case 5:
				searchEvent();
				break;
			default:
				eventChoice = 0;
				break;
			}
		} while (0 != eventChoice);
	}

	public void eventMenu() {
		out.println("Event Menu");
		out.println("1. Add event");
		out.println("2. Modify event");
		out.println("3. Delete event");
		out.println("4. List event");
		out.println("5. Search events");
		out.println("Press 6 to return to the main menu");
	}

}
