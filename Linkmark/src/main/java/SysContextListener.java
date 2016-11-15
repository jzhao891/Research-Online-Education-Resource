import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SysContextListener implements ServletContextListener{
	private Timer timer=null;
	private Timer timerInit=null;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		timer.cancel();
		timerInit.cancel();
		arg0.getServletContext().log("timer is destryed!");
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		timer=new Timer(true);
		timerInit=new Timer(true);
		arg0.getServletContext().log("timerInit begins!");
		timerInit.schedule(new Initialization(arg0.getServletContext()), 0);
		arg0.getServletContext().log("timer begins!");
		timer.schedule(new MyTask(arg0.getServletContext()), 5*1000, 5*60*1000);
		arg0.getServletContext().log("task is added!");
	}

}
