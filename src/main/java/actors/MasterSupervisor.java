package actors;

import java.nio.file.Path;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import messages.Dictionary;
import messages.Done;
import messages.Similarity;
import messages.LoadDictionary;
import messages.Pair;
import messages.Input;

import akka.pattern.Patterns;

public class MasterSupervisor extends AbstractActor {
	private List<String> dic;
	private ActorRef databaseActor;
	private ActorRef calculateSimilarityActor;
	private ActorRef aggregateActor; 
	
	public MasterSupervisor() {
		this.aggregateActor = getContext().actorOf(AggregateActor.props());
		this.calculateSimilarityActor = getContext().actorOf(CalculateSimilarityActor.props());
		this.databaseActor = getContext().actorOf(DatabaseActor.props());
	}
	
	// REMOVE AFTER (BEGIN)
	private Input input;
	private int counter = 0;
	// REMOVE AFTER (END)
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(LoadDictionary.class, msg -> databaseActor.tell(msg, getSelf()))
				.match(Dictionary.class, dic -> loadDictionary(dic))
				
				.match(Input.class, input -> {
					this.input = input; // REMOVE AFTER
					input.getWords().stream().forEach(inputWord -> {
						dic.stream().forEach(dicWord -> {
							calculateSimilarityActor.tell(
									new Pair(dicWord, inputWord), 
									getSelf());
						});
					});
				})
				.match(Similarity.class, sim -> {
					aggregateActor.tell(sim, getSelf());
				})
				.build();		
		
//		return receiveBuilder()
//				.match(ReadDictionary.class, init -> init(init.getDic(), init.getInput()))
//				.match(Dictionary.class, dic -> saveDictionary(dic.getWords()))
//				.match(Input.class, input -> calculateDistance(input.getWords()))
//				.match(Distance.class, dist -> aggregate(dist))
//				.build();
	}
	
	private void loadDictionary(Dictionary dic) {
		this.dic = dic.getWords();
	}
}
