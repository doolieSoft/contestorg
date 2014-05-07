package org.contestorg.preferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.contestorg.controllers.ContestOrg;

/**
 * Préférences
 */
public class Preferences
{
	/** Unique instance des préférences */
	private static Preferences instance;
	
	/** Préférences */
	private Properties preferences;
	
	// Préférences
	
	/** Vérifier les mises à jour */
	public static final String VERIFIER_MISES_A_JOUR = "verifierMisesAJour";
	
	/** Nombre de jours entre chaque vérification des mises à jour */
	public static final String NB_JOURS_VERIFIER_MISES_A_JOUR = "nbJoursVerifierMisesAJour";
	
	/** Date de la dernière vérification des mises à jour */
	public static final String DERNIERE_VERIFICATION_MISES_A_JOUR = "derniereVerificationMisesAJour";

	/**
	 * Récupérer l'unique instance des préférences
	 * @return unique instance des préférences
	 */
	public synchronized static Preferences getInstance () {
		// Créer l'unique instance des préférences si nécessaire
		if (Preferences.instance == null) {
			Preferences.instance = new Preferences();
		}
		
		// Retourner l'unique instance des préférences
		return Preferences.instance;
	}
	
	/**
	 * Constructeur 
	 */
	private Preferences() {
		FileInputStream inputStream = null;
		try {
			// Charger les préférences
			inputStream = new FileInputStream("conf/preferences.ini");
			this.preferences = new Properties();
			this.preferences.load(inputStream);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors du chargement des préférences", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					ContestOrg.get().erreur("Erreur lors du chargement des préférences", e);
				}
			}
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
	 * Récupérer la valeur d'une préférence
	 * @param key clé de la préférence
	 * @return valeur de la préférence
	 */
	public Integer getInteger(String key) {
		String value = this.preferences.getProperty(key);
		if(value == null) {
			return null;
		}
		try {
			return Integer.parseInt(this.preferences.getProperty(key));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Récupérer la valeur d'une préférence
	 * @param key clé de la préférence
	 * @return valeur de la préférence
	 */
	public Long getLong(String key) {
		String value = this.preferences.getProperty(key);
		if(value == null) {
			return null;
		}
		try {
			return Long.parseLong(this.preferences.getProperty(key));
		} catch (NumberFormatException e) {
			return null;
		}
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
	 * Définir une préférence
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void set(String key,int value) {
		this.preferences.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Définir une préférence
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void set(String key,long value) {
		this.preferences.setProperty(key, String.valueOf(value));
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
	 * Définir une préférence et la sauvegarder
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void setAndSave(String key,int value) {
		this.set(key, value);
		this.save();
	}
	
	/**
	 * Définir une préférence et la sauvegarder
	 * @param key clé de la préférence
	 * @param value valeur de la préférence
	 */
	public void setAndSave(String key,long value) {
		this.set(key, value);
		this.save();
	}
	
	/**
	 * Sauvegarder les préférences
	 */
	public synchronized void save() {
		FileOutputStream outputStream = null;
		try {
			// Sauvegarder les préférences
			outputStream = new FileOutputStream("conf/preferences.ini");
			this.preferences.store(outputStream, null);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de la modification des préférences", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					ContestOrg.get().erreur("Erreur lors de la modification des préférences", e);
				}
			}
		}
	}
	
}
