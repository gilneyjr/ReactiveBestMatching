package messages;

import java.util.List;

public class Result {
	private String inputWord;
	private List<String> words;
	public Result(String inputWord, List<String> words) {
		this.inputWord = inputWord;
		this.words = words;
	}
	public String getInputWord() {
		return this.inputWord;
	}
	public List<String> getWords() {
		return words;
	}
}
