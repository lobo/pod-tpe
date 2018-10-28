package ar.edu.itba.pod.hz.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class AirportData implements DataSerializable {
	
	private String oaci;
	private String iata;
	private String denomination;
	private String province;

    // mantener el orden que se hizo en el write!
    @Override
    public void readData(final ObjectDataInput in) throws IOException {
    	oaci = in.readUTF();
    	iata = in.readUTF();
		denomination = in.readUTF();
		province = in.readUTF();
    }

    // mantener el orden que se hizo en el read!
    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeUTF(oaci);
		out.writeUTF(iata);
		out.writeUTF(denomination);
		out.writeUTF(province);
    }


	@Override
	public String toString() {
		return "AirportData{" +
				"oaci='" + oaci + '\'' +
				", iata='" + iata + '\'' +
				", denomination='" + denomination + '\'' +
				", province='" + province + '\'' +
				'}';
	}

	public String getOaci() {
		return oaci;
	}

	public String getIata() {
		return iata;
	}

	public String getDenomination() {
		return denomination;
	}

	public String getProvince() {
		return province;
	}

	public void setOaci(String oaci) {
		this.oaci = oaci;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
