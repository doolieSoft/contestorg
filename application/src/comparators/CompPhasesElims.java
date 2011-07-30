package comparators;

import java.util.Comparator;

import models.ModelEquipe;

/**
 * Comparateur retournant la meilleure équipe en fonction de leur rang aux phases éliminatoires
 */
public class CompPhasesElims implements Comparator<ModelEquipe>
{

	// Implémentation de compare
	public int compare (ModelEquipe equipeA, ModelEquipe equipeB) {
		int rangA = equipeA.getRangPhasesElims();
		int rangB = equipeB.getRangPhasesElims();
		if (rangA < 1 || rangB < 1 || rangA == rangB) {
			return 0;
		}
		return rangA > rangB ? 1 : -1;
	}

}
