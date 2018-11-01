package ar.edu.itba.pod.hz.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class BiIntegerTuple implements DataSerializable, Comparable<BiIntegerTuple> {
    private Long number1 = 0l;
    private Long number2 = 0l;


    public BiIntegerTuple() {
        super();
    }

    public BiIntegerTuple(Long number1, Long number2) {
        this.number1 = number1;
        this.number2 = number2;
    }
    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(String.valueOf(number1));
        objectDataOutput.writeUTF(String.valueOf(number2));
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        number1 = Long.valueOf( objectDataInput.readUTF());
        number2 = Long.valueOf( objectDataInput.readUTF());
    }

    public void setNumber1(Long number1) {
        this.number1 = number1;
    }

    public void setNumber2(Long number2) {
        this.number2 = number2;
    }

    public Long getNumber1() {
        return number1;
    }

    public Long getNumber2() {
        return number2;
    }

    public void addTuple(BiIntegerTuple tuple){
        this.number1+=tuple.getNumber1();
        this.number2+=tuple.getNumber2();
    }

    @Override
    public int compareTo(BiIntegerTuple o) {
        if(this.number1.equals(o.getNumber1()))
            return this.number2.compareTo(o.getNumber2());
        else
            return this.number1.compareTo(o.getNumber1());

    }
}
