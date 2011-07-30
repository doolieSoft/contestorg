package common;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Tools
{
	// Méthode permettant d'effectuer un case avec un string
	public static int StringCase (String string, String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals(string)) {
				return i;
			}
		}
		return -1;
	}
	
	// Trouver le chemin relatif entre deux fichiers
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
}
