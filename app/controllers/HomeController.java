package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;

import javax.inject.Inject;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import model.actors.MasterSupervisor;
import model.form.FormData;
import model.messages.Input;
import model.messages.LoadDictionary;
import model.messages.Output;

import play.mvc.*;
import play.data.FormFactory;
import play.data.Form;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	private Form<FormData> form;
	private ActorRef master;

	@Inject
	public HomeController(FormFactory formFactory, ActorSystem actorSystem) {
		form = formFactory.form(FormData.class);
		master = actorSystem.actorOf(MasterSupervisor.props());

		Future<Object> future = Patterns.ask(master, new LoadDictionary(null), 60000);
		try {
			Await.result(future, Duration.create(120, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * An action that renders an HTML page with a welcome message.
	 * The configuration in the <code>routes</code> file means that
	 * this method will be called when the application receives a
	 * <code>GET</code> request with a path of <code>/</code>.
	 */
	public Result index() {
		return ok(views.html.index.render(form));
	}

	public Result getResult(Http.Request request) {
		String inputWord = form.bindFromRequest(request).get().getData();

		Output out = null;
		long start = System.currentTimeMillis();
		Future<Object> future = Patterns.ask(master, new Input(inputWord), 120000);
		try {
			out = (Output) Await.result(future, Duration.create(120, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();

		return ok(views.html.results.render(
			out.getOutput().size() + " Most Similar Words to \""+inputWord+"\"", 
			out.getOutput(), 
			"Duration: "
				+ ((end-start)/60000) + "min "
				+ (((end-start)%60000)/1000) + "."
				+ (((end-start)%60000)%1000) + "s"));
	}

}
