package actors;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import messages.Dictionary;

public class ReadDicActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(String.class, msg ->
					getSender().tell(readDic(msg), getSelf()))
				.build();
	}
	
	private Dictionary readDic(String path) {
		// TODO: Terminar leitura de dicionário
		return null;
	}

}
