package ar.edu.itba.pod.hz.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by giulianoscaglioni on 28/10/18.
 */
public class AirportTuple implements DataSerializable {
    private String airport1;
    private String airport2;

    // for hazelcast
    public AirportTuple() {
        super();
        airport1="";
        airport2="";
    }

    public AirportTuple(String airport1, String airport2) {
        this.airport1 = airport1;
        this.airport2 = airport2;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(airport1);
        objectDataOutput.writeUTF(airport2);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        airport1 = objectDataInput.readUTF();
        airport2 = objectDataInput.readUTF();
    }

    public String getAirport1() {
        return airport1;
    }

    public void setAirport1(String airport1) {
        this.airport1 = airport1;
    }

    @Override
    public String toString() {
        return "AirportTuple{" +
                "airport1='" + airport1 + '\'' +
                ", airport2='" + airport2 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof AirportTuple)){
            return false;
        }
        return this.airport1.equals(((AirportTuple) obj).getAirport1()) &&
                this.airport2.equals(((AirportTuple) obj).getAirport2());
    }

    @Override
    public int hashCode() {
        return ("1"+airport1+"/"+airport2+"2").hashCode();
    }

    public String getAirport2() {
        return airport2;
    }

    public void setAirport2(String airport2) {
        this.airport2 = airport2;
    }
}
