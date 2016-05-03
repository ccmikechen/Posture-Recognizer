package mis.kuas.data;

import android.graphics.Bitmap;
import android.util.Log;

import mis.kuas.sensor.Direction;
import mis.kuas.sensor.NikeSensor;
import mis.kuas.sensor.SensorPoints;
import mis.kuas.sensor.ShoePoint;


public class NikeDataGetter implements DataGetterInterface {
	
	protected Direction direction;

	private SensorPoints sensorPoints = null;

	protected float pa, pb, pc, pd;

	protected float x, y, z;

	private boolean keepShowData = false;

	private byte[][] shoeBaseMask;

	private Bitmap shoeBaseCover;

	public NikeDataGetter(Direction direction) {
		this(direction, false);
	}
	
	public NikeDataGetter(Direction direction, boolean showData) {
		this.direction = direction;
		ShoePoint[] points = NikeSensor.getSensorPoints(direction);
		this.sensorPoints = new SensorPoints(NikeSensor.getShoeBaseWidth(),
											 NikeSensor.getShoeBaseHeight(),
											 points);
		shoeBaseMask = NikeSensor.getShoeBaseMask(direction);
		shoeBaseCover = NikeSensor.getShoeBaseCover(direction);
		this.keepShowData = showData;
	}
	
	@Override
	public void dataCallBack(float[] signals) {
		x = signals[0] * 1280 / 4096;
		y = signals[1] * 1280 / 4096;
		z = signals[2] * 1280 / 4096;
		pa = signals[3];
		pb = signals[4];
		pc = signals[5];
		pd = signals[6];
		if (keepShowData)
			showData(signals);
	}
	
	private void showData(float[] signals) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%.2f   ",
				Math.sqrt(signals[0] * signals[0] +
						signals[1] * signals[1] +
						signals[2] * signals[2])));
		for (int i = 0; i <= 2; i++)
			if (signals[i] < 0)
				sb.append(String.format("%.1f  ", signals[i]));
			else
				sb.append(String.format(" %.1f  ", signals[i]));
		for (int i = 3; i <= 6; i++)
			if (signals[i] < 0)
				sb.append(String.format("%8.0f", signals[i]));
			else
				sb.append(String.format(" %8.0f", signals[i]));
		sb.append("\n");
		Log.e("DATA", new String(sb));
	}
	
	synchronized public float getX() {
		return x;
	}
	
	synchronized public float getY() {
		return y;
	}
	
	synchronized public float getZ() {
		return z;
	}
	
	synchronized public float getA() {
		return pa;
	}
	
	synchronized public float getB() {
		return pb;
	}
	
	synchronized public float getC() {
		return pc;
	}
	
	synchronized public float getD() {
		return pd;
	}
	
	synchronized public float getG() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public ShoePoint getPointA() {
		return sensorPoints.getPointA();
	}
	
	public ShoePoint getPointB() {
		return sensorPoints.getPointB();
	}
	
	public ShoePoint getPointC() {
		return sensorPoints.getPointC();
	}
	
	public ShoePoint getPointD() {
		return sensorPoints.getPointD();
	}

	public byte[][] getShoeBaseMask() {
		return shoeBaseMask;
	}

	public Bitmap getShoeBaseCover() {
		return shoeBaseCover;
	}

	public int getWidth() {
		return sensorPoints.getWidth();
	}
	
	public int getHeight() {
		return sensorPoints.getHeight();
	}
	
	synchronized public ShoePoint getCenterOfPressurePoint() {
		return NikeSensor.getCenterOfPressurePoint(direction, pa, pb, pc, pd);
	}

	public void close() {
	}

}
