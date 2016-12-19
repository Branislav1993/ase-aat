package de.tum.aat.rest.student;

import java.io.IOException;
import java.io.OutputStream;

import org.restlet.data.MediaType;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import de.tum.aat.domain.Student;

public class AttendanceResource {

	@Get("image/jpeg")
	public Representation getAttendanceQR(Student s) {
		
		byte[] data = null;

		ObjectRepresentation<byte[]> or = new ObjectRepresentation<byte[]>(data, MediaType.IMAGE_PNG) {
			@Override
			public void write(OutputStream os) throws IOException {
				super.write(os);
				os.write(this.getObject());
			}
		};

		return or;
	}

}
