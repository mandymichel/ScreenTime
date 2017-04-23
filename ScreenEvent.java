public class ScreenEvent {
	private String firstName = null;
	private String startDateTime = null;
	private String endDateTime = null;
	private String notes = null;
	private String elapsedTime = null;
	private int eventID = 0;
	private int actID = 0;

	public ScreenEvent() {

	}

	public ScreenEvent(String firstName, String startDateTime, String endDateTime, String notes, int eventID, int actID,
			String elapsedTime) {
		this.firstName = firstName;
		this.notes = notes;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.eventID = eventID;
		this.actID = actID;
		this.elapsedTime = elapsedTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public int getActID() {
		return actID;
	}

	public void setActID(int actID) {
		this.actID = actID;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s,%s,%d,%d,%s", firstName, startDateTime, endDateTime, notes, eventID, actID,
				elapsedTime);
	}

	public String toFormattedString() {
		return String.format(
				"Name: %s, Start date: %s, End Date: %s, Notes: %s, Event: %d, Activity: %d, Elapsed Time: %s",
				firstName, startDateTime, endDateTime, notes, eventID, actID, elapsedTime);
	}

}
