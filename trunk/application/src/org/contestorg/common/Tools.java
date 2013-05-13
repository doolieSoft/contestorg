package org.contestorg.common;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Ensemble d'outils
 */
public class Tools
{
	/**
	 * Méthode permettant d'effectuer un switch case avec une chaîne de caractères
	 * @param string chaîne de caractères d'entrée
	 * @param strings chaînes de caractères à comparer avec la chaîne de caractères d'entrée
	 * @return indice de la chaîne de caractères correspondant
	 */
	public static int stringCase (String string, String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals(string)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Trouver le chemin relatif entre deux fichiers
	 * @param target fichier cible
	 * @param base fichier base
	 * @return chemin relatif entre les deux fichiers
	 * @throws IOException
	 */
	public static String getRelativePath (File target, File base) throws IOException {
		String[] baseComponents = base.getCanonicalPath().split(Pattern.quote(File.separator));
		String[] targetComponents = target.getCanonicalPath().split(Pattern.quote(File.separator));
		int index = 0;
		for (; index < targetComponents.length && index < baseComponents.length; ++index) {
			if (!targetComponents[index].equals(baseComponents[index])) {
				break;
			}
		}
		StringBuilder result = new StringBuilder();
		if (index != baseComponents.length) {
			for (int i = index; i < baseComponents.length; ++i) {
				result.append(".." + File.separator);
			}
		}
		for (; index < targetComponents.length; ++index) {
			result.append(targetComponents[index] + File.separator);
		}
		if (!target.getPath().endsWith("/") && !target.getPath().endsWith("\\")) {
			result.delete(result.length() - "/".length(), result.length());
		}
		return result.toString();
	}

	// Types de hashage
	public static final String HASH_MD5 = "MD5";
	public static final String HASH_SHA1 = "SHA-1";
	
	/**
	 * Réalise le hash d'une chaîne de caractères
	 * @param string chaine de caractère à hasher
	 * @param hash hashage à utiliser
	 * @return hash de la chaîne de caractères
	 * @throws NoSuchAlgorithmException 
	 */
	public static String hash(String string, String hash) throws NoSuchAlgorithmException {
		byte[] digest = MessageDigest.getInstance(hash).digest(string.getBytes());
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<digest.length;i++) { 
            int halfbyte = (digest[i] >>> 4) & 0x0F;
            int j = 0;
            do {
                if (0 <= halfbyte && halfbyte <= 9) {
                	buffer.append((char) ('0' + halfbyte));
                } else { 
                    buffer.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = digest[i] & 0x0F;
            } while(j++ < 1);
        }
        return buffer.toString();
	}
	
	/**
	 * Récupérer la stracktrace d'un objet Throwable
	 * @param throwable object throwable
	 * @return stracktrace de l'objet throwable
	 */
	public static String getStackTrace(Throwable throwable) {
		StringBuilder stackTrace = new StringBuilder();
	    for (StackTraceElement element : throwable.getStackTrace()) {
	        stackTrace.append(element.toString());
	        stackTrace.append("\r\n");
	    }
	    return stackTrace.toString();
	}
}
