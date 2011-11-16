package events;

public class EventClear extends Event
{

	// Attributs
	private Object object;
	private Class<?> associateClass;
	private Integer role;

	// Constructeur
	public EventClear(Object object, Class<?> associate) {
		this(object, associate, null);
	}
	public EventClear(Object object, Class<?> associate, Integer role) {
		this.object = object;
		this.associateClass = associate;
		this.role = role;
	}

	// Getters
	public Object getObject () {
		return this.object;
	}
	public Class<?> getAssociateClass () {
		return this.associateClass;
	}
	public Integer getRole () {
		return this.role;
	}
}
