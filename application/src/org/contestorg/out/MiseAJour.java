package org.contestorg.out;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosMiseAJour;
import org.contestorg.infos.InfosMiseAJour.Modification;
import org.contestorg.infos.InfosMiseAJour.Telechargement;
import org.contestorg.log.Log;
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
		// Récupérer la liste des mises à jour
		HTTPHelper.Browser browser = new HTTPHelper.Browser();
		HttpResponse response = browser.get("http://www.elfangels.fr", 80, "/contestorg/api/versions");
		
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
		
		// Vérifier si la dernière version est la même que celle actuelle
		if(elementVersion.getAttributeValue("numero").equals(ContestOrg.VERSION)) {
			return null;
		}
		
		// Récupérer le nom
		String numero = elementVersion.getAttributeValue("numero");
		
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
		
		// Récupérer la liste des évolutions développées
		ArrayList<Modification> evolutions = new ArrayList<Modification>();
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
		ArrayList<Modification> anomalies = new ArrayList<Modification>();
		if(elementVersion.getChild("listeModifications") != null) {
			Iterator iteratorAnomalies = elementVersion.getChild("listeModifications").getChildren().iterator();
			while (iteratorAnomalies.hasNext()) {
				Element evolution = (Element)iteratorAnomalies.next();
				anomalies.add(new Modification(
					evolution.getAttributeValue("description"),
					evolution.getAttributeValue("ticket")
				));
			}
		}
		
		// Récupérer la liste des revues effectuées
		ArrayList<Modification> revues = new ArrayList<Modification>();
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
		ArrayList<Modification> taches = new ArrayList<Modification>();
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
		
		// Créer et retourner la mise à jour
		return new InfosMiseAJour(numero, date, telechargements, evolutions, anomalies, revues, taches);
	}
	
}
