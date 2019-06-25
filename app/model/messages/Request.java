package model.messages;

import akka.actor.ActorRef;

public class Request {
	private String inputWord;
	private ActorRef sender;
	public Request(String inputWord, ActorRef sender) {
		this.inputWord = inputWord;
		this.sender = sender;
	}
	public String getInputWord() {
		return this.inputWord;
	}
	public ActorRef getSender() {
		return this.sender;
	}
}
