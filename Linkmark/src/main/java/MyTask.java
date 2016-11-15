import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import javax.servlet.ServletContext;


public class MyTask extends TimerTask{
	private static final List<Integer> C_SCHEDULE_HOUR=new ArrayList<Integer>(Arrays.asList(0,6,7,8,9,10,11,13,15,16,17,18,19,20,21,22,23));
	private static boolean isRunning=false;
	private ServletContext context=null;
	public MyTask(ServletContext context){
		this.context=context;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Calendar c=Calendar.getInstance();
		if(!isRunning){
			if(C_SCHEDULE_HOUR.contains(c.get(Calendar.HOUR_OF_DAY))){
				isRunning=true;
				context.log("task begin!");
				String filepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\SampleSelector.csv";
				//String filepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/SampleSelector.csv";
				File f=new File(filepath);
					try {
						FileWriter fw=new FileWriter(f);
						Iterator<LinkSubset> it=SampleSelector.pq_IR.iterator();
						
							while(it.hasNext()){
								LinkSubset ls=it.next();
								StringBuilder sb=new StringBuilder();
								sb.append("IR,"+String.valueOf(ls.index)+","+String.valueOf(ls.cont));
								for(String ip:ls.iplist){
									sb.append(","+ip);
								}
								fw.append(sb+"\n");
							}
						
						it=SampleSelector.pq_HCI.iterator();
						
							while(it.hasNext()){
								LinkSubset ls=it.next();
								StringBuilder sb=new StringBuilder();
								sb.append("HCI,"+String.valueOf(ls.index)+","+String.valueOf(ls.cont));
								for(String ip:ls.iplist){
									sb.append(","+ip);
								}
								fw.append(sb+"\n");
							}
						
						it=SampleSelector.pq_java.iterator();
						
							while(it.hasNext()){
								LinkSubset ls=it.next();
								StringBuilder sb=new StringBuilder();
								sb.append("Java,"+String.valueOf(ls.index)+","+String.valueOf(ls.cont));
								for(String ip:ls.iplist){
									sb.append(","+ip);
								}
								fw.append(sb+"\n");
							}
						
						it=SampleSelector.pq_DB.iterator();
						
							while(it.hasNext()){
								LinkSubset ls=it.next();
								StringBuilder sb=new StringBuilder();
								sb.append("DB,"+String.valueOf(ls.index)+","+String.valueOf(ls.cont));
								for(String ip:ls.iplist){
									sb.append(","+ip);
								}
								fw.append(sb+"\n");
							}
						
						
						isRunning=false;
						context.log("task has done!");
						fw.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
					
				//}
			}
		}
	}

}
