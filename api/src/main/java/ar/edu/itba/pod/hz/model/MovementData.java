package ar.edu.itba.pod.hz.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class MovementData implements DataSerializable {
	
	private String clasification;
	private String type;
	private String originOACI;
	private String destOACI;

    // mantener el orden que se hizo en el write!
    @Override
    public void readData(final ObjectDataInput in) throws IOException {
		clasification = in.readUTF();
		type = in.readUTF();
		originOACI = in.readUTF();
		destOACI = in.readUTF();
    }

    // mantener el orden que se hizo en el read!
    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeUTF(clasification);
		out.writeUTF(type);
		out.writeUTF(originOACI);
		out.writeUTF(destOACI);
    }


	@Override
	public String toString() {
		return "MovementData{" +
				"clasification='" + clasification + '\'' +
				", type='" + type + '\'' +
				", originOACI='" + originOACI + '\'' +
				", destOACI='" + destOACI + '\'' +
				'}';
	}

	public String getClasification() {
		return clasification;
	}

	public String getType() {
		return type;
	}

	public String getOriginOACI() {
		return originOACI;
	}

	public String getDestOACI() {
		return destOACI;
	}

	public void setClasification(String clasification) {
		this.clasification = clasification;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOriginOACI(String originOACI) {
		this.originOACI = originOACI;
	}

	public void setDestOACI(String destOACI) {
		this.destOACI = destOACI;
	}
}
