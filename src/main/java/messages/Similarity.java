package messages;

public class Similarity {
	private String dic;
	private String input;
	private int dist;
	public Similarity(String dic, String input, int dist) {
		super();
		this.dic = dic;
		this.input = input;
		this.dist = dist;
	}
	public String getDic() {
		return dic;
	}
	public String getInput() {
		return input;
	}
	public int getDist() {
		return this.dist;
	}
}
