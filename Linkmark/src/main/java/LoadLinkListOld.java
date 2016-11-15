



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Servlet implementation class LoadLinkList
 */
@WebServlet("/LoadLinkListNew")
public class LoadLinkListOld extends HttpServlet {
	
	final static Logger logger = Logger.getLogger(LoadLinkListOld.class);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadLinkListOld() {
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
		String query=request.getParameter("query").toLowerCase();
		JsonParser jParser = new JsonParser();
        //filepath should be edited based on the absolute location of json file
		//String filepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\"+query+".json";
		String filepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/"+query+".json";
		File f=new File(filepath);
		if(!f.exists()){
			//log a message for the no existence of the file
			logger.warn("Search result can't find!");
			//transit error message to front-end
			JsonObject myObj=new JsonObject();
			myObj.addProperty("error", true);
			out.println(myObj.toString());
			out.close();
		}
		else{
			//Gson gson = new Gson();
			try{
				JsonObject myObj = (JsonObject) jParser
		                .parse(new FileReader(filepath));
				out.println(myObj.toString());
				out.close();
				
			}catch(Exception e){
				logger.error(e);
			}
			
		}
        
		
		/*Configuration conf=new Configuration();
		conf.setDirectoryForTemplateLoading(new File(Path.templatedir));
		FileWriter writer=new FileWriter(Path.htmldir+"Index.html");
		Map<String,String[]> maps=new HashMap<String,String[]>();
		//SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        String dayOfWeek = (new Date()).toString();
        String[] date=dayOfWeek.split(" ");
        String[] time=date[3].split(":");
        if(Integer.valueOf(time[0])>=6&&Integer.valueOf(time[0])<=12){
        	date[3]="morning";
        }
        else if(Integer.valueOf(time[0])>12&&Integer.valueOf(time[0])<=19){
        	date[3]="afternoon";
        }
        else{
        	date[3]="night";
        }
		maps.put("date", date);
        Template temp=conf.getTemplate("form.ftl");
        try{
        	temp.process(maps, writer);
        }catch(TemplateException e){
        	halt(500);
        }*/
	}

}
