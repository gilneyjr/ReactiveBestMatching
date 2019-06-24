package actors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.Dictionary;
import messages.LoadDictionary;

public class DatabaseActor extends AbstractActor {
	private Cluster cluster;
	private Session session;
	
	public DatabaseActor() {
		cluster = Cluster.builder()
				.addContactPoint("127.0.0.1")
				.withCredentials("cassandra", "cassandra")
				.build();
		try {
			session = cluster.connect("bestmatching");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Props props() {
		return Props.create(DatabaseActor.class);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(LoadDictionary.class, msg -> getSender().tell(loadDictionary(), getSelf()))
				.build();
	}

	private Dictionary loadDictionary() {
		ResultSet result = session.execute("SELECT * FROM dictionary;");
		
		List<String> list = new ArrayList<>();
		for(Iterator<Row> it = result.iterator(); it.hasNext(); /* Empty */) {
			Row row = it.next();
			list.add(row.getString("word"));
		}
		
		return new Dictionary(list);
	}
}
