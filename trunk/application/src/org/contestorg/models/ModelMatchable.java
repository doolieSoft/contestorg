package org.contestorg.models;

/**
 * Modèle recherchable
 */
public abstract class ModelMatchable extends ModelAbstract
{
	
	/**
	 * Récupérer le score de correspondance à des mots clés
	 * @param keywords mots clés
	 * @return score de correspondance à des mots clés
	 */
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
	 * Récupérer la liste de strings utilisées par la correspondance
	 * @return liste de strings utilisées par la correspondance
	 */
	protected abstract String[] toStrings ();
}
