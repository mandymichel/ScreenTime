
import java.util.Comparator;

public class CustomSort implements Comparator<ScreenEvent> {

	@Override
	public int compare(ScreenEvent e1, ScreenEvent e2) {
		// Assume no nulls, and simple ordinal comparisons

		// First by start time.
		int dateResult = e1.getStartDateTime().compareTo(e2.getStartDateTime());
		if (dateResult != 0) {
			return dateResult;
		}

		// Next by child name
		int childNameResult = e1.getFirstName().compareTo(e2.getFirstName());
		if (childNameResult != 0) {
			return childNameResult;
		}

		// Finally by notes
		return e1.getNotes().compareTo(e2.getNotes());
	}

}
