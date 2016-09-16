



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Servlet implementation class LoadLinkList
 */
@WebServlet("/LoadLinkList")
public class LoadLinkList extends HttpServlet {
	
	final static Logger logger = Logger.getLogger(LoadLinkList.class);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadLinkList() {
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
        //get search result according to the "query" parameter
		String query=request.getParameter("query");
		String ip=request.getParameter("ip");
		JsonParser jParser = new JsonParser();
        //filepath should be edited based on the absolute location of json file
		String filepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\"+query+".json";
		//String filepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/"+query+".json";
		File f=new File(filepath);
		if(!f.exists()){
			//log a message for the no existence of the file
			logger.warn("Search result can't find!");
			//transit error message to front-end
			JsonObject myObj=new JsonObject();
			myObj.addProperty("error", "-1");
			out.println(myObj.toString());
			out.close();
		}
		else{
			//Gson gson = new Gson();
			if(SampleSelector.isEmpty()){
				SampleSelector.init();
				logger.info("Sampleselector is empty. Initializing SampleSelector is done!");
			}
			try{
				JsonObject myObj = (JsonObject) jParser
		                .parse(new FileReader(filepath));
				JsonArray links=myObj.getAsJsonArray("links");
				List<String> list=new ArrayList<String>();
				Iterator i=links.iterator();
				while(i.hasNext()){
					String s=i.next().toString();
					System.out.println(s);
					list.add(s);
				}
				LinkSubset ls=SampleSelector.pickSet(query,ip);
				if(ls==null){
					JsonObject obj=new JsonObject();
					obj.addProperty("error", "-2");
					out.println(obj.toString());
					out.close();
				}
				else{
					int index=ls.index;
					List<String> result=new ArrayList<String>();
					for(int j=0;j<5;j++){
						result.add(list.get(index*5+j));
					}
					JsonObject obj=new JsonObject();
					JsonElement je=new JsonParser().parse(result.toString());
					obj.addProperty("index", index);
					obj.add("links", je);
					//System.out.println("test3"+result.toString());
					//System.out.println("test4"+obj.toString());
					out.println(obj.toString());
					out.close();
				}
				
				
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}

}
