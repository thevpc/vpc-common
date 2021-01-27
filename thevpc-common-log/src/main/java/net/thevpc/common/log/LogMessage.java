package net.thevpc.common.log;

class LogMessage {
	Log logger;
	Message msg;
	public LogMessage(Log logger, Message msg) {
		super();
		this.logger = logger;
		this.msg = msg;
	}
	
}
