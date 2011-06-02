package comparators;

import interfaces.IMatchable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CompMatchable implements Comparator<IMatchable>
{

	// Attributs
	private String keywords;

	// Constructeur
	public CompMatchable(String keywords) {
		this.keywords = keywords;
	}

	// Implémentation de compare
	public int compare (IMatchable matchableA, IMatchable matchableB) {
		return matchableA.match(this.keywords) - matchableB.match(this.keywords);
	}

	// Recherche dans les models matchable
	public static ArrayList<IMatchable> search (String keywords, ArrayList<IMatchable> matchables) {
		// Trier la liste des objets en fonction de leur concordance avec les mots clés
		Collections.sort(matchables, new CompMatchable(keywords));

		// Initialiser la liste résultat
		ArrayList<IMatchable> resultat = new ArrayList<IMatchable>();

		// Ajouter les objets correspondant aux mots clés dans la liste résultat
		for (IMatchable matchable : matchables) {
			// Vérifier si les mots clés correspondent
			if (matchable.match(keywords) == 0) {
				return resultat;
			} else {
				resultat.add(matchable);
			}
		}

		// Retourner la liste résultat
		return resultat;
	}
}
