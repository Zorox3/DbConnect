package data.base;

import java.util.LinkedHashMap;
import java.util.Map;

public class DbValue {

	private Map<String, String> data = new LinkedHashMap<>();
	
	public void add(String j, String i){
		data.put(j	, i);
	}
	
	public String get(String i){
		return data.get(i);
	}
	
	public Map<String, String> getAll() {
		return data;
	} 
	
	public String toString(){
		return data.toString();
	}
}
