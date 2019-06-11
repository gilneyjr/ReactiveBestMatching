package messages;

import java.util.List;

public class Input {
	private List<String> words;
	public Input(List<String> words) {
		this.words = words;
	}
	public List<String> getWords() {
		return this.words;
	}
}