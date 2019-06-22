package actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.Done;
import messages.Result;
import messages.Similarity;

public class AggregateActor extends AbstractActor {
	private static class WordAndSimilarity {
		private String word;
		private int similarity;
		public WordAndSimilarity(String word, int similarity) {
			super();
			this.word = word;
			this.similarity = similarity;
		}
		public String getWord() {
			return this.word;
		}
		public int getSimilarity() {
			return this.similarity;
		}
	}
	
	private Map<String, Set<WordAndSimilarity>> aggregated;
	
	public AggregateActor() {
		this.aggregated = new TreeMap<>();
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Similarity.class, similarity -> aggregate(similarity))
				.match(Done.class, done -> {
					getSender().tell(getResult(done), getSelf());
				})
				.build();
	}
	
	private Result getResult(Done done) {
		if(!aggregated.containsKey(done.getWord()))
			return new Result(done.getWord(), null);
		
		// Create result
		List<String> list = new ArrayList<>();
		int counter = 0;
		for(WordAndSimilarity ws : aggregated.get(done.getWord())) {
			if(counter++ == done.getNumberOfResults())
				break;
			list.add(ws.getWord());
		}
		Result res = new Result(done.getWord(), list);
		
		// Delete data
		aggregated.remove(done.getWord());
		
		// Return result
		return res;
	}
	
	private void aggregate(Similarity similarity) {
		// If not exists yet, create a new set
		if(!aggregated.containsKey(similarity.getInput()))
			aggregated.put(similarity.getInput(), new TreeSet<>( (a, b) -> {
						if(a.getSimilarity() != b.getSimilarity())
							return -(a.getSimilarity() - b.getSimilarity());
						return a.getWord().compareTo(b.getWord());
					}
			));
		
		// Add dictionary word
		Set<WordAndSimilarity> set = aggregated.get(similarity.getInput());
		set.add(new WordAndSimilarity(similarity.getDic(), similarity.getDist()));
	}
	
	public static Props props() {
		return Props.create(AggregateActor.class);
	}
}
