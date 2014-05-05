package lib;

import play.libs.Akka;
import scala.concurrent.ExecutionContext;

public class DBExecutionContext {
	//dispatcher for executing asynchronous db calls
	public static ExecutionContext ctx = Akka.system().dispatchers()
			.lookup("akka.db-dispatcher");
}
