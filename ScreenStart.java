import static java.lang.System.out;

import java.util.Scanner;

public class ScreenStart {
	private static final int REPORT_MENU = 3;
	private static final int EVENT_MENU = 2;
	private static final int ACTIVITY_MENU = 1;
	Scanner keyboard = new Scanner(System.in);

	public void printMenu() {
		out.println("Main Menu");
		out.println("1. Activities");
		out.println("2. Events");
		out.println("3. Reports");
		out.println("0. Exit");
	}

	public void run() {
		int choice = 0;
		do {
			printMenu();
			choice = keyboard.nextInt();
			switch (choice) {
			case ACTIVITY_MENU:
				navigateToActivitiesMenu();
				break;
			case EVENT_MENU:
				navigateToEventsMenu();
				break;
			case REPORT_MENU:
				navigateToReportMenu();
				break;
			default:
				choice = 0;
				break;
			}
		} while (0 != choice);
		out.println("Goodbye!");
	}

	private void navigateToEventsMenu() {
		ScreenEventMenu e = new ScreenEventMenu();
		e.printEventMenu();
	}

	private void navigateToActivitiesMenu() {
		ScreenActivityMenu a = new ScreenActivityMenu();
		a.printActivityMenu();
	}

	private void navigateToReportMenu() {
		ScreenReportMenu r = new ScreenReportMenu();
		r.report();
	}

	public static void main(String[] args) {
		ScreenStart s = new ScreenStart();
		s.run();
	}

}
