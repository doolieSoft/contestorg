package org.contestorg.out;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.Tools;
import org.contestorg.events.Action;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.infos.InfosThemeFichier;
import org.contestorg.interfaces.IHistoryListener;
import org.contestorg.models.FrontModel;
import org.contestorg.models.ModelAbstract;
import org.contestorg.models.ModelPoule;
import org.jdom.Element;

/**
 * Ressource abstraite de thème d'exportation/diffusion
 */
public abstract class ThemeRessourceAbstract implements IHistoryListener
{
	/**
	 * Créer les ressources associées à un thème
	 * @param theme thème
	 * @param refresh autoriser la mise à jour automatique des ressources ?
	 * @return liste des ressources du thème
	 * @throws ContestOrgOutException
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<ThemeRessourceAbstract> getRessources (InfosModelTheme theme, boolean refresh) throws ContestOrgWarningException {
		// Vérifier si le thème existe
		if(!(new File(theme.getChemin()).exists())) {
			throw new ContestOrgWarningException("Le thème \""+theme.getNom()+"\" n'a pas été retrouvé.");
		}
		
		// Initialiser les ressources
		ArrayList<ThemeRessourceAbstract> ressources = new ArrayList<ThemeRessourceAbstract>();
		
		// Ajouter les ressources
		Element configuration = ConfigurationTheme.getConfiguration(theme.getChemin());
		if (configuration.getChild("fichiers") != null) {
			List listeFichiers = configuration.getChild("fichiers").getChildren("fichier");
			Iterator i = listeFichiers.iterator();
			if (!i.hasNext()) {
				throw new ContestOrgWarningException("Aucun fichier n'a été défini dans le fichier de configuration du thème.");
			} else {
				while (i.hasNext()) {
					// Récupérer le fichier dans le DOM
					Element file = (Element)i.next();
					
					// Récupérer les paramètres du fichier
					String source = file.getAttributeValue("source");
					String cible = file.getAttributeValue("cible");
					if (cible == null) {
						cible = source;
					}
					boolean principal = file.getAttributeValue("principal") != null && file.getAttributeValue("principal").equals("oui");
					String transformation = file.getAttributeValue("transformation");
					
					// Utiliser la cible définie par l'utilisateur si nécéssaire
					if(theme.getFichiers().containsKey(file.getAttributeValue("id"))) {
						cible = theme.getFichiers().get(file.getAttributeValue("id"));
					}

					// Transformer les paramètres spéciaux dans la cible
					cible = ThemeRessourceAbstract.transform(cible, theme.getParametres());
					
					// Récupérer le fichier associé à la ressource
					File fichier = new File(theme.getChemin() + File.separator + source);
					
					// Créer et ajouter la ressource
					switch (Tools.stringCase(transformation, InfosThemeFichier.TRANSFORMATION_XSLT, InfosThemeFichier.TRANSFORMATION_FOP)) {
						case 0: // Ressource XSLT
							ressources.add(new ThemeRessourceXSLT(cible, principal, theme.getParametres(), theme.getFichiers(), fichier));
							break;
						case 1: // Ressource FOP
							ressources.add(new ThemeRessourceFOP(cible, principal, theme.getParametres(), theme.getFichiers(), fichier));
							break;
						default: // Ressource fichier
							ressources.add(new ThemeRessourceFichier(cible, principal, theme.getParametres(), theme.getFichiers(), fichier));
					}
				}
			}
		} else {
			// Erreur
			throw new ContestOrgWarningException("Aucun fichier n'a été défini dans le fichier de configuration du thème.");
		}
		
		// Autoriser la mise à jour automatique si nécéssaire
		if(refresh) {
			for(ThemeRessourceAbstract ressource : ressources) {
				ressource.setAutoRefresh();
			}
		}
		
		// Retourner les ressources
		return ressources;
	}
	
	/**
	 * Récupérer le type de contenu d'un fichier
	 * @param fichier fichier
	 * @return type de contenu du fichier
	 */
	public static String getContentType(String fichier) {
		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
		map.addMimeTypes("application/pdf pdf PDF");
		map.addMimeTypes("application/javascript js JS");
		map.addMimeTypes("text/css css CSS");
		map.addMimeTypes("text/html html HTML htm HTM");
		map.addMimeTypes("text/xml xml XML");
		return map.getContentType(fichier);
	}
	
	/**
	 * Transformer la valeur d'un paramètre en une chaine de caractères à partir de la liste des paramètres spéciaux
	 * @param valeur valeur du paramètre
	 * @param parametres liste des paramètres spéciaux
	 * @return chaine de caractère
	 */
	public static String transform(String valeur, HashMap<String, String> parametres) {
		// Id de la dernière phase dans une poule
		Matcher idDernierePhase = Pattern.compile("%idDernierePhase\\(([0-9]+),([0-9]+)\\)%").matcher(valeur);
		if (idDernierePhase.find()) {
			// Récupérer le model
			ModelAbstract model = ModelAbstract.search(Integer.parseInt(idDernierePhase.group(2)));
			
			// Vérifier si le model est bien une poule
			if(model != null && model instanceof ModelPoule) {
				// Caster le model en poule
				ModelPoule poule = (ModelPoule)model;
				
				// Remplacer le paramètre spécial par sa valeur
				if(poule.getPhasesQualificatives().size() != 0) {
					valeur = valeur.replace(idDernierePhase.group(0), String.valueOf(poule.getPhasesQualificatives().get(poule.getPhasesQualificatives().size()-1).getId()));
				} else {
					valeur = valeur.replace(idDernierePhase.group(0), "");
				}
			}
		}
		
		// Numéro de la dernière phase dans une poule indicé de 1 à N
		Matcher numeroDernierePhase = Pattern.compile("%numeroDernierePhase\\(([0-9]+),([0-9]+)\\)%").matcher(valeur);
		if (numeroDernierePhase.find()) {
			// Récupérer le model
			ModelAbstract model = ModelAbstract.search(Integer.parseInt(numeroDernierePhase.group(2)));
			
			// Vérifier si le model est bien une poule
			if(model != null && model instanceof ModelPoule) {
				// Caster le model en poule
				ModelPoule poule = (ModelPoule)model;
				
				// Remplacer le paramètre spécial par sa valeur
				valeur = valeur.replace(numeroDernierePhase.group(0), String.valueOf(poule.getPhasesQualificatives().size()));
			}
		}
		
		// Retourner la valeur
		return valeur;
	}
	
	/** Cible */
	protected String cible;
	
	/** Ressource principale ? */
	private boolean principale;
	
	/** Liste des paramètres */
	private HashMap<String, String> parametres;
	
	/** Liste des fichiers */
	private HashMap<String, String> fichiers;
	
	/** Mutex d'accès à la ressource */
	private ReentrantLock mutex = new ReentrantLock();
	
	/**
	 * Constructeur
	 * @param cible cible
	 * @param principale ressource principale ?
	 * @param parametres liste des paramètres
	 * @param fichiers liste des fichiers
	 */
	public ThemeRessourceAbstract(String cible, boolean principale, HashMap<String, String> parametres, HashMap<String, String> fichiers) {
		// Retenir les informations
		this.cible = cible;
		this.principale = principale;
		this.parametres = parametres;
		this.fichiers = fichiers;
	}
	
	// Getters
	
	/**
	 * Récupérer le fichier associé à la ressource
	 * @return fichier associé à la ressource
	 */
	public abstract File getFichier ();
	
	/**
	 * Récupérer le type de contenu du fichier associé à la ressource
	 * @return type de contenu du fichier associé à la ressource
	 */
	public abstract String getContentType ();
	
	/**
	 * Récupérer la cible
	 * @return cible
	 */
	public String getCible () {
		return this.cible;
	}
	
	/**
	 * Vérifier s'il s'agit d'une ressource principale
	 * @return resource principal
	 */
	public boolean isPrincipale () {
		return this.principale;
	}
	
	/**
	 * Récupérer la liste des paramètres
	 * @return liste des paramètres
	 */
	protected HashMap<String, String> getParametres () {
		// Initialiser la liste des parametres
		HashMap<String, String> parametres = new HashMap<String, String>();
		
		// Parcourir tous les paramètres
		for (Entry<String, String> parametre : this.parametres.entrySet()) {
			// Extraire la valeur
			String valeur = parametre.getValue();
			
			// Transformer la valeur si nécéssaire
			if(valeur != null) {
				valeur = ThemeRessourceAbstract.transform(valeur, this.parametres);
			}
			
			// Ajouter le paramètre
			parametres.put(parametre.getKey(), valeur);
		}
		
		// Retourner les paramètres
		return parametres;
	}
	
	/**
	 * Récupérer la liste des fichier
	 * @return liste des fichier
	 */
	protected HashMap<String,String> getFichiers() {
		return this.fichiers;
	}
		
	/**
	 * Nettoyer la ressource
	 */
	public abstract void clean ();
	
	/**
	 * Rafraichir la ressource
	 * @return opération réussie ?
	 */
	protected abstract boolean refresh ();
		
	/**
	 * Vérifier si la ressource est associée à une transformation
	 * @return ressource associée à une transformation ?
	 */
	public abstract boolean isTransformation();
	
	/**
	 * Autoriser la mise à jour automatique de la ressource
	 */
	public void setAutoRefresh() { 
		// Ecouter l'historique
		FrontModel.get().getHistory().addListener(this);
	}
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		if(mutex.getQueueLength() < 1) {
			new Thread(new Refresh()).start();
		}
	}
	@Override
	public void historyActionPoped (Action action) {
		if(mutex.getQueueLength() < 1) {
			new Thread(new Refresh()).start();
		}
	}
	public void historyInit () {
		if(mutex.getQueueLength() < 1) {
			new Thread(new Refresh()).start();
		}
	}
	
	// Vérouiller/Dévérouiller la ressource
	
	/**
	 * Vérouiller la ressource
	 */
	public void lock() {
		this.mutex.lock();
	}
	
	/**
	 * Dévérouiller la ressource
	 */
	public void unlock() {
		this.mutex.unlock();
	}
	
	/**
	 * Classe permettant de rafraichir la ressource sans bloquer l'appel
	 */
	private class Refresh implements Runnable
	{
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {
			// Prendre jeton du mutex
			mutex.lock();
			
			// Nettoyer la ressource
			clean();
			
			// Rafraichir la ressource
			refresh();
			
			// Relacher jeton du mutex
			mutex.unlock();
		}
	}
	
}
