package comparators;

import java.util.Comparator;

import models.ModelEquipe;

/**
 * Comparateur pour trier une liste d'équipes alphabétiquement
 */
public class CompEquipeNom implements Comparator<ModelEquipe>
{

	// Implémentation de compare
	public int compare (ModelEquipe equipeA, ModelEquipe equipeB) {
		return equipeA.getNom().compareTo(equipeB.getNom());
	}

}
