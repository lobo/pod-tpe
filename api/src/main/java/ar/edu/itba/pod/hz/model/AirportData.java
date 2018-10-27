package ar.edu.itba.pod.hz.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class AirportData implements DataSerializable {
	
	private String oaci;
	private String iata;
	private String denominacion;
	private String provincia;

    // mantener el orden que se hizo en el write!
    @Override
    public void readData(final ObjectDataInput in) throws IOException {
    	oaci = in.readUTF();
    	iata = in.readUTF();
		denominacion = in.readUTF();
		provincia = in.readUTF();
    }

    // mantener el orden que se hizo en el read!
    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeUTF(oaci);
		out.writeUTF(iata);
		out.writeUTF(denominacion);
		out.writeUTF(provincia);
    }


	@Override
	public String toString() {
		return "AirportData{" +
				"oaci='" + oaci + '\'' +
				", iata='" + iata + '\'' +
				", denominacion='" + denominacion + '\'' +
				", provincia='" + provincia + '\'' +
				'}';
	}

	public String getOaci() {
		return oaci;
	}

	public String getIata() {
		return iata;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public String getProvincia() {
		return provincia;
	}
}
