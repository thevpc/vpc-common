package net.thevpc.common.log;

import java.util.ArrayList;
import java.util.HashMap;


public class LogEventSupport {
	private HashMap<String, ArrayList<LogListener>> mapper;
	public LogEventSupport(){
		
	}
	public void addLogListener(String property, LogListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		ArrayList<LogListener> list;
		if (mapper == null) {
			mapper = new HashMap<String, ArrayList<LogListener>>();
			list = new ArrayList<LogListener>();
			mapper.put(property, list);
		} else {
			list = mapper.get(property);
			if (list == null) {
				list = new ArrayList<LogListener>();
				mapper.put(property, list);
			}
		}
		list.add(listener);
	}
	
	public void removeLogListener(String property, LogListener listener) {
		if (mapper != null) {
			ArrayList<LogListener> list = mapper.get(property);
			if(list!=null){
				list.remove(listener);
			}
		}
	}
	
	public void fireEvent(Log log,String property){
		fireEvent(log,property,false,true);
	}
	
	public void fireEvent(Log log,String property,Object oldValue,Object newValue){
		if(oldValue!=newValue && (oldValue==null || !oldValue.equals(newValue))){
			fireEvent(new LogEvent(log,property,oldValue,newValue));
		}
	}
	
	public void fireEvent(LogEvent event){
		if (mapper != null) {
			String p=event.getProperty();
			ArrayList<LogListener> list = mapper.get(p);
			if(list!=null){
				for (LogListener listener : list) {
					listener.logEvent(event);
				}
			}
			if(p!=null){
				list = mapper.get(null);
				if(list!=null){
					for (LogListener listener : list) {
						listener.logEvent(event);
					}
				}
			}
		}
	}
}
