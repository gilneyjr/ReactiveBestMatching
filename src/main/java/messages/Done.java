package messages;

public class Done {
	private int numberOfResults;
	public Done(int numberOfResults) {
		this.numberOfResults = numberOfResults;
	}
	public int getNumberOfResults() {
		return numberOfResults;
	}
}
