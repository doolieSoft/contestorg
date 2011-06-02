package infos;

/**
 * Cette classe permet d'obtenir toutes les permutations d'un tableau
 */
public class Permutations
{

	/*
	 * Pour tout k, tel que 0 <= k < n!, le code suivant génère une unique permutation pour la séquence Sj, j=1...n
	 * fonction permutation(k, S) {
	 * pour j = 2 jusqu'à taille(S) {
	 * k := k/ (j-1);
	 * échanger S[(k mod j)+1] avec S[j] // notez que notre tableau est indexé à partir de 1
	 * }
	 * retourne S;
	 * }
	 */

	/**
	 * Effectue une permutation
	 * @param k
	 *        index de 0 à objects.length!
	 * @param objects
	 *        le tableau d'objets
	 * @return la permutation
	 */
	public static Object[] permutation (double k, Object[] objects) {
		Object[] permutation = objects.clone();
		for (int i = 2; i < permutation.length + 1; i++) {
			k = k / (i - 1);
			Permutations.swap(permutation, (int)(k % i), i - 1);
		}
		return permutation;
	}

	/**
	 * Permute deux objets dans un tableau d'objets
	 * @param objects
	 *        le tableau d'objet
	 * @param indexA
	 *        le premier index
	 * @param indexB
	 *        le deuxième index
	 */
	public static void swap (Object[] objects, int indexA, int indexB) {
		Object temp = objects[indexA];
		objects[indexA] = objects[indexB];
		objects[indexB] = temp;
	}

	/**
	 * Calcul la factorielle d'un nombre
	 */
	public static double factorial (int value) {
		double result = value;
		while (value != 2) {
			result *= --value;
		}
		return result;
	}

}
