package actors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import akka.actor.AbstractActor;
import messages.Dictionary;

public class DictionaryReaderActor extends AbstractActor {
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Path.class, msg -> getSender().tell(readDic(msg), getSelf()))
				.build();
	}
	
	private Dictionary readDic(Path path) {
		Dictionary dic = null;
		try {
			List<String> l = Files.readAllLines(path);
			dic = new Dictionary(l);
		} catch (IOException e) {
			// TODO Tratar erro com o Akka
			e.printStackTrace();
		}
		return dic;
	}
}