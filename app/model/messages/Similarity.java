package model.messages;

public class Similarity {
	private String inputWord;
	private String dicWord;
	private int similarity;
	public Similarity(String inputWord, String dicWord, int similarity) {
		this.inputWord = inputWord;
		this.dicWord = dicWord;
		this.similarity = similarity;
	}
	public String getInputWord() {
		return inputWord;
	}
	public String getDicWord() {
		return dicWord;
	}
	public int getSimilarity() {
		return similarity;
	}
}
