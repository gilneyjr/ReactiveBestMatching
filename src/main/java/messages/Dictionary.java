package messages;

import java.util.List;

public class Dictionary {
	private List<String> words;
	public Dictionary(List<String> words) {
		this.words = words;
	}
	public List<String> getWords() {
		return this.words;
	}
}