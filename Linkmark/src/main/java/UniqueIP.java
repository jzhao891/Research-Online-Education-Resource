

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class UniqueIP
 */
public class UniqueIP extends HttpServlet {
	
	final static Logger logger = Logger.getLogger(LoadLinkList.class);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UniqueIP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
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
        
		String ip=request.getParameter("ip");
		String query=request.getParameter("query");
		boolean ifUnique=true;
		
		String filepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\ipaddress-"+query+".txt";
		//String filepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/ipaddress-"+query+".txt";
		File ipAdd=new File(filepath);
		if(ipAdd.exists()){
			FileInputStream fis = new FileInputStream(ipAdd);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line=null;
			while((line=br.readLine())!=null){
				String ipInrecord=line.split(":")[0];
				if(ipInrecord.equals(ip)){
					logger.info("This ipaddress: "+ip+" has been recorded in file!");
					ifUnique=false;
					break;
				}
			}
			br.close();
		}
		if(ifUnique){
			logger.info("This ipaddress: "+ip+" hasn't submit any HIT!");
		}
		JsonObject myObj=new JsonObject();
		myObj.addProperty("value", ifUnique);
		out.println(myObj.toString());
		out.close();
	}

}
