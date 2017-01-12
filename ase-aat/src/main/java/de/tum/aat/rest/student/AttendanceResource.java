package de.tum.aat.rest.student;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.NotFoundException;
import de.tum.aat.logic.QRGenerator;

public class AttendanceResource extends ServerResource {

	private static final QRGenerator QR = new QRGenerator();

	@Get("json")
	public String getAttendanceQR() {

		long id = Long.MIN_VALUE;

		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NotFoundException(Student.class);
		}

		// byte[] data =
		// QRCode.from(QR.generateAttendance(id)).to(ImageType.PNG).stream().toByteArray();
		//
		// ByteArrayRepresentation bar = new ByteArrayRepresentation(data,
		// MediaType.IMAGE_PNG);
		// getResponse().setEntity(bar);
		//
		// return bar;

		return "{\"url\":\"" + QR.generateAttendance(id) + "\"}";

	}

}
