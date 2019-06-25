package model.messages;

public class Pair {
	private String dicWord;
	private String inputWord;
	public Pair(String dicWord, String inputWord) {
		this.dicWord = dicWord;
		this.inputWord = inputWord;
	}
	public String getDicWord() {
		return dicWord;
	}
	public String getInputWord() {
		return inputWord;
	}
}
