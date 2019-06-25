package model.messages;

import akka.actor.ActorRef;

import java.util.List;

public class Dictionary {
	private List<String> words;
	private ActorRef sender;
	public Dictionary(List<String> words, ActorRef sender) {
		this.words = words;
		this.sender = sender;
	}
	public List<String> getWords() {
		return this.words;
	}
	public ActorRef getSender() {
		return this.sender;
	}
}