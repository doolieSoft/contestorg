package out;

import infos.InfosConnexionFTP;
import infos.InfosModelCategorie;
import infos.InfosModelCheminFTP;
import infos.InfosModelCheminLocal;
import infos.InfosModelCompPhasesQualifsObjectif;
import infos.InfosModelCompPhasesQualifsPoints;
import infos.InfosModelCompPhasesQualifsVictoires;
import infos.InfosModelConcours;
import infos.InfosModelDiffusion;
import infos.InfosModelEmplacement;
import infos.InfosModelEquipe;
import infos.InfosModelEquipe.Statut;
import infos.InfosModelExportation;
import infos.InfosModelHoraire;
import infos.InfosModelLieu;
import infos.InfosModelMatchPhasesElims;
import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelObjectif;
import infos.InfosModelObjectifNul;
import infos.InfosModelObjectifPoints;
import infos.InfosModelObjectifPourcentage;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;
import infos.InfosModelPhaseEliminatoires;
import infos.InfosModelPhaseQualificative;
import infos.InfosModelPoule;
import infos.InfosModelPrix;
import infos.InfosModelPropriete;
import infos.InfosModelProprieteEquipe;
import infos.InfosModelTheme;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import log.Log;
import models.ContestOrgModelException;
import models.FrontModel;
import models.ModelAbstract;
import models.ModelCategorie;
import models.ModelCheminAbstract;
import models.ModelCheminFTP;
import models.ModelCheminLocal;
import models.ModelCompPhasesQualifsAbstract;
import models.ModelCompPhasesQualifsObjectif;
import models.ModelCompPhasesQualifsPoints;
import models.ModelCompPhasesQualifsVictoires;
import models.ModelConcours;
import models.ModelDiffusion;
import models.ModelEmplacement;
import models.ModelEquipe;
import models.ModelExportation;
import models.ModelHoraire;
import models.ModelLieu;
import models.ModelMatchAbstract;
import models.ModelMatchPhasesElims;
import models.ModelMatchPhasesQualifs;
import models.ModelObjectif;
import models.ModelObjectifRemporte;
import models.ModelParticipation;
import models.ModelPhaseQualificative;
import models.ModelPhasesEliminatoires;
import models.ModelPoule;
import models.ModelPrix;
import models.ModelPropriete;
import models.ModelProprietePossedee;
import models.ModelTheme;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import common.Tools;
import comparators.CompPhasesElims;
import comparators.CompPhasesQualifs;

public class PersistanceXML extends PersistanceAbstract
{
	// Chemin
	private String chemin;
	
	// Constructeur
	public PersistanceXML(String chemin) {
		// Retenir le chemin
		this.chemin = chemin;
	}
	
	// Implémentation de load
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ModelConcours load () {
		try {
			// Construire le document JDom à partir du fichier
			Document document = new SAXBuilder().build(new File(this.chemin));
			
			// Récupérer l'élément root
			Element root = document.getRootElement();
			
			// Récupérer les données sur le concours
			String concoursNom = root.getAttributeValue("nom");
			String concoursSite = root.getAttributeValue("site");
			String concoursLieu = root.getAttributeValue("lieu");
			String concoursEmail = root.getAttributeValue("email");
			String concoursTelephone = root.getAttributeValue("telephone");
			String concoursDescription = root.getAttributeValue("description");
			
			// Récupérer les données sur l'organisme
			Element elementOrganisateur = root.getChild("organisateur");
			String organismeNom = elementOrganisateur == null ? null : elementOrganisateur.getAttributeValue("nom");
			String organismeSite = elementOrganisateur == null ? null : elementOrganisateur.getAttributeValue("site");
			String organismeLieu = elementOrganisateur == null ? null : elementOrganisateur.getAttributeValue("lieu");
			String organismeEmail = elementOrganisateur == null ? null : elementOrganisateur.getAttributeValue("email");
			String organismeTelephone = elementOrganisateur == null ? null : elementOrganisateur.getAttributeValue("telephone");
			String organismeDescription = elementOrganisateur == null ? null : elementOrganisateur.getAttributeValue("description");
			
			// Récupérer les données sur le type de qualifications/participants
			int typeQualifications = root.getAttributeValue("qualifications").equals("phases") ? InfosModelConcours.QUALIFICATIONS_PHASES : InfosModelConcours.QUALIFICATIONS_GRILLE;
			int typeParticipants = root.getAttributeValue("participants").equals("equipes") ? InfosModelConcours.PARTICIPANTS_EQUIPES : InfosModelConcours.PARTICIPANTS_JOUEURS;
			
			// Récupérer les données sur les points
			Element elementPoints = root.getChild("points");
			double pointsVictoire = Double.parseDouble(elementPoints.getAttributeValue("victoire"));
			double pointsEgalite = Double.parseDouble(elementPoints.getAttributeValue("egalite"));
			double pointsDefaite = Double.parseDouble(elementPoints.getAttributeValue("defaite"));
			
			// Récupérer les données de programmation
			Element elementProgrammation = root.getChild("programmation");
			Double programmationDuree = elementProgrammation == null || elementProgrammation.getAttributeValue("duree") == null ? null : Double.parseDouble(elementProgrammation.getAttributeValue("duree"));
			Double programmationInterval = elementProgrammation == null || elementProgrammation.getAttributeValue("interval") == null ? null : Double.parseDouble(elementProgrammation.getAttributeValue("interval"));
			Double programmationPause = elementProgrammation == null || elementProgrammation.getAttributeValue("pause") == null ? null : Double.parseDouble(elementProgrammation.getAttributeValue("pause"));
			
			// Construire le concours
			ModelConcours concours = new ModelConcours(new InfosModelConcours(concoursNom, concoursSite, concoursLieu, concoursEmail, concoursTelephone, concoursDescription, organismeNom, organismeSite, organismeLieu, organismeEmail, organismeTelephone, organismeDescription, typeQualifications, typeParticipants, pointsVictoire, pointsEgalite, pointsDefaite, programmationDuree, programmationInterval, programmationPause));
			concours.setId(root.getAttribute("id") == null ? -1 : Integer.parseInt(root.getAttributeValue("id")));
			
			// Ajouter les objectifs
			if (root.getChild("listeObjectifs") != null) {
				Iterator iteratorObjectifs = root.getChild("listeObjectifs").getChildren("objectif").iterator();
				while (iteratorObjectifs.hasNext()) {
					Element elementObjectif = (Element)iteratorObjectifs.next();
					if (elementObjectif.getChild("objectifPoints") != null) {
						Element elementObjectifPoints = elementObjectif.getChild("objectifPoints");
						ModelObjectif objectif = new ModelObjectif(concours, new InfosModelObjectifPoints(elementObjectifPoints.getAttributeValue("nom"), Double.parseDouble(elementObjectifPoints.getAttributeValue("points")), elementObjectifPoints.getAttributeValue("borneParticipation") == null ? null : Double.parseDouble(elementObjectifPoints.getAttributeValue("borneParticipation"))));
						objectif.setId(Integer.parseInt(elementObjectif.getAttributeValue("id")));
						concours.addObjectif(objectif);
					} else if (elementObjectif.getChild("objectifPourcentage") != null) {
						Element elementObjectifPourcentage = elementObjectif.getChild("objectifPourcentage");
						ModelObjectif objectif = new ModelObjectif(concours, new InfosModelObjectifPourcentage(elementObjectifPourcentage.getAttributeValue("nom"), Double.parseDouble(elementObjectifPourcentage.getAttributeValue("pourcentage")), elementObjectifPourcentage.getAttributeValue("borneParticipation") == null ? null : Double.parseDouble(elementObjectifPourcentage.getAttributeValue("borneParticipation")), elementObjectifPourcentage.getAttributeValue("borneAugmentation") == null ? null : Double.parseDouble(elementObjectifPourcentage.getAttributeValue("borneAugmentation"))));
						objectif.setId(Integer.parseInt(elementObjectif.getAttributeValue("id")));
						concours.addObjectif(objectif);
					} else if (elementObjectif.getChild("objectifNul") != null) {
						ModelObjectif objectif = new ModelObjectif(concours, new InfosModelObjectifNul(elementObjectif.getChild("objectifNul").getAttributeValue("nom")));
						objectif.setId(Integer.parseInt(elementObjectif.getAttributeValue("id")));
						concours.addObjectif(objectif);
					}
				}
			}
			
			// Ajouter les critères de classement
			if (root.getChild("listeCriteres") != null) {
				Iterator iteratorCriteres = root.getChild("listeCriteres").getChildren("critere").iterator();
				while (iteratorCriteres.hasNext()) {
					Element elementCritere = (Element)iteratorCriteres.next();
					if (elementCritere.getChild("critereNbPoints") != null) {
						ModelCompPhasesQualifsPoints comparateur = new ModelCompPhasesQualifsPoints(concours, new InfosModelCompPhasesQualifsPoints());
						comparateur.setId(Integer.parseInt(elementCritere.getAttributeValue("id")));
						concours.addCompPhasesQualifs(comparateur);
					} else if (elementCritere.getChild("critereNbVictoires") != null) {
						ModelCompPhasesQualifsVictoires comparateur = new ModelCompPhasesQualifsVictoires(concours, new InfosModelCompPhasesQualifsVictoires());
						comparateur.setId(Integer.parseInt(elementCritere.getAttributeValue("id")));
						concours.addCompPhasesQualifs(comparateur);
					} else if (elementCritere.getChild("critereNbObjectifs") != null) {
						ModelObjectif objectif = (ModelObjectif)ModelAbstract.search(Integer.parseInt(elementCritere.getChild("critereNbObjectifs").getAttributeValue("refObjectif")));
						ModelCompPhasesQualifsObjectif comparateur = new ModelCompPhasesQualifsObjectif(concours, objectif, new InfosModelCompPhasesQualifsObjectif(null));
						comparateur.setId(Integer.parseInt(elementCritere.getAttributeValue("id")));
						concours.addCompPhasesQualifs(comparateur);
						objectif.addCompPhasesQualifs(comparateur);
					}
				}
			}
			
			// Ajouter les lieux, les emplacements et les horaires
			if (root.getChild("listeLieux") != null) {
				Iterator iteratorLieux = root.getChild("listeLieux").getChildren("lieu").iterator();
				while (iteratorLieux.hasNext()) {
					Element elementLieu = (Element)iteratorLieux.next();
					ModelLieu lieu = new ModelLieu(concours, new InfosModelLieu(elementLieu.getAttributeValue("nom"), elementLieu.getAttributeValue("lieu"), elementLieu.getAttributeValue("telephone"), elementLieu.getAttributeValue("email"), elementLieu.getAttributeValue("description")));
					lieu.setId(Integer.parseInt(elementLieu.getAttributeValue("id")));
					
					if (elementLieu.getChild("listeHoraires") != null) {
						Iterator iteratorHoraires = elementLieu.getChild("listeHoraires").getChildren("horaire").iterator();
						while (iteratorHoraires.hasNext()) {
							Element elementHoraire = (Element)iteratorHoraires.next();
							ModelHoraire horaire = new ModelHoraire(lieu, new InfosModelHoraire(Integer.parseInt(elementHoraire.getAttributeValue("jours")), Integer.parseInt(elementHoraire.getAttributeValue("debut")), Integer.parseInt(elementHoraire.getAttributeValue("fin"))));
							horaire.setId(Integer.parseInt(elementHoraire.getAttributeValue("id")));
							lieu.addHoraire(horaire);
						}
					}
					
					Iterator iteratorEmplacements = elementLieu.getChild("listeEmplacements").getChildren("emplacement").iterator();
					while (iteratorEmplacements.hasNext()) {
						Element elementEmplacement = (Element)iteratorEmplacements.next();
						ModelEmplacement emplacement = new ModelEmplacement(lieu, new InfosModelEmplacement(elementEmplacement.getAttributeValue("nom"), elementEmplacement.getAttributeValue("description")));
						emplacement.setId(Integer.parseInt(elementEmplacement.getAttributeValue("id")));
						lieu.addEmplacement(emplacement);
					}
					
					concours.addLieu(lieu);
				}
			}
			
			// Ajouter les prix
			if (root.getChild("listePrix") != null) {
				Iterator iteratorPrix = root.getChild("listePrix").getChildren("prix").iterator();
				while (iteratorPrix.hasNext()) {
					Element elementPrix = (Element)iteratorPrix.next();
					ModelPrix prix = new ModelPrix(concours, new InfosModelPrix(elementPrix.getAttributeValue("nom")));
					prix.setId(Integer.parseInt(elementPrix.getAttributeValue("id")));
					concours.addPrix(prix);
				}
			}
			
			// Ajouter les propriétés
			if (root.getChild("listeProprietes") != null) {
				Iterator iteratorProprietes = root.getChild("listeProprietes").getChildren("propriete").iterator();
				while (iteratorProprietes.hasNext()) {
					Element elementPropriete = (Element)iteratorProprietes.next();
					int type = -1;
					switch (Tools.StringCase(elementPropriete.getAttributeValue("type"), "entier", "decimal", "texte")) {
						case 0:
							type = InfosModelPropriete.TYPE_INT;
							break;
						case 1:
							type = InfosModelPropriete.TYPE_FLOAT;
							break;
						case 2:
							type = InfosModelPropriete.TYPE_STRING;
							break;
					}
					ModelPropriete propriete = new ModelPropriete(concours, new InfosModelPropriete(elementPropriete.getAttributeValue("nom"), type, elementPropriete.getAttributeValue("obligatoire").equals("oui")));
					propriete.setId(Integer.parseInt(elementPropriete.getAttributeValue("id")));
					concours.addPropriete(propriete);
				}
			}
			
			// Ajouter les exportations
			if (root.getChild("listeExportations") != null) {
				Iterator iteratorExportations = root.getChild("listeExportations").getChildren("exportation").iterator();
				while (iteratorExportations.hasNext()) {
					Element elementExportation = (Element)iteratorExportations.next();
					
					Element elementChemin = elementExportation.getChild("chemin");
					ModelCheminAbstract chemin = null;
					if (elementChemin.getChild("cheminLocal") != null) {
						chemin = new ModelCheminLocal(new InfosModelCheminLocal(elementChemin.getChild("cheminLocal").getAttributeValue("chemin")));
					} else if (elementChemin.getChild("cheminFTP") != null) {
						chemin = new ModelCheminFTP(new InfosModelCheminFTP(new InfosConnexionFTP(elementChemin.getChild("cheminFTP").getAttributeValue("host"), Integer.parseInt(elementChemin.getChild("cheminFTP").getAttributeValue("port")), elementChemin.getChild("cheminFTP").getAttributeValue("username"), elementChemin.getChild("cheminFTP").getAttributeValue("password"), elementChemin.getChild("cheminFTP").getAttributeValue("path"), elementChemin.getChild("cheminFTP").getAttributeValue("path").equals("actif") ? InfosConnexionFTP.MODE_ACTIF : InfosConnexionFTP.MODE_PASSIF)));
					}
					chemin.setId(Integer.parseInt(elementChemin.getAttributeValue("id")));
					
					Element elementTheme = elementExportation.getChild("theme");
					HashMap<String, String> parametres = new HashMap<String, String>();
					if(elementTheme.getChild("listeParametres") != null) {
						Iterator iteratorParametres = elementTheme.getChild("listeParametres").getChildren("parametre").iterator();
						while (iteratorParametres.hasNext()) {
							Element parametre = (Element)iteratorParametres.next();
							parametres.put(parametre.getAttributeValue("id"), parametre.getAttributeValue("valeur"));
						}
					}
					HashMap<String, String> fichiers = new HashMap<String, String>();
					if(elementTheme.getChild("listeFichiers") != null) {
						Iterator iteratorFichiers = elementTheme.getChild("listeFichiers").getChildren("fichier").iterator();
						while (iteratorFichiers.hasNext()) {
							Element fichier = (Element)iteratorFichiers.next();
							fichiers.put(fichier.getAttributeValue("id"), fichier.getAttributeValue("cible"));
						}
					}
					ModelTheme theme = new ModelTheme(new InfosModelTheme(elementTheme.getAttributeValue("chemin").replace("/", File.separator), parametres, fichiers));
					theme.setId(Integer.parseInt(elementTheme.getAttributeValue("id")));
					
					ModelExportation exportation = new ModelExportation(concours, chemin, theme, new InfosModelExportation(elementExportation.getAttributeValue("nom"), elementExportation.getAttributeValue("automatique").equals("oui")));
					exportation.setId(Integer.parseInt(elementExportation.getAttributeValue("id")));
					concours.addExportation(exportation);
				}
			}
			
			// Ajouter la publication
			if (root.getChild("publication") != null) {
				concours.setPublication((ModelExportation)ModelAbstract.search(Integer.parseInt(root.getChild("publication").getAttributeValue("refExportation"))));
			}
			
			// Ajouter les diffusions
			if (root.getChild("listeDiffusions") != null) {
				Iterator iteratorDiffusions = root.getChild("listeDiffusions").getChildren("diffusion").iterator();
				while (iteratorDiffusions.hasNext()) {
					Element elementDiffusion = (Element)iteratorDiffusions.next();
					
					Element elementTheme = elementDiffusion.getChild("theme");
					HashMap<String, String> parametres = new HashMap<String, String>();
					if(elementTheme.getChild("listeParametres") != null) {
						Iterator iteratorParametres = elementTheme.getChild("listeParametres").getChildren("parametre").iterator();
						while (iteratorParametres.hasNext()) {
							Element parametre = (Element)iteratorParametres.next();
							parametres.put(parametre.getAttributeValue("id"), parametre.getAttributeValue("valeur"));
						}
					}
					HashMap<String, String> fichiers = new HashMap<String, String>();
					if(elementTheme.getChild("listeFichiers") != null) {
						Iterator iteratorFichiers = elementTheme.getChild("listeFichiers").getChildren("fichier").iterator();
						while (iteratorFichiers.hasNext()) {
							Element fichier = (Element)iteratorFichiers.next();
							fichiers.put(fichier.getAttributeValue("id"), fichier.getAttributeValue("cible"));
						}
					}
					ModelTheme theme = new ModelTheme(new InfosModelTheme(elementTheme.getAttributeValue("chemin").replace("/", File.separator), parametres, fichiers));
					theme.setId(Integer.parseInt(elementTheme.getAttributeValue("id")));
					
					ModelDiffusion diffusion = new ModelDiffusion(concours, theme, new InfosModelDiffusion(elementDiffusion.getAttributeValue("nom"), Integer.parseInt(elementDiffusion.getAttributeValue("port"))));
					diffusion.setId(Integer.parseInt(elementDiffusion.getAttributeValue("id")));
					concours.addDiffusion(diffusion);
				}
			}
			
			// Ajouter les catégories
			if (root.getChild("listeCategories") != null) {
				// Operations à executer après l'ajout des catégorie
				ArrayList<Runnable> operationsApresCategories = new ArrayList<Runnable>();
				
				// Pour chaque catégorie
				Iterator iteratorCategories = root.getChild("listeCategories").getChildren("categorie").iterator();
				while (iteratorCategories.hasNext()) {
					final Element elementCategorie = (Element)iteratorCategories.next();
					
					final ModelCategorie categorie = new ModelCategorie(concours, new InfosModelCategorie(elementCategorie.getAttributeValue("nom")));
					categorie.setId(Integer.parseInt(elementCategorie.getAttributeValue("id")));
					
					// Ajouter les poules
					if (elementCategorie.getChild("listePoules") != null) {
						Iterator iteratorPoules = elementCategorie.getChild("listePoules").getChildren("poule").iterator();
						while (iteratorPoules.hasNext()) {
							final Element elementPoule = (Element)iteratorPoules.next();
							
							final ModelPoule poule = new ModelPoule(categorie, new InfosModelPoule(elementPoule.getAttributeValue("nom")));
							poule.setId(Integer.parseInt(elementPoule.getAttributeValue("id")));
							
							// Ajouter les équipes
							if (elementPoule.getChild("listeEquipes") != null) {
								Iterator iteratorEquipes = elementPoule.getChild("listeEquipes").getChildren("equipe").iterator();
								while (iteratorEquipes.hasNext()) {
									Element elementEquipe = (Element)iteratorEquipes.next();
									
									ModelEquipe equipe = new ModelEquipe(poule, new InfosModelEquipe(elementEquipe.getAttributeValue("stand"), elementEquipe.getAttributeValue("nom"), elementEquipe.getAttributeValue("ville"), InfosModelEquipe.Statut.search(elementEquipe.getAttributeValue("statut")), elementEquipe.getAttributeValue("membres"), elementEquipe.getAttributeValue("details")));
									equipe.setId(Integer.parseInt(elementEquipe.getAttributeValue("id")));
									
									if (elementEquipe.getChild("listeProprietesPossedees") != null) {
										Iterator iteratorProprietesPossedees = elementEquipe.getChild("listeProprietesPossedees").getChildren("proprietePossedee").iterator();
										while (iteratorProprietesPossedees.hasNext()) {
											Element elementProprietePossedee = (Element)iteratorProprietesPossedees.next();
											ModelPropriete propriete = (ModelPropriete)ModelAbstract.search(Integer.parseInt(elementProprietePossedee.getAttributeValue("refPropriete")));
											ModelProprietePossedee proprietePossedee = new ModelProprietePossedee(propriete, equipe, new InfosModelProprieteEquipe(elementProprietePossedee.getAttributeValue("valeur")));
											proprietePossedee.setId(Integer.parseInt(elementProprietePossedee.getAttributeValue("id")));
											propriete.addProprieteEquipe(proprietePossedee);
											equipe.addProprieteEquipe(proprietePossedee);
										}
									}
									
									if (elementEquipe.getChild("listePrixRemportes") != null) {
										Iterator iteratorPrixRemportes = elementEquipe.getChild("listePrixRemportes").getChildren("prixRemporte").iterator();
										while (iteratorPrixRemportes.hasNext()) {
											Element elementPrixRemporte = (Element)iteratorPrixRemportes.next();
											ModelPrix prix = (ModelPrix)ModelAbstract.search(Integer.parseInt(elementPrixRemporte.getAttributeValue("refPrix")));
											equipe.addPrix(prix);
											prix.addEquipe(equipe);
										}
									}
									
									poule.addEquipe(equipe);
								}
							}
							
							// Ajouter les phases qualificatives de la poule
							if (elementPoule.getChild("listePhasesQualificatives") != null) {
								operationsApresCategories.add(new Runnable() {
									@Override
									public void run () {
										try {
											Iterator iteratorPhasesQualificatives = elementPoule.getChild("listePhasesQualificatives").getChildren("phaseQualificative").iterator();
											while (iteratorPhasesQualificatives.hasNext()) {
												Element elementPhaseQualificative = (Element)iteratorPhasesQualificatives.next();
												
												ModelPhaseQualificative phaseQualificative = new ModelPhaseQualificative(poule, new InfosModelPhaseQualificative());
												phaseQualificative.setId(Integer.parseInt(elementPhaseQualificative.getAttributeValue("id")));
												
												Iterator iteratorMatchsPhaseQualificative = elementPhaseQualificative.getChildren("matchPhaseQualificative").iterator();
												while (iteratorMatchsPhaseQualificative.hasNext()) {
													Element elementMatchPhaseQualificative = (Element)iteratorMatchsPhaseQualificative.next();
													
													String details = elementMatchPhaseQualificative.getAttributeValue("details");
													String timestamp = elementMatchPhaseQualificative.getAttributeValue("timestamp");
													Date date = null;
													if(timestamp != null) {
														date = new Date(Long.parseLong(timestamp)*1000);
													}
													
													ModelMatchPhasesQualifs match = new ModelMatchPhasesQualifs(phaseQualificative, new InfosModelMatchPhasesQualifs(date,details));
													if (elementMatchPhaseQualificative.getAttributeValue("timestamp") != null) {
														match.setDate(new Date(Integer.parseInt(elementMatchPhaseQualificative.getAttributeValue("timestamp")) * 1000));
													}
													match.setId(Integer.parseInt(elementMatchPhaseQualificative.getAttributeValue("id")));
													
													Iterator iteratorParticipations = elementMatchPhaseQualificative.getChildren("participation").iterator();
													
													match.setParticipationA(PersistanceXML.loadParticipation((Element)iteratorParticipations.next(), match));
													match.setParticipationB(PersistanceXML.loadParticipation((Element)iteratorParticipations.next(), match));
													
													phaseQualificative.addMatch(match);
												}
												
												poule.addPhaseQualificative(phaseQualificative);
											}
										} catch (ContestOrgModelException e) {
											// FIXME Faire remonter l'exception
										}
									}
								});
							}
							
							categorie.addPoule(poule);
						}
					}
					
					// Ajouter les phases éliminatoires de la catégorie
					if (elementCategorie.getChild("listePhasesEliminatoires") != null) {
						operationsApresCategories.add(new Runnable() {
							@Override
							public void run () {
								try {
									List listePhasesEliminatoires = elementCategorie.getChild("listePhasesEliminatoires").getChildren("phaseEliminatoire");
									Collections.sort(listePhasesEliminatoires, new Comparator() {
										@Override
										public int compare (Object phaseA, Object phaseB) {
											return new Integer(((Element)phaseA).getAttributeValue("numero")).compareTo(new Integer(((Element)phaseB).getAttributeValue("numero")));
										}
									});
									
									ModelPhasesEliminatoires phasesEliminatoires = new ModelPhasesEliminatoires(categorie, new InfosModelPhaseEliminatoires());
									phasesEliminatoires.setId(Integer.parseInt(elementCategorie.getChild("listePhasesEliminatoires").getAttributeValue("id")));
									
									Iterator iteratorPhasesEliminatoires = listePhasesEliminatoires.iterator();
									while (iteratorPhasesEliminatoires.hasNext()) {
										Element elementPhaseEliminatoireElement = (Element)iteratorPhasesEliminatoires.next();
										
										Iterator iteratorMatchsPhasesEliminatoires = elementPhaseEliminatoireElement.getChildren("matchPhaseEliminatoire").iterator();
										while (iteratorMatchsPhasesEliminatoires.hasNext()) {
											Element elementMatchPhasesEliminatoires = (Element)iteratorMatchsPhasesEliminatoires.next();
											
											ModelMatchPhasesElims matchPrecedantA = elementMatchPhasesEliminatoires.getAttributeValue("refMatchPrecedantA") == null ? null : (ModelMatchPhasesElims)ModelAbstract.search(Integer.parseInt(elementMatchPhasesEliminatoires.getAttributeValue("refMatchPrecedantA")));
											ModelMatchPhasesElims matchPrecedantB = elementMatchPhasesEliminatoires.getAttributeValue("refMatchPrecedantB") == null ? null : (ModelMatchPhasesElims)ModelAbstract.search(Integer.parseInt(elementMatchPhasesEliminatoires.getAttributeValue("refMatchPrecedantB")));
											
											String details = elementMatchPhasesEliminatoires.getAttributeValue("details");
											String timestamp = elementMatchPhasesEliminatoires.getAttributeValue("timestamp");
											Date date = null;
											if(timestamp != null) {
												date = new Date(Long.parseLong(timestamp)*1000);
											}
											
											ModelMatchPhasesElims match = new ModelMatchPhasesElims(phasesEliminatoires, matchPrecedantA, matchPrecedantB, new InfosModelMatchPhasesElims(date,details));
											match.setId(Integer.parseInt(elementMatchPhasesEliminatoires.getAttributeValue("id")));

											if(elementMatchPhasesEliminatoires.getAttributeValue("petiteFinale") == null) {
												if(matchPrecedantA != null) {
													matchPrecedantA.setMatchSuivant(match);
												}
												if(matchPrecedantB != null) {
													matchPrecedantB.setMatchSuivant(match);
												}
											}
											
											Iterator iteratorParticipations = elementMatchPhasesEliminatoires.getChildren("participation").iterator();
											if (iteratorParticipations.hasNext()) {
												match.setParticipationA(PersistanceXML.loadParticipation((Element)iteratorParticipations.next(), match));
											}
											if (iteratorParticipations.hasNext()) {
												match.setParticipationB(PersistanceXML.loadParticipation((Element)iteratorParticipations.next(), match));
											}
											
											if (elementMatchPhasesEliminatoires.getAttributeValue("grandeFinale") != null) {
												phasesEliminatoires.setGrandeFinale(match);
											} else if (elementMatchPhasesEliminatoires.getAttributeValue("petiteFinale") != null) {
												phasesEliminatoires.setPetiteFinale(match);
											}
										}
									}
									
									categorie.setPhasesEliminatoires(phasesEliminatoires);
								} catch (ContestOrgModelException e) {
									// FIXME Faire remonter l'exception
								}
							}
						});
					}
					
					concours.addCategorie(categorie);
				}
				
				// Executer les opérations à executer après l'ajout des catégorie
				for(Runnable operationApresCategories : operationsApresCategories) {
					operationApresCategories.run();
				}
			}
			
			// Retourner le concours
			return concours;
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors du chargement d'un fichier de tournois.",e);
			return null;
		}
	}
	
	// Récupérer une participation à partir de son élément JDom
	@SuppressWarnings("rawtypes")
	public static ModelParticipation loadParticipation (Element elementParticipation, ModelMatchAbstract match) throws ContestOrgModelException {
		ModelEquipe equipe = elementParticipation.getAttribute("refEquipe") == null ? null : (ModelEquipe)ModelAbstract.search(Integer.parseInt(elementParticipation.getAttributeValue("refEquipe")));
		
		int resultat = -1;
		switch (Tools.StringCase(elementParticipation.getAttributeValue("resultat"), "attente", "victoire", "egalite", "defaite", "forfait")) {
			case 0:
				resultat = InfosModelParticipation.RESULTAT_ATTENTE;
				break;
			case 1:
				resultat = InfosModelParticipation.RESULTAT_VICTOIRE;
				break;
			case 2:
				resultat = InfosModelParticipation.RESULTAT_EGALITE;
				break;
			case 3:
				resultat = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
			case 4:
				resultat = InfosModelParticipation.RESULTAT_FORFAIT;
				break;
		}
		
		ModelParticipation participation = new ModelParticipation(equipe, match, new InfosModelParticipation(resultat));
		participation.setId(Integer.parseInt(elementParticipation.getAttributeValue("id")));
		
		Iterator iteratorObjectifsRemplis = elementParticipation.getChildren("objectifRempli").iterator();
		while (iteratorObjectifsRemplis.hasNext()) {
			Element elementObjectifRempli = (Element)iteratorObjectifsRemplis.next();
			ModelObjectif objectif = (ModelObjectif)ModelAbstract.search(Integer.parseInt(elementObjectifRempli.getAttributeValue("refObjectif")));
			ModelObjectifRemporte objectifRempli = new ModelObjectifRemporte(participation, objectif, new InfosModelParticipationObjectif(Integer.parseInt(elementObjectifRempli.getAttributeValue("quantite"))));
			objectifRempli.setId(Integer.parseInt(elementObjectifRempli.getAttributeValue("id")));
			objectif.addObjectifRemporte(objectifRempli);
			participation.addObjectifRemporte(objectifRempli);
		}
		
		if (equipe != null)
			equipe.addParticipation(participation);
		return participation;
	}
	
	// Implémentation de save
	@Override
	public boolean save () {
		if (!XMLHelper.save(PersistanceXML.getConcoursDocument(), new File(this.chemin))) {
			Log.getLogger().error("Erreur lors de la sauvegarde XML du concours.");
			return false;
		} else {
			return true;
		}
	}
	
	// Créer le document JDom du concours
	@SuppressWarnings("unchecked")
	public static Document getConcoursDocument () {
		// Récupérer le concours
		ModelConcours concours = FrontModel.get().getConcours();
		if (concours == null) {
			return null;
		}
		
		// Créer l'élément root
		Element root = new Element("concours");
		
		// Initialiser le document
		Document document = new Document(root);
		
		// Ajouter les informations du concours
		root.setAttribute("id", String.valueOf(concours.getId()));
		root.setAttribute("nom", concours.getConcoursNom());
		if (concours.getConcoursSite() != null && !concours.getConcoursSite().isEmpty())
			root.setAttribute("site", concours.getConcoursSite());
		if (concours.getConcoursLieu() != null && !concours.getConcoursLieu().isEmpty())
			root.setAttribute("lieu", concours.getConcoursLieu());
		if (concours.getConcoursEmail() != null && !concours.getConcoursEmail().isEmpty())
			root.setAttribute("email", concours.getConcoursEmail());
		if (concours.getConcoursTelephone() != null && !concours.getConcoursTelephone().isEmpty())
			root.setAttribute("telephone", concours.getConcoursTelephone());
		if (concours.getConcoursDescription() != null && !concours.getConcoursDescription().isEmpty())
			root.setAttribute("description", concours.getConcoursDescription());
		root.setAttribute("participants", concours.getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "equipes" : "joueurs");
		root.setAttribute("qualifications", concours.getTypeQualifications() == InfosModelConcours.QUALIFICATIONS_PHASES ? "phases" : "grille");
		
		// Ajouter les informations de l'organisateur
		if (concours.getOrganisateurNom() != null && !concours.getOrganisateurNom().isEmpty()) {
			root.addContent(new Comment("Informations de l'organisateur du concours"));
			Element elementOrganisme = new Element("organisateur");
			elementOrganisme.setAttribute("nom", concours.getOrganisateurNom());
			if (concours.getOrganisateurSite() != null && !concours.getOrganisateurSite().isEmpty())
				elementOrganisme.setAttribute("site", concours.getOrganisateurSite());
			if (concours.getOrganisateurLieu() != null && !concours.getOrganisateurLieu().isEmpty())
				elementOrganisme.setAttribute("lieu", concours.getOrganisateurLieu());
			if (concours.getOrganisateurEmail() != null && !concours.getOrganisateurEmail().isEmpty())
				elementOrganisme.setAttribute("email", concours.getOrganisateurEmail());
			if (concours.getOrganisateurTelephone() != null && !concours.getOrganisateurTelephone().isEmpty())
				elementOrganisme.setAttribute("telephone", concours.getOrganisateurTelephone());
			if (concours.getOrganisateurDescription() != null && !concours.getOrganisateurDescription().isEmpty())
				elementOrganisme.setAttribute("description", concours.getOrganisateurDescription());
			root.addContent(elementOrganisme);
		}
		
		// Ajouter les informations de points
		root.addContent(new Comment("Liste des points remportés en fonction du résultat des matchs"));
		Element elementPoints = new Element("points");
		elementPoints.setAttribute("victoire", String.valueOf(concours.getPointsVictoire()));
		elementPoints.setAttribute("egalite", String.valueOf(concours.getPointsEgalite()));
		elementPoints.setAttribute("defaite", String.valueOf(concours.getPointsDefaite()));
		root.addContent(elementPoints);
		
		// Ajouter les informations de programmation
		if (concours.getProgrammationDuree() != null || concours.getProgrammationInterval() != null || concours.getProgrammationPause() != null) {
			Element elementProgrammation = new Element("programmation");
			if (concours.getProgrammationDuree() != null)
				elementProgrammation.setAttribute("duree", String.valueOf(concours.getProgrammationDuree()));
			if (concours.getProgrammationInterval() != null)
				elementProgrammation.setAttribute("interval", String.valueOf(concours.getProgrammationInterval()));
			if (concours.getProgrammationPause() != null)
				elementProgrammation.setAttribute("pause", String.valueOf(concours.getProgrammationPause()));
			root.addContent(elementProgrammation);
		}
		
		// Ajouter les critères de classement
		root.addContent(new Comment("Liste des critère de classement"));
		Element listeCriteres = new Element("listeCriteres");
		for (ModelCompPhasesQualifsAbstract comparateur : concours.getCompsPhasesQualifs()) {
			Element elementCritere = new Element("critere");
			elementCritere.setAttribute("id", String.valueOf(comparateur.getId()));
			if (comparateur instanceof ModelCompPhasesQualifsPoints) {
				elementCritere.addContent(new Element("critereNbPoints"));
			} else if (comparateur instanceof ModelCompPhasesQualifsVictoires) {
				elementCritere.addContent(new Element("critereNbVictoires"));
			} else if (comparateur instanceof ModelCompPhasesQualifsObjectif) {
				Element elementCritereNbObjectifs = new Element("critereNbObjectifs");
				elementCritereNbObjectifs.setAttribute("refObjectif", String.valueOf(((ModelCompPhasesQualifsObjectif)comparateur).getObjectif().getId()));
				elementCritere.addContent(elementCritereNbObjectifs);
			}
			listeCriteres.addContent(elementCritere);
		}
		root.addContent(listeCriteres);
		
		// Ajouter les lieux, horaires et emplacements
		if (concours.getLieux().size() != 0) {
			root.addContent(new Comment("Liste des lieux ou peuvent se dérouler les matchs"));
			Element listeLieux = new Element("listeLieux");
			for (ModelLieu lieu : concours.getLieux()) {
				Element elementLieu = new Element("lieu");
				elementLieu.setAttribute("id", String.valueOf(lieu.getId()));
				elementLieu.setAttribute("nom", lieu.getNom());
				if (lieu.getLieu() != null && !lieu.getLieu().isEmpty())
					elementLieu.setAttribute("lieu", lieu.getLieu());
				if (lieu.getTelephone() != null && !lieu.getTelephone().isEmpty())
					elementLieu.setAttribute("telephone", lieu.getTelephone());
				if (lieu.getEmail() != null && !lieu.getEmail().isEmpty())
					elementLieu.setAttribute("email", lieu.getEmail());
				if (lieu.getDescription() != null && !lieu.getDescription().isEmpty())
					elementLieu.setAttribute("description", lieu.getDescription());
				if (lieu.getHoraires().size() != 0) {
					Element listeHoraires = new Element("listeHoraires");
					for (ModelHoraire horaire : lieu.getHoraires()) {
						Element elementHoraire = new Element("horaire");
						elementHoraire.setAttribute("id", String.valueOf(horaire.getId()));
						elementHoraire.setAttribute("debut", String.valueOf(horaire.getDebut()));
						elementHoraire.setAttribute("fin", String.valueOf(horaire.getFin()));
						elementHoraire.setAttribute("jours", String.valueOf(horaire.getJours()));
						listeHoraires.addContent(elementHoraire);
					}
					elementLieu.addContent(listeHoraires);
				}
				Element listeEmplacements = new Element("listeEmplacements");
				for (ModelEmplacement emplacement : lieu.getEmplacements()) {
					Element elementEmplacement = new Element("emplacement");
					elementEmplacement.setAttribute("id", String.valueOf(emplacement.getId()));
					elementEmplacement.setAttribute("nom", emplacement.getNom());
					if (emplacement.getDescription() != null && !emplacement.getDescription().isEmpty())
						elementEmplacement.setAttribute("description", emplacement.getDescription());
					listeEmplacements.addContent(elementEmplacement);
				}
				elementLieu.addContent(listeEmplacements);
				listeLieux.addContent(elementLieu);
			}
			root.addContent(listeLieux);
		}
		
		// Ajouter les objectifs
		if (concours.getObjectifs().size() != 0) {
			root.addContent(new Comment("Liste des objectifs à remporter au cours matchs"));
			Element listeObjectifs = new Element("listeObjectifs");
			for (ModelObjectif objectif : concours.getObjectifs()) {
				Element elementObjectif = new Element("objectif");
				elementObjectif.setAttribute("id", String.valueOf(objectif.getId()));
				InfosModelObjectif infos = objectif.toInformation();
				if (infos instanceof InfosModelObjectifPoints) {
					Element elementObjectifPoints = new Element("objectifPoints");
					elementObjectifPoints.setAttribute("nom", infos.getNom());
					elementObjectifPoints.setAttribute("points", String.valueOf(((InfosModelObjectifPoints)infos).getPoints()));
					if (((InfosModelObjectifPoints)infos).getBorneParticipation() != null)
						elementObjectifPoints.setAttribute("borneParticipation", String.valueOf(((InfosModelObjectifPoints)infos).getBorneParticipation()));
					elementObjectif.addContent(elementObjectifPoints);
				} else if (infos instanceof InfosModelObjectifPourcentage) {
					Element elementObjectifPourcentage = new Element("objectifPourcentage");
					elementObjectifPourcentage.setAttribute("nom", infos.getNom());
					elementObjectifPourcentage.setAttribute("pourcentage", String.valueOf(((InfosModelObjectifPourcentage)infos).getPourcentage()));
					if (((InfosModelObjectifPourcentage)infos).getBorneAugmentation() != null)
						elementObjectifPourcentage.setAttribute("borneAugmentation", String.valueOf(((InfosModelObjectifPourcentage)infos).getBorneAugmentation()));
					if (((InfosModelObjectifPourcentage)infos).getBorneParticipation() != null)
						elementObjectifPourcentage.setAttribute("borneParticipation", String.valueOf(((InfosModelObjectifPourcentage)infos).getBorneParticipation()));
					elementObjectif.addContent(elementObjectifPourcentage);
				} else if (infos instanceof InfosModelObjectifNul) {
					Element elementObjectifNul = new Element("objectifNul");
					elementObjectifNul.setAttribute("nom", infos.getNom());
					elementObjectif.addContent(elementObjectifNul);
				}
				listeObjectifs.addContent(elementObjectif);
			}
			root.addContent(listeObjectifs);
		}
		
		// Ajouter les prix
		if (concours.getPrix().size() != 0) {
			root.addContent(new Comment("Liste des prix à remporter pendant le concours"));
			Element listePrix = new Element("listePrix");
			for (ModelPrix prix : concours.getPrix()) {
				Element elementPrix = new Element("prix");
				elementPrix.setAttribute("id", String.valueOf(prix.getId()));
				elementPrix.setAttribute("nom", prix.getNom());
				listePrix.addContent(elementPrix);
			}
			root.addContent(listePrix);
		}
		
		// Ajouter les propriétés d'équipe
		if (concours.getProprietes().size() != 0) {
			root.addContent(new Comment("Liste des propriétés d'équipe"));
			Element listeProprietes = new Element("listeProprietes");
			for (ModelPropriete propriete : concours.getProprietes()) {
				Element elementPropriete = new Element("propriete");
				elementPropriete.setAttribute("id", String.valueOf(propriete.getId()));
				elementPropriete.setAttribute("nom", propriete.getNom());
				String type = null;
				switch (propriete.getType()) {
					case InfosModelPropriete.TYPE_INT:
						type = "entier";
						break;
					case InfosModelPropriete.TYPE_FLOAT:
						type = "decimal";
						break;
					case InfosModelPropriete.TYPE_STRING:
						type = "texte";
						break;
				}
				elementPropriete.setAttribute("type", type);
				elementPropriete.setAttribute("obligatoire", propriete.isObligatoire() ? "oui" : "non");
				listeProprietes.addContent(elementPropriete);
			}
			root.addContent(listeProprietes);
		}
		
		// Ajouter les exportations
		if (concours.getExportations().size() != 0) {
			root.addContent(new Comment("Liste des exportations"));
			Element listeExportations = new Element("listeExportations");
			for (ModelExportation exportation : concours.getExportations()) {
				Element elementExportation = new Element("exportation");
				elementExportation.setAttribute("id", String.valueOf(exportation.getId()));
				elementExportation.setAttribute("nom", exportation.getNom());
				elementExportation.setAttribute("automatique", exportation.isAutomatique() ? "oui" : "non");
				
				Element elementChemin = new Element("chemin");
				elementChemin.setAttribute("id", String.valueOf(exportation.getChemin().getId()));
				if (exportation.getChemin() instanceof ModelCheminLocal) {
					Element elementCheminLocal = new Element("cheminLocal");
					elementCheminLocal.setAttribute("chemin", ((ModelCheminLocal)exportation.getChemin()).getChemin());
					elementChemin.addContent(elementCheminLocal);
				} else if (exportation.getChemin() instanceof ModelCheminFTP) {
					Element elementCheminFTP = new Element("cheminFTP");
					elementCheminFTP.setAttribute("host", ((ModelCheminFTP)exportation.getChemin()).getHost());
					elementCheminFTP.setAttribute("username", ((ModelCheminFTP)exportation.getChemin()).getUsername());
					elementCheminFTP.setAttribute("password", ((ModelCheminFTP)exportation.getChemin()).getPassword());
					elementCheminFTP.setAttribute("port", String.valueOf(((ModelCheminFTP)exportation.getChemin()).getPort()));
					elementCheminFTP.setAttribute("mode", ((ModelCheminFTP)exportation.getChemin()).getMode() == InfosConnexionFTP.MODE_ACTIF ? "actif" : "passif");
					elementCheminFTP.setAttribute("path", ((ModelCheminFTP)exportation.getChemin()).getPath());
					elementChemin.addContent(elementCheminFTP);
				}
				elementExportation.addContent(elementChemin);
				
				Element elementTheme = new Element("theme");
				elementTheme.setAttribute("id", String.valueOf(exportation.getTheme().getId()));
				elementTheme.setAttribute("chemin", exportation.getTheme().getChemin().replace(File.separator, "/"));

				if(exportation.getTheme().getParametres() != null && exportation.getTheme().getParametres().size() != 0) {
					Element elementListeParametres = new Element("listeParametres");
					for (Entry<String, String> parametre : exportation.getTheme().getParametres().entrySet()) {
						Element elementParametre = new Element("parametre");
						elementParametre.setAttribute("id", parametre.getKey());
						elementParametre.setAttribute("valeur", parametre.getValue() == null ? "" : parametre.getValue());
						elementListeParametres.addContent(elementParametre);
					}
					elementTheme.addContent(elementListeParametres);
				}
				if(exportation.getTheme().getFichiers() != null && exportation.getTheme().getFichiers().size() != 0) {
					Element elementListeFichiers = new Element("listeFichiers");
					for (Entry<String, String> fichier : exportation.getTheme().getFichiers().entrySet()) {
						Element elementFichier = new Element("fichier");
						elementFichier.setAttribute("id", fichier.getKey());
						elementFichier.setAttribute("cible", fichier.getValue() == null ? "" : fichier.getValue());
						elementListeFichiers.addContent(elementFichier);
					}
					elementTheme.addContent(elementListeFichiers);
				}
				
				elementExportation.addContent(elementTheme);
				
				listeExportations.addContent(elementExportation);
			}
			root.addContent(listeExportations);
		}
		
		// Ajouter la publication
		if (concours.getPublication() != null) {
			root.addContent(new Comment("Exportation par défaut pour la publication"));
			Element elementPublication = new Element("publication");
			elementPublication.setAttribute("refExportation", String.valueOf(concours.getPublication().getId()));
			root.addContent(elementPublication);
		}
		
		// Ajouter les diffusions
		if (concours.getDiffusions().size() != 0) {
			root.addContent(new Comment("Liste des diffusions"));
			Element listeDiffusions = new Element("listeDiffusions");
			try {
				listeDiffusions.setAttribute("hote", InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
			}
			for (ModelDiffusion diffusion : concours.getDiffusions()) {
				Element elementDiffusion = new Element("diffusion");
				elementDiffusion.setAttribute("id", String.valueOf(diffusion.getId()));
				elementDiffusion.setAttribute("nom", diffusion.getNom());
				elementDiffusion.setAttribute("port", String.valueOf(diffusion.getPort()));
				
				Element elementTheme = new Element("theme");
				elementTheme.setAttribute("id", String.valueOf(diffusion.getTheme().getId()));
				elementTheme.setAttribute("chemin", diffusion.getTheme().getChemin().replace(File.separator, "/"));

				if(diffusion.getTheme().getParametres() != null && diffusion.getTheme().getParametres().size() != 0) {
					Element elementListeParametres = new Element("listeParametres");
					for (Entry<String, String> parametre : diffusion.getTheme().getParametres().entrySet()) {
						Element elementParametre = new Element("parametre");
						elementParametre.setAttribute("id", parametre.getKey());
						elementParametre.setAttribute("valeur", parametre.getValue() == null ? "" : parametre.getValue());
						elementListeParametres.addContent(elementParametre);
					}
					elementTheme.addContent(elementListeParametres);
				}
				if(diffusion.getTheme().getFichiers() != null && diffusion.getTheme().getFichiers().size() != 0) {
					Element elementListeFichiers = new Element("listeFichiers");
					for (Entry<String, String> fichier : diffusion.getTheme().getFichiers().entrySet()) {
						Element elementFichier = new Element("fichier");
						elementFichier.setAttribute("id", fichier.getKey());
						elementFichier.setAttribute("cible", fichier.getValue() == null ? "" : fichier.getValue());
						elementListeFichiers.addContent(elementFichier);
					}
					elementTheme.addContent(elementListeFichiers);
				}
				
				elementDiffusion.addContent(elementTheme);
				
				listeDiffusions.addContent(elementDiffusion);
			}
			root.addContent(listeDiffusions);
		}
		
		// Récupérer le comparateur des phases qualificatives
		CompPhasesQualifs compPhasesQualifs = concours.getComparateurPhasesQualificatives();
		
		// Récupérer le comparateur des phases éliminatoires
		CompPhasesElims compPhasesElims = new CompPhasesElims();
		
		// Ajouter les catégories
		if (concours.getCategories().size() != 0) {
			root.addContent(new Comment("Liste des catégories"));
			Element listeCategories = new Element("listeCategories");
			for (ModelCategorie categorie : concours.getCategories()) {
				Element elementCategorie = new Element("categorie");
				elementCategorie.setAttribute("id", String.valueOf(categorie.getId()));
				elementCategorie.setAttribute("nom", categorie.getNom());
				
				// Récupérer les équipes de la catégorie
				ArrayList<ModelEquipe> equipesCategorie = categorie.getEquipes();
				
				// Ajouter le classement des phases qualificatives pour la catégorie
				if (equipesCategorie.size() != 0) {
					// Trier les équipes
					Collections.sort(equipesCategorie, compPhasesQualifs);
					Collections.reverse(equipesCategorie);
					Element elementClassement = new Element("classementCategoriePhasesQualificatives");
					Element elementlisteClassementEquipes = new Element("listeClassementEquipes");
					for (ModelEquipe equipe : equipesCategorie) {
						Element elementEquipe = new Element("classementEquipe");
						elementEquipe.setAttribute("refEquipe", String.valueOf(equipe.getId()));
						elementEquipe.setAttribute("rang", String.valueOf(equipe.getRangPhasesQualifs()));
						elementlisteClassementEquipes.addContent(elementEquipe);
					}
					elementClassement.addContent(elementlisteClassementEquipes);
					elementCategorie.addContent(elementClassement);
				}
				
				// Ajouter les poules
				if (categorie.getPoules().size() != 0) {
					Element listePoules = new Element("listePoules");
					for (ModelPoule poule : categorie.getPoules()) {
						Element elementPoule = new Element("poule");
						elementPoule.setAttribute("id", String.valueOf(poule.getId()));
						elementPoule.setAttribute("nom", poule.getNom());
						
						// Ajouter les équipes
						if (poule.getEquipes().size() != 0) {
							Element listeEquipes = new Element("listeEquipes");
							for (ModelEquipe equipe : poule.getEquipes()) {
								Element elementEquipe = new Element("equipe");
								elementEquipe.setAttribute("id", String.valueOf(equipe.getId()));
								elementEquipe.setAttribute("nom", equipe.getNom());
								if (equipe.getVille() != null && !equipe.getVille().isEmpty())
									elementEquipe.setAttribute("ville", equipe.getVille());
								elementEquipe.setAttribute("statut", equipe.getStatut().getId());
								if (equipe.getStand() != null && !equipe.getStand().isEmpty())
									elementEquipe.setAttribute("stand", equipe.getStand());
								if (equipe.getMembres() != null && !equipe.getMembres().isEmpty())
									elementEquipe.setAttribute("membres", equipe.getMembres());
								if (equipe.getDetails() != null && !equipe.getDetails().isEmpty())
									elementEquipe.setAttribute("details", equipe.getDetails());
								elementEquipe.setAttribute("rangPhasesQualifs", String.valueOf(equipe.getRangPhasesQualifs()));
								elementEquipe.setAttribute("pointsPhasesQualifs", String.valueOf(String.valueOf(equipe.getPoints(false, true))));
								elementEquipe.setAttribute("rangPhasesElims", String.valueOf(equipe.getRangPhasesElims()));
								elementEquipe.setAttribute("pointsPhasesElims", String.valueOf(equipe.getPoints(true, false)));
								
								if (equipe.getProprietesEquipe().size() != 0) {
									Element listeProprietesPossedees = new Element("listeProprietesPossedees");
									for (ModelProprietePossedee proprieteEquipe : equipe.getProprietesEquipe()) {
										Element elementProprietePossedee = new Element("proprietePossedee");
										elementProprietePossedee.setAttribute("id", String.valueOf(proprieteEquipe.getId()));
										elementProprietePossedee.setAttribute("refPropriete", String.valueOf(proprieteEquipe.getPropriete().getId()));
										elementProprietePossedee.setAttribute("valeur", proprieteEquipe.getValue());
										listeProprietesPossedees.addContent(elementProprietePossedee);
									}
									elementEquipe.addContent(listeProprietesPossedees);
								}
								
								if (equipe.getPrix().size() != 0) {
									Element listePrixRemportes = new Element("listePrixRemportes");
									for (ModelPrix prix : equipe.getPrix()) {
										Element elementPrixRemporte = new Element("prixRemporte");
										elementPrixRemporte.setAttribute("refPrix", String.valueOf(prix.getId()));
										listePrixRemportes.addContent(elementPrixRemporte);
									}
									elementEquipe.addContent(listePrixRemportes);
								}
								
								listeEquipes.addContent(elementEquipe);
							}
							elementPoule.addContent(listeEquipes);
						}
						
						// Récupérer les équipes de la poule
						ArrayList<ModelEquipe> equipesPoule = poule.getEquipes();
						
						// Ajouter le classement des phases qualificatives pour la poule
						if (equipesPoule.size() != 0) {
							// Trier les équipes
							Collections.sort(equipesPoule, compPhasesQualifs);
							Collections.reverse(equipesPoule);
							Element elementClassement = new Element("classementPoulePhasesQualificatives");
							Element elementlisteClassementEquipes = new Element("listeClassementEquipes");
							for (ModelEquipe equipe : equipesPoule) {
								Element elementEquipe = new Element("classementEquipe");
								elementEquipe.setAttribute("refEquipe", String.valueOf(equipe.getId()));
								elementEquipe.setAttribute("rang", String.valueOf(equipe.getRangPhasesQualifs()));
								elementlisteClassementEquipes.addContent(elementEquipe);
							}
							elementClassement.addContent(elementlisteClassementEquipes);
							elementPoule.addContent(elementClassement);
						}
						
						// Ajouter les phases qualificatives
						if (poule.getPhasesQualificatives().size() != 0) {
							Element listePhasesQualificatives = new Element("listePhasesQualificatives");
							for (ModelPhaseQualificative phase : poule.getPhasesQualificatives()) {
								Element elementPhaseQualificative = new Element("phaseQualificative");
								elementPhaseQualificative.setAttribute("id", String.valueOf(phase.getId()));
								elementPhaseQualificative.setAttribute("numero", String.valueOf(poule.getPhasesQualificatives().indexOf(phase)));
								
								// Récupérer le comparateur pour la phase qualificative
								CompPhasesQualifs compPhaseQualif = concours.getComparateurPhasesQualificatives(phase.getNumero());
								
								// Ajouter le classement pour la phase qualificative
								if (equipesPoule.size() != 0) {
									// Trier les équipes
									Collections.sort(equipesPoule, compPhaseQualif);
									Collections.reverse(equipesPoule);
									Element elementClassement = new Element("classementPhaseQualificative");
									Element elementlisteClassementEquipes = new Element("listeClassementEquipes");
									for (ModelEquipe equipe : equipesPoule) {
										Element elementEquipe = new Element("classementEquipe");
										elementEquipe.setAttribute("refEquipe", String.valueOf(equipe.getId()));
										elementEquipe.setAttribute("rang", String.valueOf(equipe.getRangPhasesQualifs(phase.getNumero())));
										elementlisteClassementEquipes.addContent(elementEquipe);
									}
									elementClassement.addContent(elementlisteClassementEquipes);
									elementPhaseQualificative.addContent(elementClassement);
								}
								
								for (ModelMatchPhasesQualifs match : phase.getMatchs()) {
									Element elementMatchPhaseQualificative = new Element("matchPhaseQualificative");
									elementMatchPhaseQualificative.setAttribute("id", String.valueOf(match.getId()));
									elementMatchPhaseQualificative.setAttribute("details", match.getDetails() == null ? "" : match.getDetails());
									if (match.getDate() != null)
										elementMatchPhaseQualificative.setAttribute("timestamp", String.valueOf(match.getDate().getTime() / 1000));
										
									
									elementMatchPhaseQualificative.addContent(PersistanceXML.getElementParticipation(match.getParticipationA()));
									elementMatchPhaseQualificative.addContent(PersistanceXML.getElementParticipation(match.getParticipationB()));
									
									elementPhaseQualificative.addContent(elementMatchPhaseQualificative);
								}
								
								listePhasesQualificatives.addContent(elementPhaseQualificative);
							}
							elementPoule.addContent(listePhasesQualificatives);
						}
						
						listePoules.addContent(elementPoule);
					}
					elementCategorie.addContent(listePoules);
				}
				
				// Ajouter le classement des phases éliminatoires
				if (equipesCategorie.size() != 0) {
					// Trier les équipes
					Collections.sort(equipesCategorie, compPhasesElims);
					Element elementClassement = new Element("classementCategoriePhasesEliminatoires");
					Element elementlisteClassementEquipes = new Element("listeClassementEquipes");
					for (ModelEquipe equipe : equipesCategorie) {
						int rang = equipe.getRangPhasesElims();
						if(rang > 0) {
							Element elementEquipe = new Element("classementEquipe");
							elementEquipe.setAttribute("refEquipe", String.valueOf(equipe.getId()));
							elementEquipe.setAttribute("rang", String.valueOf(rang));
							elementlisteClassementEquipes.addContent(elementEquipe);
						}
					}
					elementClassement.addContent(elementlisteClassementEquipes);
					elementCategorie.addContent(elementClassement);
				}
				
				// Ajouter les phases éliminatoires
				if (categorie.getPhasesEliminatoires() != null) {
					Element listePhasesEliminatoires = new Element("listePhasesEliminatoires");
					listePhasesEliminatoires.setAttribute("id", String.valueOf(categorie.getPhasesEliminatoires().getId()));
					int nbPhases = categorie.getPhasesEliminatoires().getNbPhases();
					for (int i = 0; i < nbPhases; i++) {
						Element elementPhaseEliminatoire = new Element("phaseEliminatoire");
						elementPhaseEliminatoire.setAttribute("numero", String.valueOf(i));
						
						ArrayList<ModelMatchPhasesElims> matchs = categorie.getPhasesEliminatoires().getMatchs(i);
						if (i == nbPhases - 1) {
							matchs.add(categorie.getPhasesEliminatoires().getPetiteFinale());
						}
						for (ModelMatchPhasesElims match : matchs) {
							Element elementMatchPhaseEliminatoire = new Element("matchPhaseEliminatoire");
							elementMatchPhaseEliminatoire.setAttribute("id", String.valueOf(match.getId()));
							elementMatchPhaseEliminatoire.setAttribute("details", match.getDetails() == null ? "" : match.getDetails());
							if (match.isGrandeFinale()) {
								elementMatchPhaseEliminatoire.setAttribute("grandeFinale", "oui");
							}
							if(match.isPetiteFinale()) {
								elementMatchPhaseEliminatoire.setAttribute("petiteFinale", "oui");
							}
							if (match.getDate() != null)
								elementMatchPhaseEliminatoire.setAttribute("timestamp", String.valueOf(match.getDate().getTime() / 1000));
							if (match.getMatchPrecedantA() != null)
								elementMatchPhaseEliminatoire.setAttribute("refMatchPrecedantA", String.valueOf(match.getMatchPrecedantA().getId()));
							if (match.getMatchPrecedantB() != null)
								elementMatchPhaseEliminatoire.setAttribute("refMatchPrecedantB", String.valueOf(match.getMatchPrecedantB().getId()));
							elementMatchPhaseEliminatoire.setAttribute("id", String.valueOf(match.getId()));
							
							if (match.getParticipationA() != null) {
								elementMatchPhaseEliminatoire.addContent(PersistanceXML.getElementParticipation(match.getParticipationA()));
							}
							if (match.getParticipationB() != null) {
								elementMatchPhaseEliminatoire.addContent(PersistanceXML.getElementParticipation(match.getParticipationB()));
							}
							
							elementPhaseEliminatoire.addContent(elementMatchPhaseEliminatoire);
						}
						
						listePhasesEliminatoires.addContent(elementPhaseEliminatoire);
					}
					elementCategorie.addContent(listePhasesEliminatoires);
				}
				
				listeCategories.addContent(elementCategorie);
			}
			root.addContent(listeCategories);
		}
		
		// Retourner le document
		return document;
	}
	
	// Créer l'élément JDom d'une participation
	private static Element getElementParticipation (ModelParticipation participation) {
		Element elementParticipation = new Element("participation");
		elementParticipation.setAttribute("id", String.valueOf(participation.getId()));
		if (participation.getEquipe() != null)
			elementParticipation.setAttribute("refEquipe", String.valueOf(participation.getEquipe().getId()));
		String resultat = null;
		switch (participation.getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				resultat = "attente";
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				resultat = "victoire";
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				resultat = "egalite";
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				resultat = "defaite";
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				resultat = "forfait";
				break;
		}
		elementParticipation.setAttribute("resultat", resultat);
		elementParticipation.setAttribute("points", String.valueOf(participation.getPoints()));
		for (ModelObjectifRemporte objectifRemporte : participation.getObjectifsRemportes()) {
			Element elementObjectifRempli = new Element("objectifRempli");
			elementObjectifRempli.setAttribute("id", String.valueOf(objectifRemporte.getId()));
			elementObjectifRempli.setAttribute("refObjectif", String.valueOf(objectifRemporte.getObjectif().getId()));
			elementObjectifRempli.setAttribute("quantite", String.valueOf(objectifRemporte.getQuantite()));
			elementParticipation.addContent(elementObjectifRempli);
		}
		return elementParticipation;
	}
	
	// Extraire les information d'équipe d'un fichier XML
	@SuppressWarnings("rawtypes")
	public static ArrayList<InfosModelEquipe> loadEquipes (String chemin) {
		try {
			// Construire le document JDom à partir du fichier
			Document document = new SAXBuilder().build(new File(chemin));
			
			// Récupérer l'élément root
			Element root = document.getRootElement();
			
			// Initialiser la liste des équipes
			ArrayList<InfosModelEquipe> equipes = new ArrayList<InfosModelEquipe>();
			
			// Récupérer la liste des équipes
			Iterator iteratorEquipes = root.getChildren("equipe").iterator();
			while(iteratorEquipes.hasNext()) {
				// Récupérer l'équipe courante
				Element elementEquipe = (Element)iteratorEquipes.next();
				
				// Récupérer les propriétés de l'équipe
				String stand = elementEquipe.getAttributeValue("stand");
				if(stand != null)
					stand = stand.trim();
				String nom = elementEquipe.getAttributeValue("nom");
				if(nom != null)
					nom = nom.trim();
				String ville = elementEquipe.getAttributeValue("ville");
				if(ville != null)
					ville = ville.trim();
				Statut statut = InfosModelEquipe.Statut.search(elementEquipe.getAttributeValue("statut"));
				String membres = elementEquipe.getAttributeValue("membres");
				if(membres != null)
					membres = membres.trim();
				String details = elementEquipe.getAttributeValue("details");
				if(details != null)
					details = details.trim();
				
				// Ajouter l'équipe dans la liste des équipes
				if(!nom.isEmpty()) {
					equipes.add(new InfosModelEquipe(stand, nom, ville, statut, membres, details));
				}
			}
			
			// Retourner la liste des équipes
			return equipes;
		} catch (JDOMException e) {
			Log.getLogger().error("Erreur lors de l'importation des équipes",e);
			return null;
		} catch (IOException e) {
			Log.getLogger().error("Erreur lors de l'importation des équipes",e);
			return null;
		}
	}
}
