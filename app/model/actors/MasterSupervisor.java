package model.actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import model.messages.Dictionary;
import model.messages.DictionaryIsLoaded;
import model.messages.Input;
import model.messages.LoadDictionary;
import model.messages.Output;
import model.messages.Result;
import model.messages.Pair;
import model.messages.Request;
import model.messages.Calculate;
import model.messages.Similarity;

public class MasterSupervisor extends AbstractActor {
	// Actors
	private ActorRef databaseActor;
	private ActorRef aggregateActor;
	
	// Router
	private static int N_ROUTEES = 5;
	private Router similarityRouter;
	
	// Dictionary
	private List<String> dictionary;
	
	public MasterSupervisor() {
		this.aggregateActor = getContext().actorOf(AggregateActor.props());
		this.databaseActor = getContext().actorOf(DatabaseActor.props());
		
		List<Routee> routees = new ArrayList<>();
		for(int i = 0; i < N_ROUTEES; i++) {
			ActorRef a = getContext().actorOf(CalculateSimilarityActor.props());
			getContext().watch(a);
			routees.add(new ActorRefRoutee(a));
		}
		this.similarityRouter = new Router(new RoundRobinRoutingLogic(), routees);
	}
	
	public static Props props() {
		return Props.create(MasterSupervisor.class);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(LoadDictionary.class, msg -> databaseActor.tell(new LoadDictionary(getSender()), getSelf()))
				.match(Dictionary.class, this::storeDictionary)
				.match(Input.class, input -> aggregateActor.tell(new Request(input.getInputWord(), getSender()), getSelf()))
				.match(Calculate.class, this::calculate)
				.match(Similarity.class, sim -> aggregateActor.tell(sim, getSelf()))
				.match(Result.class, result -> result.getSender().tell(new Output(result.getInputWord(), result.getOutput()), getSelf()))
				.build();
	}
	
	private void storeDictionary(Dictionary dictionary) {
		this.dictionary = dictionary.getWords();
		aggregateActor.tell(new Integer(this.dictionary.size()), getSelf());
		dictionary.getSender().tell(new DictionaryIsLoaded(), getSelf());
	}
	
	private void calculate(Calculate resp) {
		for(String dicWord : dictionary)
			similarityRouter.route(new Pair(dicWord, resp.getInputWord()), getSelf());
	}
}

