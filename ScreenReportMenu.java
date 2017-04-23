import static java.lang.System.out;

import java.text.ParseException;
import java.util.Scanner;

public class ScreenReportMenu {
	Scanner keyboard = new Scanner(System.in);
	ScreenReport sr = new ScreenReport();

	public void printReportMenu() {
		out.println("Report Menu");
		out.println("1. Child Total Time by Activity Type");
		out.println("2. Child Total Time by Educational Value");
		out.println("3. Child Time by Activity Type, Monthly for a Year");
		out.println("4. Notes for a Child");
		out.println("Press 5 to return to the main menu.");
	}

	public void report() {
		printReportMenu();
		int reportChoice = keyboard.nextInt();
		switch (reportChoice) {
		case 1:
			sr.reportActivity();
			break;
		case 2:
			sr.reportEd();
			break;
		case 3:
			try {
				sr.reportActivityIDMonth();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			sr.reportNotes();
			break;
		default:
			reportChoice = 0;
			break;
		}
	}
}