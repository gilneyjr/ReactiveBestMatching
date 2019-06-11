package actors;

import akka.actor.AbstractActor;

public class AggregateActor extends AbstractActor {
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.build();
	}
}
