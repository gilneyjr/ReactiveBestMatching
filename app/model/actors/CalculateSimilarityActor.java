package model.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import model.messages.Similarity;
import model.messages.Pair;

public class CalculateSimilarityActor extends AbstractActor {
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Pair.class, pair -> {
					getSender().tell(calculateSimilarity(pair.getDicWord(), pair.getInputWord()), getSelf());
				})
				.build();
	}
	
	private Similarity calculateSimilarity(String dicWord, String inputWord) {
		return new Similarity(
				inputWord, 
				dicWord,
				similarityDistance(dicWord, inputWord)
		);
	}
	
	private int levenshtein(int[][] memo, String str1, String str2, int i, int j) {
		if(Math.min(i,j) == 0)
			return Math.max(i, j);
		
		if(memo[i-1][j-1] != -1)
			return memo[i-1][j-1];
		
		memo[i-1][j-1] = Math.min(
				Math.min(
						levenshtein(memo, str1, str2, i-1, j)+1,
						levenshtein(memo, str1, str2, i, j-1)+1),
				levenshtein(memo, str1, str2, i-1, j-1) + (str1.charAt(i-1) != str2.charAt(j-1) ? 1 : 0));
		return memo[i-1][j-1];
	}
	
	private int similarityDistance(String str1, String str2) {
		int [][] memo = new int[str1.length()][str2.length()];
		for (int i = 0; i < str1.length(); i++)
			for (int j = 0; j < str2.length(); j++)
				memo[i][j] = -1;
		return levenshtein(memo, str1, str2, str1.length(), str2.length());
	}
	
	public static Props props() {
		return Props.create(CalculateSimilarityActor.class);
	}
}
