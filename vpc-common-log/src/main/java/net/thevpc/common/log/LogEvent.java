package net.thevpc.common.log;


public class LogEvent {
    public static final String MESSAGE_TYPE_ADDED = "Log.PROPERTY_MESSAGE_TYPE_ADDED";
    public static final String MESSAGE_TYPE_REMOVED = "Log.PROPERTY_MESSAGE_TYPE_REMOVED";

    public static final String DEFAULT_LOGGER_SET = "Log.defaultLog";
    public static final String LOG_REGISTRED = "Log.LogRegistred";
    public static final String LOG_UNREGISTRED = "Log.LogUnregistred";

    public static final String PROPERTY_PREFIX = "LogProperty.";

    private Log log;
	private String property;
	private Object oldValue;
	private Object newValue;
	
	/**
	 * @param log
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	public LogEvent(Log log, String property, Object oldValue, Object newValue) {
		super();
		this.log = log;
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}
	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}
	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((log == null) ? 0 : log.hashCode());
		result = PRIME * result + ((newValue == null) ? 0 : newValue.hashCode());
		result = PRIME * result + ((oldValue == null) ? 0 : oldValue.hashCode());
		result = PRIME * result + ((property == null) ? 0 : property.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LogEvent other = (LogEvent) obj;
		if (log == null) {
			if (other.log != null)
				return false;
		} else if (!log.equals(other.log))
			return false;
		if (newValue == null) {
			if (other.newValue != null)
				return false;
		} else if (!newValue.equals(other.newValue))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}
	
	
}
