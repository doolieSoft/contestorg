package org.contestorg.common;

/**
 * Permet d'obtenir toutes les permutations d'un tableau
 */
public class Permutations
{
	
	/*
	 * Pour tout k, tel que 0 <= k < n!, le code suivant génère une unique permutation pour la séquence Sj, j=1...n
	 * 
	 * fonction permutation(k, S) {
	 *    pour j = 2 jusqu'à taille(S) {
	 *       k := k/ (j-1);
	 *       échanger S[(k mod j)+1] avec S[j] // notez que notre tableau est indexé à partir de 1
	 *    }
	 *    retourne S;
	 * }
	 */

	/**
	 * Effectue une permutation
	 * @param <K>
	 * @param k index de 0 à objects.length!
	 * @param objects le tableau d'objets
	 * @return la permutation
	 */
	public static <K> K[] permutation (double k, K[] objects) {
		K[] permutation = objects.clone();
		for (int i = 2; i < permutation.length + 1; i++) {
			k = k / (i - 1);
			Permutations.swap(permutation, (int)(k % i), i - 1);
		}
		return permutation;
	}

	/**
	 * Permute deux objets parmis une liste d'objets
	 * @param <K>
	 * @param objects liste d'objets
	 * @param indexA indice du premier objet
	 * @param indexB indice du deuxième objet
	 */
	public static <K> void swap (K[] objects, int indexA, int indexB) {
		K temp = objects[indexA];
		objects[indexA] = objects[indexB];
		objects[indexB] = temp;
	}

	/**
	 * Calcul la factorielle d'un nombre 
	 * @param value nombre
	 * @return factorielle du nombre 
	 */
	public static double factorial (int value) {
		double result = value;
		while (value != 2) {
			result *= --value;
		}
		return result;
	}
	
}
