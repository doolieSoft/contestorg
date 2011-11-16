package events;

public class EventRemove extends Event
{

	// Attributs
	private Object object;
	private Object associate;
	private int index;
	private Integer role;

	// Constructeur
	public EventRemove(Object object, Object associate, int index) {
		this(object,associate,index,null);
	}
	public EventRemove(Object object, Object associate, int index, Integer role) {
		this.object = object;
		this.associate = associate;
		this.index = index;
		this.role = role;
	}

	// Getters
	public Object getObject () {
		return this.object;
	}
	public Object getAssociate () {
		return this.associate;
	}
	public int getIndex() {
		return this.index;
	}
	public Integer getRole () {
		return this.role;
	}
}
