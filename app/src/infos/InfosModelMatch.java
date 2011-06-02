package infos;

import java.util.Date;

public abstract class InfosModelMatch extends InfosModelAbstract
{
	// Attributs
	private Date date;
	private String details;
	
	// Constructeur
	public InfosModelMatch(Date date,String details) {
		this.details = details;
	}
	
	// Getters
	public Date getDate() {
		return this.date;
	}
	public String getDetails() {
		return this.details;
	}
}
