package mis.kuas.data;

public class PressureDataModel implements PressureDataGetter {

    private short a;

    private short b;

    private short c;

    private short d;

    @Override
    public short getA() {
        return a;
    }

    @Override
    public short getB() {
        return b;
    }

    @Override
    public short getC() {
        return c;
    }

    @Override
    public short getD() {
        return d;
    }

    public void setA(short a) {
        this.a = a;
    }

    public void setB(short b) {
        this.b = b;
    }

    public void setC(short c) {
        this.c = c;
    }

    public void setD(short d) {
        this.d = d;
    }
}
