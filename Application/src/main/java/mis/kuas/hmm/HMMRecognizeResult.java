package mis.kuas.hmm;

public class HMMRecognizeResult {

	public static final HMMRecognizeResult UNKNOWN_RESULT = new HMMRecognizeResult(-1, Double.NaN);

	private int state;
	
	private double value;
	
	public HMMRecognizeResult(int state, double value) {
		this.state = state;
		this.value = value;
	}

	public int getState() {
		return state;
	}

	public double getValue() {
		return value;
	}
	
	public String toString() {
		if (Double.isInfinite(value) || Double.isNaN(value)) {
			return "Recognization Result : State = unknown";
		}
		return "Recognization Result : State = " + state + ", value = " + value;
	}

	public boolean isUnknown() {
		return Double.isNaN(this.value);
	}
}
