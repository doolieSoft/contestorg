package models;

public abstract class ModelMatchable extends ModelAbstract
{
	
	// Implémentation de match
	public int match (String keywords) {
		// Récupérer la liste de strings de l'objet
		String[] strings = this.toStrings();
		
		// Pour tous les keywords
		for (String keyword : keywords.split(" ")) {
			// Initialiser le niveau de correspondance
			int niveau = strings.length;
			
			// Pour toutes les strings de l'objet
			for (String string : strings) {
				// Tester si keyword est contenu dans le string
				if (string.contains(keyword)) {
					return niveau;
				}
				
				// Décrémenter le niveau
				niveau--;
			}
		}
		
		// Retourner 0 si pas de correspondance trouvée
		return 0;
	}
	
	/**
	 * Méthode que doivent implémenter les filles de cette classe
	 * @return une liste de strings qui seront utilisées dans la recherche en prenant en compte l'ordre
	 */
	protected abstract String[] toStrings ();
}
