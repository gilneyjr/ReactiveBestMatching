package model.messages;

import akka.actor.ActorRef;

public class LoadDictionary {
	private ActorRef sender;

	public LoadDictionary(ActorRef sender) {
		this.sender = sender;
	}
	public ActorRef getSender() {
		return this.sender;
	}
}
