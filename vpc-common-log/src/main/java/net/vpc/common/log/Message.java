package net.vpc.common.log;

import java.util.Date;

class Message {
    String type ;
    String message ;
    int logLevel ;
    Date date ;
    Long delta ;
    Thread thread ;
    StackTrace stack ;
    String user_id ;
    String user_name ;
	public Message(String type, String message, int logLevel, Date date, Long delta, Thread thread, StackTrace stack, String user_id, String user_name) {
		super();
		this.type = type;
		this.message = message;
		this.logLevel = logLevel;
		this.date = date;
		this.delta = delta;
		this.thread = thread;
		this.stack = stack;
		this.user_id = user_id;
		this.user_name = user_name;
	}
    
}
