package de.tum.aat.rest.student;

import org.restlet.data.MediaType;
import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import de.tum.aat.domain.Student;
import de.tum.aat.exceptions.NotFoundException;
import de.tum.aat.logic.QRGenerator;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class PresentationResource extends ServerResource {

	private static final QRGenerator QR = new QRGenerator();

	@Get("image/png")
	public Representation getPresentationQR() {

		long id = Long.MIN_VALUE;

		try {
			id = Long.parseLong(getAttribute("id"));
		} catch (Exception e) {
			throw new NotFoundException(Student.class);
		}

		byte[] data = QRCode.from(QR.generatePresentation(id)).to(ImageType.PNG).stream().toByteArray();

		ByteArrayRepresentation bar = new ByteArrayRepresentation(data, MediaType.IMAGE_PNG);
		getResponse().setEntity(bar);

		return bar;
	}

}
