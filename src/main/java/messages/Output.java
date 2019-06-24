package messages;

import java.util.List;

public class Output {
	private String inputWord;
	private List<String> output;
	public Output(String inputWord, List<String> output) {
		this.inputWord = inputWord;
		this.output = output;
	}
	public String getInputWord() {
		return inputWord;
	}
	public List<String> getOutput() {
		return output;
	}
}
