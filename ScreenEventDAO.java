import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ScreenEventDAO {
	Scanner keyboard = new Scanner(System.in);
	private static ScreenEventDAO instance;

	private ScreenEventDAO() {

	}

	public static ScreenEventDAO getInstance() {
		if (instance == null) {
			instance = new ScreenEventDAO();
			instance.scanEventFile();
		}
		return instance;
	}

	public List<ScreenEvent> events = new ArrayList<>();
	private int primaryKey;

	public void addEvent(ScreenEvent event) {
		event.setEventID(primaryKey++);
		events.add(event);
		String newEvent = event.toString();
		ReadWriteFiles.writeToFile(newEvent, "Events.txt");
	}

	public List<ScreenEvent> getEvents() {
		return events;
	}

	private void scanEventFile() {
		ScreenEvent event = null;
		String[] strArray = null;
		List<String> list = new ArrayList<String>();
		String eventsList = ReadWriteFiles.readFile("events.txt");
		String eachline = null;
		primaryKey = 0;
		Scanner inputScanner = new Scanner(eventsList);
		while (inputScanner.hasNext()) {
			eachline = inputScanner.nextLine();
			strArray = eachline.split(",");
			list = Arrays.asList(strArray);
			String firstName = list.get(0);
			String eventIDString = list.get(4);
			String actIDString = list.get(5);
			int eventID = 0;
			int actID = 0;
			try {
				eventID = Integer.parseInt(eventIDString);
				if (primaryKey <= eventID) {
					primaryKey = eventID + 1;
				}
				actID = Integer.parseInt(actIDString);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String notes = list.get(3);
			String startDateTime = list.get(1);
			String endDateTime = list.get(2);
			String elapsedTime = list.get(6);
			event = new ScreenEvent(firstName, startDateTime, endDateTime, notes, eventID, actID, elapsedTime);
			events.add(event);
		}
		try {
			inputScanner.close();
		} catch (Exception e) {
		}
	}

	public String rebuildStringAfterModOrDelete(List<ScreenEvent> events) {
		StringBuilder s = new StringBuilder();
		for (ScreenEvent t : events) {
			String tString = t.toString();
			if (tString.length() > 0) {
				s.append(tString.trim());
				s.append("\n");
			}
		}

		return s.toString();
	}

	public void delEvent(ScreenEvent eventToRemove) {
		Iterator<ScreenEvent> iter = events.iterator();
		while (iter.hasNext()) {
			if (iter.next().getEventID() == eventToRemove.getEventID()) {
				iter.remove();
			}
		}
		saveEvents();
	}

	public void delEvent(int id) {
		Iterator<ScreenEvent> iter = events.iterator();
		while (iter.hasNext()) {
			if (iter.next().getEventID() == id) {
				iter.remove();
			}
		}
		saveEvents();
	}

	public ScreenEvent findEventByID(int id) {
		Iterator<ScreenEvent> iter = events.iterator();
		while (iter.hasNext()) {
			ScreenEvent next = iter.next();
			if (next.getEventID() == id) {
				return next;
			}
		}
		return null;
	}

	public void saveEvents() {
		List<String> contents = new ArrayList<String>();
		for (ScreenEvent event : events) {
			contents.add(event.toString());
		}
		ReadWriteFiles.writeListOfStringsToFileAfterDelete(contents, "Events.txt");
	}

	public void save(ScreenEvent s) {
		for (int i = 0; i < events.size(); i++) {
			ScreenEvent screenEvent = events.get(i);
			if (screenEvent.getEventID() == s.getEventID()) {
				events.set(i, s);
			}
		}
		saveEvents();
	}

	public List<ScreenEvent> findByNameAndDate(String searchName, String searchStartDate) {
		List<ScreenEvent> searchResults = new ArrayList<>();
		for (ScreenEvent t : events) {
			String firstName = t.getFirstName();
			String startDateTime = t.getStartDateTime();
			boolean startDateMatches = startDateTime.equals(searchStartDate);
			boolean firstNameMatches = firstName.equalsIgnoreCase(searchName);
			if (firstNameMatches && startDateMatches) {
				searchResults.add(t);
			}
		}
		return searchResults;
	}

	public List<ScreenEvent> findByStartDate(String searchStartDate) {
		List<ScreenEvent> searchResults = new ArrayList<>();
		for (ScreenEvent t : events) {
			if (searchStartDate.equals(t.getStartDateTime())) {
				searchResults.add(t);
			}
		}
		return searchResults;
	}
}
