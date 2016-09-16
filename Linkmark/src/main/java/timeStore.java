
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
@WebServlet("/timeStore")
public class timeStore extends HttpServlet {
	
	final static Logger logger = Logger.getLogger(timeStore.class);
	private static final long serialVersionUID = 1L;
       
	ServletConfig config ;
	public void init(ServletConfig config)throws ServletException {

	    this.config = config;

	    super.init(config);

	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public timeStore() {
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
		String[] items={};//parameter lists
		try{
			while((line=in.readLine())!=null){
	        	
	        	line=java.net.URLDecoder.decode(line, "UTF-8");
	        	System.out.println(line);
	        	items=line.split("&");
	        	userid=items[0].split("=")[1];//items[1]->userid
	        	//add user's ip
	        	String addFilepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\clickthrough-"+userid+".txt";
	    		//String addFilepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/clickthrough-"+userid+".txt";
	            FileWriter fw=new FileWriter(new File(addFilepath),true);
	          	for(int i=1;i<items.length;i++){
	          		fw.append(items[i]+"\n");//click through
	          	}
	          	
	          	fw.close();
	        	
	        }
			if(logger.isInfoEnabled()){
				logger.info("click log is stored!");
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

}
