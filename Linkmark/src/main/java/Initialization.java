import java.io.IOException;
import java.util.TimerTask;

import javax.servlet.ServletContext;

public class Initialization extends TimerTask{
	private ServletContext context=null;
	public Initialization(ServletContext context){
		this.context=context;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			SampleSelector.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			context.log(e.getMessage());
		}
		System.out.println("sampleselector is initialed");
	     
	}
}
