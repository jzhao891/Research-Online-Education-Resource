
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.servlet.*;
/**
 * Servlet implementation class submit
 */
@WebServlet("/submit")
public class submit extends HttpServlet {
	
	final static Logger logger = Logger.getLogger(submit.class);
	private static final long serialVersionUID = 1L;
       
	ServletConfig config ;
	public void init(ServletConfig config)throws ServletException {

	    this.config = config;

	    super.init(config);

	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public submit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
      
        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
        String line=null;
        String userid="";
        String query="";
        String userip="";
        String index="";
		String[] items={};//parameter lists
		try{
			while((line=in.readLine())!=null){
	        	
	        	line=java.net.URLDecoder.decode(line, "UTF-8");
	        	System.out.println(line);
	        	items=line.split("&");
	        	query=items[2].substring(6);
	        	System.out.println(items[1]);
	        	userid=items[1].substring(7);//items[1]->userid
	        	userip=items[0].substring(7);//items[0]->userip
	        	index=items[3].substring(12);//items[3]->subsetIndex
	        	//store user's ip and submitted subset index
	        	//String addFilepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\ipaddress-"+query+".txt";
	    		//String addFilepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/ipaddress-"+query+".txt";
	            //String addFilepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\ipaddress.txt";
	          	//String addFilepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/ipaddress.txt";
	          	/*FileWriter fw=new FileWriter(new File(addFilepath),true);
	          	fw.append(items[0].substring(7)+"\n");//items[0]->userip
	          	fw.close();*/
	        	
	        }
			ServletContext sc=config.getServletContext();
			String filepath=sc.getRealPath("/")+"data/submit_"+userid+".json";
			System.out.println(filepath);
			String filefolder=sc.getRealPath("/")+"data";
			File datafolder=new File(filefolder);
			if(!datafolder.exists()){
				logger.warn("data folder doesn't exist!");
				datafolder.mkdir();
				logger.warn("folder is built! the path is:"+filefolder);
			}
			FileWriter fw=new FileWriter(new File(filepath));
			fw.append("{\n\"userid\":"+userid+",\n\"list\":[\n");
			int cont=0;
			for(int i=4;i<items.length-4;){//item[4,length-1]->lists
				cont++;
				if(i==items.length-5){
					fw.append("{\n\"id\":"+getcontent(items[i])+",\"link\":\""+getcontent(items[i+1])+"\",\"revised\":"+getcontent(items[i+2])+"\",\"IfContainSyllabus\":"+getcontent(items[i+3])+",\"xpath\":\""+getcontent(items[i+4])+"\"\n}\n],\n");
				}
				else{
					fw.append("{\n\"id\":"+getcontent(items[i])+",\"link\":\""+getcontent(items[i+1])+"\",\"revised\":"+getcontent(items[i+2])+"\",\"IfContainSyllabus\":"+getcontent(items[i+3])+",\"xpath\":\""+getcontent(items[i+4])+"\"\n},\n");
				}
				
				i+=5;
			}
			fw.append("\"link_count\":"+cont+"\n}");
			fw.close();
        	SampleSelector.modify(query, index, userip);
			if(logger.isInfoEnabled()){
				logger.info("marking results is stored in "+filepath);
			}
			Gson gson = new Gson();
	        JsonElement je=gson.toJsonTree(1);
			JsonObject myObj = new JsonObject();
			myObj.add("reply", je);
			out.println(myObj.toString());
			out.close();
			
		}catch(IOException e){
			logger.error(e);
			e.printStackTrace();
			Gson gson = new Gson();
	        JsonElement je=gson.toJsonTree(-1);
			JsonObject myObj = new JsonObject();
			myObj.add("reply", je);
			out.println(myObj.toString());
			out.close();
		}
		
		
	}
	public String getcontent(String item){
		String[] list=item.split("=");
		return list.length==1 ? "" : list[1] ;
		
	}

}
