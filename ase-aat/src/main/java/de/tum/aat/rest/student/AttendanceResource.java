package de.tum.aat.rest.student;

import org.restlet.data.MediaType;
import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.logic.QRGenerator;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class AttendanceResource extends ServerResource {

	private static final QRGenerator QR = new QRGenerator();

	@Get("image/png")
	public Representation getAttendanceQR() {

		long id = Long.MIN_VALUE;

		try {
			id = Long.parseLong(getAttribute("id"));

		} catch (Exception e) {
			// TODO: create NoIdProvidedException
		}

		byte[] data = QRCode.from(QR.generateAttendance(id)).to(ImageType.PNG).stream().toByteArray();

		ByteArrayRepresentation bar = new ByteArrayRepresentation(data, MediaType.IMAGE_PNG);
		getResponse().setEntity(bar);

		return bar;
	}

}
