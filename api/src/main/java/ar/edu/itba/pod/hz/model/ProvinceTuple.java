package ar.edu.itba.pod.hz.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by giulianoscaglioni on 28/10/18.
 */
public class ProvinceTuple implements DataSerializable {
    private String province1;
    private String province2;

    public ProvinceTuple(String origin, String dest) {
        if(origin.compareTo(dest) < 0) {
            this.province1 = origin;
            this.province2 = dest;
        } else {
            this.province1 = dest;
            this.province2 = origin;
        }
    }

    //for hazelcast
    public ProvinceTuple() {
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(province1);
        objectDataOutput.writeUTF(province2);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        province1 = objectDataInput.readUTF();
        province2 = objectDataInput.readUTF();
    }

    public String getProvince1() {
        return province1;
    }

    public void setProvince1(String province1) {
        this.province1 = province1;
    }

    public String getProvince2() {
        return province2;
    }

    public void setProvince2(String province2) {
        this.province2 = province2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProvinceTuple that = (ProvinceTuple) o;

        if (!province1.equals(that.province1)) return false;
        return province2.equals(that.province2);
    }

    @Override
    public int hashCode() {
        int result = province1.hashCode();
        result = 31 * result + province2.hashCode();
        return result;
    }
}
