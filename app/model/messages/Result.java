package model.messages;

import java.util.List;

import akka.actor.ActorRef;

public class Result {
	private String inputWord;
	private List<String> output;
	private ActorRef sender;
	public Result(String inputWord, List<String> output, ActorRef sender) {
		this.inputWord = inputWord;
		this.output = output;
		this.sender = sender;
	}
	public String getInputWord() {
		return inputWord;
	}
	public List<String> getOutput() {
		return output;
	}
	public ActorRef getSender() {
		return sender;
	}
}
