package messages;

public class Done {
	private String word;
	private int numberOfResults;
	public Done(int numberOfResults, String word) {
		this.numberOfResults = numberOfResults;
		this.word = word;
	}
	public String getWord() {
		return this.word;
	}
	public int getNumberOfResults() {
		return numberOfResults;
	}
}
