package actors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.Dictionary;
import messages.LoadDictionary;

public class DatabaseActor extends AbstractActor {
	private String path = "data/dictionary.txt";

	private Dictionary loadDictionary() {
		try {
			return new Dictionary(Files.readAllLines(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null; // Suppress return error
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(LoadDictionary.class, msg -> {
					getSender().tell(loadDictionary(), getSelf());
				})
				.build();
	}
	
	public static Props props() {
		return Props.create(DatabaseActor.class);
	}
}
