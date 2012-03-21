package org.contestorg.preferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.contestorg.controllers.ContestOrg;

/**
 * Préférences
 */
public class Preferences
{
	
	/** Préférences */
	private Properties preferences;
	
	// Préférences
	
	/** Vérifier les mises à jour */
	public static final String VERIFIER_MISES_A_JOUR = "verifierMisesAJour";
	
	/**
	 * Constructeur 
	 */
	public Preferences() {
		try {
			// Charger les préférences
			this.preferences = new Properties();
			this.preferences.load(new FileInputStream("conf/preferences.ini"));
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du chargement des préférences", e);
		}
	}
	
	/**
	 * Récupérer la valeur d'une préférence
	 * @param key clé de la préférence
	 * @return valeur de la préférence
	 */
	public String getString(String key) {
		return this.preferences.getProperty(key);
	}
	
	/**
	 * Récupérer la valeur d'une préférence
	 * @param key clé de la préférence
	 * @return valeur de la préférence
	 */
	public Boolean getBoolean(String key) {
		String value = this.preferences.getProperty(key);
		if(value == null) {
			return null;
		}
		return this.preferences.getProperty(key).equals("true");
	}
	
	/**
	 * Définir une préférence
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void set(String key,String value) {
		this.preferences.setProperty(key, value);
	}
	
	/**
	 * Définir une préférence
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void set(String key,boolean value) {
		this.preferences.setProperty(key, value ? "true" : "false");
	}
	
	/**
	 * Définir une préférence et la sauvegarder
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void setAndSave(String key,String value) {
		this.set(key, value);
		this.save();
	}
	
	/**
	 * Définir une préférence et la sauvegarder
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void setAndSave(String key,boolean value) {
		this.set(key, value);
		this.save();
	}
	
	/**
	 * Sauvegarder les préférences
	 */
	public void save() {
		try {
			// Sauvegarder les préférences
			FileOutputStream out = new FileOutputStream("conf/preferences.ini");
			this.preferences.store(out, null);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des préférences", e);
		}
	}
	
}
