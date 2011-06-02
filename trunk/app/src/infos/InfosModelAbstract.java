package infos;

public abstract class InfosModelAbstract
{
	// Id du modèle
	private int id;	
	
	// Récupérer l'id du modèle
	public int getId() {
		return this.id;
	}
	
	// Définir l'id du modèle
	public void setId(int id) {
		this.id = id;
	}
}
