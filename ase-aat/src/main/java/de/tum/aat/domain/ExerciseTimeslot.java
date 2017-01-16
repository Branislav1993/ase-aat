package de.tum.aat.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseTimeslot implements Serializable {

	private static final long serialVersionUID = -4258752755633898996L;

	private static final SimpleDateFormat TIMESLOT_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	private Date start;
	private Date end;

	public ExerciseTimeslot(long start, long end) {
		this.start = new Date(start);
		this.end = new Date(end);
	}

	public ExerciseTimeslot() {

	}

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExerciseTimeslot other = (ExerciseTimeslot) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExerciseTimeslot [start=" + TIMESLOT_FORMAT.format(start) + ", end=" + TIMESLOT_FORMAT.format(end)
				+ "]";
	}

}
