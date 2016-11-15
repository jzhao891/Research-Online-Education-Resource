import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class SampleSelector {
	public static volatile PriorityQueue<LinkSubset> pq_IR=new PriorityQueue<LinkSubset>();//index,answercount,iplist,it should be sorted by "answercont"
	public static volatile PriorityQueue<LinkSubset> pq_HCI=new PriorityQueue<LinkSubset>();
	public static volatile PriorityQueue<LinkSubset> pq_DB=new PriorityQueue<LinkSubset>();
	public static volatile PriorityQueue<LinkSubset> pq_java=new PriorityQueue<LinkSubset>();
	
	public static int irCont=150;
	public static int hciCont=290;
	public static int javaCont=200;
	public static int dbCont=150;
	public static int num=5;
	
	public static void init() throws IOException{
		String filepath="C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\linkmark-data\\SampleSelector.csv";
		//String filepath="/Users/jessicazhao/Documents/workspace/workspace-python/data/SampleSelector.csv";
		
		File f=new File(filepath);
		if(f.exists()){
			BufferedReader br=new BufferedReader(new FileReader(f));
			String line;
			int cont=-1;
			while((line=br.readLine())!=null){
				String[] items=line.split(",");
				String subject=items[0];
				int index=Integer.valueOf(items[1]);
				cont=Integer.valueOf(items[2]);
				List<String> list=new ArrayList<String>();
				for(int i=3;i<items.length;i++){
					if(items[i]!=""||items[i]==null){
						list.add(items[i]);
					}
				}
				System.out.println("test2");
				LinkSubset ls=new LinkSubset(index,cont,list);
				if(subject.equals("IR")){
					pq_IR.add(ls);
					System.out.println("IR ok");
				}
				else if(subject.equals("HCI")){
					pq_HCI.add(ls);
					System.out.println("HCI ok");
				}
				else if(subject.equals("Java")){
					pq_java.add(ls);
					System.out.println("java ok");
				}
				else{
					pq_DB.add(ls);
					System.out.println("DB ok");
				}
			}
			br.close();
			if(cont==-1){
				initalWithoutFile();
			}
		}
		else{
			initalWithoutFile();
		}
			
	}
	public static void initalWithoutFile(){
		for(int i=0;i<irCont/num;i++){
			int index=i;
			LinkSubset ls=new LinkSubset(index);
			pq_IR.add(ls);
		}
		for(int i=0;i<hciCont/num;i++){
			int index=i;
			LinkSubset ls=new LinkSubset(index);
			pq_HCI.add(ls);
		}
		for(int i=0;i<javaCont/num;i++){
			int index=i;
			LinkSubset ls=new LinkSubset(index);
			pq_java.add(ls);
		}
		for(int i=0;i<dbCont/num;i++){
			int index=i;
			LinkSubset ls=new LinkSubset(index);
			pq_DB.add(ls);
		}
	}
	public static boolean isEmpty(){
			if(pq_IR.size()<=0&&pq_HCI.size()<=0&&pq_java.size()<=0&&pq_DB.size()<=0){
				return true;
			}
		
		return false;
	}
	public static LinkSubset pickSet(String query,String ip){
		LinkSubset ls=null;
		if(query.equals("Information_Retrieval_course")){
			return find(pq_IR,ls,ip);
			/*while(!pq_IR.isEmpty()){
				ls=pq_IR.poll();
				List<String> iplist=ls.iplist;
				for(String s:iplist){
					if(s.equals(ip)){
						temp.add(ls);
						ls=null;
						break;
					}
				}
				if(ls!=null){
					while(!temp.isEmpty()){
						pq_IR.add(temp.poll());
					}
					return ls;
				}
			}
			return null;*/
		}
		else if(query.equals("Human_Computer_Interaction_course")){
			return find(pq_HCI,ls,ip);
		}
		else if(query.equals("Java_Programming_course")){
			return find(pq_java,ls,ip);
		}
		else{
			return find(pq_DB,ls,ip);
		}
	}
	public static LinkSubset find(PriorityQueue<LinkSubset> pq,LinkSubset ls,String ip){

		PriorityQueue<LinkSubset> temp=new PriorityQueue<LinkSubset>();
		while(!pq.isEmpty()){
			ls=pq.poll();
			List<String> iplist=ls.iplist;
			for(String s:iplist){
				if(s.equals(ip)||s.equals("1.1.1.1")){
					temp.add(ls);
					ls=null;
					break;
				}
			}
			if(ls!=null){
				while(!temp.isEmpty()){
					pq.add(temp.poll());
				}
				pq.add(ls);
				return ls;
			}
		}
		return null;
	}
	public static LinkSubset get(String query,String index){
		if(query.equals("Information_Retrieval_course")){
			if(!pq_IR.isEmpty()){
				Iterator<LinkSubset> it=pq_IR.iterator();
				LinkSubset ls;
				while(it.hasNext()){
					if((ls=it.next()).index==Integer.valueOf(index)){
						pq_IR.remove(ls);
						return ls;
					}
				}
			}
			
		}
		else if(query.equals("Human_Computer_Interaction_course")){
			if(!pq_HCI.isEmpty()){
				Iterator<LinkSubset> it=pq_HCI.iterator();
				LinkSubset ls;
				while(it.hasNext()){
					if((ls=it.next()).index==Integer.valueOf(index)){
						pq_HCI.remove(ls);
						return ls;
					}
				}
			}
		}
		else if(query.equals("Java_Programming_course")){
			if(!pq_java.isEmpty()){
				Iterator<LinkSubset> it=pq_java.iterator();
				LinkSubset ls;
				while(it.hasNext()){
					if((ls=it.next()).index==Integer.valueOf(index)){
						pq_java.remove(ls);
						return ls;
					}
				}
			}
		}
		else{
			if(!pq_DB.isEmpty()){
				Iterator<LinkSubset> it=pq_DB.iterator();
				LinkSubset ls;
				while(it.hasNext()){
					if((ls=it.next()).index==Integer.valueOf(index)){
						pq_DB.remove(ls);
						return ls;
					}
				}
			}
		}
		return null;
	}
	public static boolean modify(String query,String index,String ip){
		LinkSubset ls=get(query,index);
		if(ls!=null){
			ls.cont++;
			ls.iplist.add(ip);
			if(query.equals("Information_Retrieval_course")){
				pq_IR.add(ls);
				return true;
				
			}
			else if(query.equals("Human_Computer_Interaction_course")){
				
					pq_HCI.add(ls);
					return true;
			}
			else if(query.equals("Java_Programming_course")){
				
					pq_java.add(ls);
					return true;
			}
			else{
				
					pq_DB.add(ls);
					return true;
			}
		}
		/*if(query.equals("information_retrieval_course")){
			
			if(ls!=null){
				
				pq_IR.add(ls);
				return true;
			}
			
		}
		else if(query.equals("human_computer_interaction_course")){
			LinkSubset ls=get(query,index);
			if(ls!=null){
				ls.cont++;
				ls.iplist.add(ip);
				pq_HCI.add(ls);
				return true;
			}
		}
		else if(query.equals("java_programming_course")){
			LinkSubset ls=get(query,index);
			if(ls!=null){
				ls.cont++;
				ls.iplist.add(ip);
				pq_java.add(ls);
				return true;
			}
		}
		else{
			LinkSubset ls=get(query,index);
			if(ls!=null){
				ls.cont++;
				ls.iplist.add(ip);
				pq_DB.add(ls);
				return true;
			}
		}*/
		return false;
	}
	
}
