import java.util.concurrent.TimeUnit;

import actors.MasterSupervisor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import messages.Input;
import messages.LoadDictionary;
import messages.Output;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class Main {
	public static void main(String[] args) {
		final ActorSystem actorSystem = ActorSystem.create("bestmatching");
		final ActorRef master = actorSystem.actorOf(MasterSupervisor.props());
		
		System.out.print("Loading dictionary... ");
		master.tell(new LoadDictionary(), ActorRef.noSender());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("Done!");
		
		Output out = null;
		System.out.println("Running...");
		long start = System.currentTimeMillis();
		Future<Object> future = Patterns.ask(master, new Input("casa"), 60000);
		try {
			out = (Output) Await.result(future, Duration.create(120, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Duration: "
				+ ((end-start)/60000) + "min "
				+ (((end-start)%60000)/1000) + "."
				+ (((end-start)%60000)%1000) + "s");
		if(out != null) {
			System.out.print("{ ");
			for(String str : out.getOutput())
				System.out.print(str + " ");
			System.out.println("}");
		}
		
		actorSystem.terminate();
	}
}
