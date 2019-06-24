package actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import messages.Dictionary;
import messages.Input;
import messages.LoadDictionary;
import messages.Output;
import messages.Result;
import messages.Pair;
import messages.Request;
import messages.Calculate;
import messages.Similarity;

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
				.match(LoadDictionary.class, msg -> databaseActor.tell(msg, getSelf()))
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
	}
	
	private void calculate(Calculate resp) {
		for(String dicWord : dictionary)
			similarityRouter.route(new Pair(dicWord, resp.getInputWord()), getSelf());
	}
}

