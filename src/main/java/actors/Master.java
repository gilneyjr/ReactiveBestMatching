package actors;

import java.nio.file.Path;
import java.util.List;

import akka.actor.AbstractActor;
import messages.Dictionary;
import messages.Distance;
import messages.Init;
import messages.Input;

public class Master extends AbstractActor {
	private List<String> dic = null;
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Init.class, init -> init(init.getDic(), init.getInput()))
				.match(Dictionary.class, dic -> saveDictionary(dic.getWords()))
				.match(Input.class, input -> calculateDistance(input.getWords()))
				.match(Distance.class, dist -> aggregate(dist))
				.build();
	}
	
	private void init(Path dic, Path input) {
		
	}
	
	private void saveDictionary(List<String> dic) {
		this.dic = dic;
	}
	
	
	private void calculateDistance(List<String> input) {
		
	}
	
	private void aggregate(Distance dist) {
		
	}
}
