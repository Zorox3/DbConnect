package data.base;

import java.util.LinkedHashMap;
import java.util.Map;

public class DbData {

    private Map<Object, DbValue> data = new LinkedHashMap<>();

    
    public void add(Object i , DbValue j){
    	data.put(i, j);
    }
    
    public Map<Object, DbValue> getAll() {
		return data;
	}
    
    public boolean isEmtpy(){
    	return data.isEmpty();
    }
    
    public void clear(){
    	data.clear();
    }
    
    
	public String toString(){
		return data.toString();
	}
}
