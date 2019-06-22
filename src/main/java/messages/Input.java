package messages;

import java.util.List;

public class Input {
	private List<String> words;
	private int qnt;
	public Input(List<String> words, int qnt) {
		this.words = words;
	}
	public List<String> getWords() {
		return this.words;
	}
	public int getQnt() {
		return this.qnt;
	}
}