package model.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.messages.Result;
import model.messages.Request;
import model.messages.Calculate;
import model.messages.Similarity;

public class AggregateActor extends AbstractActor {
	private static final int N_RESULTS = 15;
	private static class Node {
		public int calculated = 0;
		public List<Data> similar;
		public List<ActorRef> requestedBy;
		
		public Node() {
			this.calculated = 0;
			this.similar = new ArrayList<>();
			this.requestedBy = new ArrayList<>();
		}
		
		public List<String> getSimilarWords() {
			List<String> list = new ArrayList<>();
			for(Data d : similar)
				list.add(d.dicWord);
			return list;
		}
		
		public void addData(Data data, int max_similar) {
			// If similar is full
			if(similar.size() == max_similar) {
				// Search position
				int pos = 0;
				for(Data d : similar) {
					// If data is less than d
					if(data.compare(d) < 0)
						break;
					pos++;
				}
				// If data is one of the most similar words 
				if(pos < similar.size()) {
					// Insert data at count position
					similar.add(pos, data);
					// Remove last
					similar.remove(similar.size()-1);
				}
			}
			// If similar isn't full
			else
				similar.add(data);
		}
	}
	
	private static class Data {
		public String dicWord;
		public int similarity;
		public Data(String dicWord, int similarity) {
			this.dicWord = dicWord;
			this.similarity = similarity;
		}
		
		public int compare(Data data) {
			if(this.similarity != data.similarity)
				return this.similarity-data.similarity;
			return this.dicWord.compareTo(data.dicWord);
		}
	}
	
	private Integer dicSize;
	private Map<String, Node> data;
	
	public AggregateActor() {
		this.data = new TreeMap<>();
	}
	
	public static Props props() {
		return Props.create(AggregateActor.class);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Integer.class, integer -> this.dicSize = integer)
				.match(Request.class, this::response)
				.match(Similarity.class, this::aggregate)
				.build();
	}
	
	private void response(Request request) {
		Node node;
		
		// If the word haven't requested yet
		if((node = data.get(request.getInputWord())) == null) {
			// Create a node
			node = new Node();
			node.requestedBy.add(request.getSender());
			
			// Insert into map
			data.put(request.getInputWord(), node);
			
			// Send response to calculate
			getSender().tell(new Calculate(request.getInputWord()), getSelf());
		}
		// If the word have requested
		else {			
			// If the word is totally calculated
			if(node.calculated == dicSize)
				// Send the result
				getSender().tell(new Result(request.getInputWord(), node.getSimilarWords(), request.getSender()), getSelf());
			// If the word isn't calculated yet
			else
				// Save the request
				node.requestedBy.add(request.getSender());
		}
	}
	
	private void aggregate(Similarity similarity) {
		Node node;
		if((node = data.get(similarity.getInputWord())) != null) {
			node.calculated++;
			node.addData(new Data(similarity.getDicWord(), similarity.getSimilarity()), N_RESULTS);
			
			// If the calculation is finished
			if(node.calculated == dicSize) {
				// Respond to all senders who requested
				for(ActorRef sender : node.requestedBy)
					getSender().tell(new Result(similarity.getInputWord(), node.getSimilarWords(), sender), getSelf());
				// Discard senders list
				node.requestedBy = null;
			}
		}
		else
			System.err.println("Unexpected Error!");
			/* Exception (Unexpected) */;
	}
}
