package actors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import akka.actor.AbstractActor;
import messages.Input;

public class InputReaderActor extends AbstractActor {
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Path.class, msg -> getSender().tell(readInput(msg), getSelf()))
				.build();
	}
	
	private Input readInput(Path path) {
		Input input = null;
		try {
			List<String> l = Files.readAllLines(path);
			input = new Input(l);
		} catch (IOException e) {
			// TODO Tratar erro com o Akka
			e.printStackTrace();
		}
		return input;
	}
}