package de.tum.aat.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseTimeslot {

	private static final SimpleDateFormat TIMESLOT_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	private Date start;
	private Date end;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "ExerciseTimeslot [start=" + TIMESLOT_FORMAT.format(start) + ", end=" + TIMESLOT_FORMAT.format(end)
				+ "]";
	}

}
