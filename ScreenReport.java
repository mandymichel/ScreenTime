import static java.lang.System.out;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;

public class ScreenReport {
	private static final ScreenEventDAO screenEventDAO = ScreenEventDAO.getInstance();
	private static final ScreenActivityDAO screenActivityDAO = ScreenActivityDAO.getInstance();

	Scanner keyboard = new Scanner(System.in);

	public void reportNotes() {
		out.println("Which child's notes do you want in the report?");
		String childNotes = keyboard.next();
		List<ScreenEvent> notesList = new ArrayList<>();
		for (ScreenEvent t : screenEventDAO.getEvents()) {
			if ((childNotes.equalsIgnoreCase(t.getFirstName())) && (!t.getNotes().equals(""))) {
				notesList.add(t);
			}
		}
		out.println("Here is the notes report for " + childNotes + ": ");
		for (ScreenEvent s : notesList) {
			out.println(s.toFormattedString());
		}
	}

	public void reportEd() {
		out.println("Enter start date: ");
		String sd = keyboard.next();
		out.println("Enter end date: ");
		keyboard.nextLine();
		String ed = keyboard.next();
		out.println("Enter child's name: ");
		String firstNameRequest = keyboard.next();
		int elapsedSecs = 0;
		int totalYesSecs = 0;
		float totalYesHours = 0.0f;
		int totalNoSecs = 0;
		int totalNoHours = 0;
		for (ScreenEvent event : screenEventDAO.getEvents()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			Date d1 = null;
			Date d2 = null;
			Date currentDate = null;

			try {
				d1 = format.parse(sd);
				d2 = format.parse(ed);
				currentDate = format.parse(event.getStartDateTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateTime dt1 = new DateTime(d1);
			DateTime dt2 = new DateTime(d2);
			DateTime currentDT = new DateTime(currentDate);
			ScreenActivity searchedActivity = screenActivityDAO.findActivityByEd("y");
			boolean edIsYes = searchedActivity.getActivityEd().equals("y");
			boolean childIsSame = event.getFirstName().equalsIgnoreCase(firstNameRequest);
			if (isWithinRange(currentDT, dt1, dt2) && edIsYes && childIsSame) {
				out.println(event.toFormattedString());
				elapsedSecs = turnElapsedToSecs(event.getElapsedTime());
				totalYesSecs = totalYesSecs + elapsedSecs;
				totalYesHours = totalYesSecs / 3600;
			} else if (isWithinRange(currentDT, dt1, dt2) && !edIsYes && childIsSame) {
				out.println(event.toFormattedString());
				elapsedSecs = turnElapsedToSecs(event.getElapsedTime());
				totalNoSecs = totalNoSecs + elapsedSecs;
				totalNoHours = totalNoSecs / 3600;
			}
		}
		out.println("Total Educational hours: " + totalYesHours);
		out.println("Total Non-educational hours: " + totalNoHours);
	}

	public boolean isWithinRange(DateTime currentDT, DateTime sd, DateTime ed) {
		return !(currentDT.isBefore(sd) || currentDT.isAfter(ed));
	}

	public void reportActivity() {
		String sd = null;
		String ed = null;
		ScreenEventMenu sem = new ScreenEventMenu();
		do {
			out.println("Enter start date: ");
			sd = keyboard.next();
		} while (!sem.validDate(sd));
		do {
			out.println("Enter end date: ");
			keyboard.nextLine();
			ed = keyboard.next();
		} while (!sem.validDate(ed));
		out.println("Enter activity type(video, game, or conversation): ");
		String activityRequest = keyboard.next();
		out.println("Enter child's name: ");
		String firstNameRequest = keyboard.next();
		int elapsedSecs = 0;
		int totalSecs = 0;
		float totalHours = 0.0f;

		for (ScreenEvent event : screenEventDAO.getEvents()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			Date d1 = null;
			Date d2 = null;
			Date currentDate = null;

			try {
				d1 = format.parse(sd);
				d2 = format.parse(ed);
				currentDate = format.parse(event.getStartDateTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateTime dt1 = new DateTime(d1);
			DateTime dt2 = new DateTime(d2);
			DateTime currentDT = new DateTime(currentDate);
			ScreenActivity searchedActivity = screenActivityDAO.findActivityByType(activityRequest);
			boolean actIsSame = searchedActivity.getActivityType().equals(activityRequest);
			boolean childIsSame = event.getFirstName().equals(firstNameRequest);
			if (isWithinRange(currentDT, dt1, dt2) && actIsSame && childIsSame) {
				out.println(event.toFormattedString());
				elapsedSecs = turnElapsedToSecs(event.getElapsedTime());
				totalSecs = totalSecs + elapsedSecs;
				totalHours = (float) totalSecs / 3600;
			}
		}
		out.println("Total hours: " + totalHours);
	}

	private int turnElapsedToSecs(String elapsedTime) {
		String noLetters = elapsedTime.replaceAll("[dayshoursminutesseconds]", "");
		String noSpaces = noLetters.replaceAll(" ", "");
		String[] strArray = noSpaces.split("/");
		String d = strArray[0];
		String h = strArray[1];
		String m = strArray[2];
		String s = strArray[3];
		int days = Integer.parseInt(d);
		int hours = Integer.parseInt(h);
		int minutes = Integer.parseInt(m);
		int seconds = Integer.parseInt(s);
		int totalSeconds = seconds + (minutes * 60) + (hours * 60 * 60);
		return totalSeconds;
	}

	public String[] makeDateArray(ScreenEvent s) {
		String[] strArray = null;
		String noChars = s.getStartDateTime().replaceAll("[:]", "-");
		strArray = noChars.split("-");
		String year = strArray[0];
		String month = strArray[1];
		String day = strArray[2].substring(0, 2);
		String hour = strArray[2].substring(2, 4);
		String minute = strArray[3];
		String second = strArray[4];
		return strArray;
	}

	public void printActivityMonthReport(String searchChild, String searchYear, String type) throws ParseException {
		int elapsedSecs = 0;
		int totalSecs = 0;
		float totalHours = 0.0f;
		int dateNumber = 1;
		for (int i = 1; i < 13; i++) {
			String monthString = new DateFormatSymbols().getMonths()[dateNumber - 1];
			out.println("Month " + monthString + ":");
			for (ScreenEvent event : screenEventDAO.getEvents()) {
				if (event.getFirstName().equals(searchChild)) {
					ScreenActivity searchedActivity = screenActivityDAO.findActivityByActID(event.getActID());
					if (searchedActivity.getActivityType().equals(type)) {
						String[] strArray = makeDateArray(event);
						DateTime currentDate = null;
						try {
							currentDate = makeDateObject(event.getStartDateTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String sdString = searchYear + "-01-0101:00:00";
						String edString = searchYear + "-12-3123:59:59";
						DateTime sd = makeDateObject(sdString);
						DateTime ed = makeDateObject(edString);
						boolean isSameYear = isWithinRange(currentDate, sd, ed);
						if ((strArray[1].equals(dateNumber) || strArray[1].equals("0" + dateNumber)) && isSameYear) {
							out.println(event.toFormattedString());
							elapsedSecs = turnElapsedToSecs(event.getElapsedTime());
							totalSecs = totalSecs + elapsedSecs;
							totalHours = (float) totalSecs / 3600;
						}
					}
				}
			}
			dateNumber++;
		}
		out.println("Total hours spent on " + type + "s: " + totalHours);
	}

	public DateTime makeDateObject(String d) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date d1 = null;
		d1 = format.parse(d);
		DateTime dt1 = new DateTime(d1);
		return dt1;
	}

	public void reportActivityIDMonth() throws ParseException {
		out.println("Type the child's name: ");
		String searchChild = keyboard.next();
		String searchYear = null;
		do {
			out.println("Type the year: ");
			searchYear = keyboard.next();
		} while (!searchYear.matches("\\d{4}"));
		out.println("Video report by month: ");
		printActivityMonthReport(searchChild, searchYear, "video");
		out.println("Game report by month: ");
		printActivityMonthReport(searchChild, searchYear, "game");
		out.println("Conversation report by month: ");
		printActivityMonthReport(searchChild, searchYear, "conversation");
	}
}
