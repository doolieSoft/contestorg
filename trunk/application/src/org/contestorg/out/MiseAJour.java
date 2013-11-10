package org.contestorg.out;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.contestorg.comparators.CompVersions;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosMiseAJour;
import org.contestorg.infos.InfosMiseAJour.Modification;
import org.contestorg.infos.InfosMiseAJour.Telechargement;
import org.contestorg.log.Log;
import org.contestorg.preferences.Preferences;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Classe de vérification des mises à jour
 */
public class MiseAJour
{
	
	/**
	 * Vérifier si une mise à jour est disponible
	 * @return mise à jour disponible
	 */
	@SuppressWarnings("rawtypes")
	public static InfosMiseAJour verifier() {
		// Récupérer les préférences
		Preferences preferences = ContestOrg.get().getPreferences();
		
		// Vérifier s'il faut vérifier les mises à jour
		Boolean verifierMisesAJour = preferences.getBoolean(Preferences.VERIFIER_MISES_A_JOUR);
		if(verifierMisesAJour != null && verifierMisesAJour == true) {
			// Récupérer le timestamp
			long timestamp = Calendar.getInstance().getTimeInMillis();
			
			// Récupérer la date de la dernière vérification des mises à jour
			Integer nbJoursVerifierMisesAJour = preferences.getInteger(Preferences.NB_JOURS_VERIFIER_MISES_A_JOUR);
			Long derniereVerificationMisesAJour = preferences.getLong(Preferences.DERNIERE_VERIFICATION_MISES_A_JOUR);
			if(derniereVerificationMisesAJour == null || nbJoursVerifierMisesAJour == null) {
				// Initialiser les informations
				nbJoursVerifierMisesAJour = 7;
				derniereVerificationMisesAJour = (long)0;
				preferences.setAndSave(Preferences.NB_JOURS_VERIFIER_MISES_A_JOUR, nbJoursVerifierMisesAJour);
			}
			
			// Vérifier si la dernière vérification des mises à jour n'est pas trop récente
			if((timestamp-derniereVerificationMisesAJour)/(1000*3600*24) > nbJoursVerifierMisesAJour) {
				// Retenir la date de vérification des mises à jour
				preferences.setAndSave(Preferences.DERNIERE_VERIFICATION_MISES_A_JOUR, timestamp);
				
				// Message de log
				Log.getLogger().info("Vérification des mises à jour...");
				
				// Récupérer la liste des mises à jour
				HTTPHelper.Browser browser = new HTTPHelper.Browser();
				HttpResponse response = browser.get("http://localhost", 80, "/Projets/ContestOrg/api/versions");
				
				// Vérifier si la requête a réussie
				if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode() != 200) {
					return null;
				}
				
				// Extraire le contenu de la réponse
				String xml = null;
				try {
					xml = EntityUtils.toString(response.getEntity(),"UTF8");
				} catch (Exception e) {
					Log.getLogger().error("Erreur lors de la vérification d'une mise à jour",e);
					return null;
				}
						
				// Parser le contenu de la réponse
				Document document = null;
				try {
					document = new SAXBuilder().build(new StringReader(xml));
				} catch (Exception e) {
					Log.getLogger().error("Erreur lors de la vérification d'une mise à jour",e);
					return null;
				}
				
				// Récupérer la dernière version
				Element elementVersion = document.getRootElement().getChild("version");
				
				// Vérifier si la dernière version a bien été récupérée
				if(elementVersion == null) {
					return null;
				}
				
				// Comparateur de versions
				Comparator<String> comparateurVersions = new CompVersions();
				
				// Récupérer le numéro de version
				String numero = elementVersion.getAttributeValue("numero");
				
				// Vérifier si la dernière version est supérieure à la version actuelle
				if(comparateurVersions.compare(numero, ContestOrg.VERSION) < 0) {
					// Message de log
					Log.getLogger().info("Pas de nouvelles mises à jour depuis la "+ContestOrg.VERSION);
					
					// Retourner null
					return null;
				}
				
				// Message de log
				Log.getLogger().info("Nouvelle version "+numero+" disponible");
				
				// Récupérer et parser la date de publication
				Matcher matcher = Pattern.compile("([0-9]{2,2})/([0-9]{2,2})/([0-9]{4,4})").matcher(elementVersion.getAttributeValue("date"));
				matcher.matches();
				Calendar date = Calendar.getInstance();
				date.set(
					Integer.parseInt(matcher.group(3)),
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(1))
				);
				
				// Récupérer la liste des téléchargements
				ArrayList<Telechargement> telechargements = new ArrayList<Telechargement>();
				if(elementVersion.getChild("listeTelechargements") != null) {
					Iterator iteratorTelechargements = elementVersion.getChild("listeTelechargements").getChildren().iterator();
					while (iteratorTelechargements.hasNext()) {
						Element telechargement = (Element)iteratorTelechargements.next();
						telechargements.add(new Telechargement(
							telechargement.getAttributeValue("nom"),
							telechargement.getAttributeValue("url")
						));
					}
				}
				
				// Initialiser les listes de modifications
				ArrayList<Modification> evolutions = new ArrayList<Modification>();
				ArrayList<Modification> anomalies = new ArrayList<Modification>();
				ArrayList<Modification> revues = new ArrayList<Modification>();
				ArrayList<Modification> taches = new ArrayList<Modification>();
				
				// Récupérer la liste des versions
				Iterator iteratorVersions = document.getRootElement().getChildren().iterator();
				while (iteratorVersions.hasNext()) {
					elementVersion = (Element)iteratorVersions.next();
					
					// Vérifier si la version est supérieure à la version actuelle
					if(comparateurVersions.compare(elementVersion.getAttributeValue("numero"), ContestOrg.VERSION) > 0) {
						// Récupérer la liste des évolutions développées
						if(elementVersion.getChild("listeEvolutions") != null) {
							Iterator iteratorEvolutions = elementVersion.getChild("listeEvolutions").getChildren().iterator();
							while (iteratorEvolutions.hasNext()) {
								Element evolution = (Element)iteratorEvolutions.next();
								evolutions.add(new Modification(
									evolution.getAttributeValue("description"),
									evolution.getAttributeValue("ticket")
								));
							}
						}
						
						// Récupérer la liste des anomalies corrigées
						if(elementVersion.getChild("listeAnomalies") != null) {
							Iterator iteratorAnomalies = elementVersion.getChild("listeAnomalies").getChildren().iterator();
							while (iteratorAnomalies.hasNext()) {
								Element evolution = (Element)iteratorAnomalies.next();
								anomalies.add(new Modification(
									evolution.getAttributeValue("description"),
									evolution.getAttributeValue("ticket")
								));
							}
						}
						
						// Récupérer la liste des revues effectuées
						if(elementVersion.getChild("listeRevues") != null) {
							Iterator iteratorRevues = elementVersion.getChild("listeRevues").getChildren().iterator();
							while (iteratorRevues.hasNext()) {
								Element evolution = (Element)iteratorRevues.next();
								revues.add(new Modification(
									evolution.getAttributeValue("description"),
									evolution.getAttributeValue("ticket")
								));
							}
						}
						
						// Récupérer la liste des tâches effectuées
						if(elementVersion.getChild("listeTaches") != null) {
							Iterator iteratorTaches = elementVersion.getChild("listeTaches").getChildren().iterator();
							while (iteratorTaches.hasNext()) {
								Element evolution = (Element)iteratorTaches.next();
								taches.add(new Modification(
									evolution.getAttributeValue("description"),
									evolution.getAttributeValue("ticket")
								));
							}
						}
					}
				}
				
				// Créer et retourner la mise à jour
				return new InfosMiseAJour(numero, date, telechargements, evolutions, anomalies, revues, taches);
			}
		}
		
		// Retourner null
		return null;
	}
	
}
