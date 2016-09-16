import java.util.ArrayList;
import java.util.List;

public class LinkSubset implements Comparable<LinkSubset>{
	public int index;
	public int cont;
	public List<String> iplist;
	public LinkSubset(int index){
		this.index=index;
		cont=0;
		iplist=new ArrayList<String>();
	}
	public LinkSubset(int index,int cont,List<String> l){
		this.index=index;
		this.cont=cont;
		iplist=l;
	}
	public int compareTo(LinkSubset o) {
		// TODO Auto-generated method stub
		if(this.cont==o.cont){
			return 0;
		}
		if(this.cont>o.cont){
			return 1;
		}
		return -1;
	}
}
