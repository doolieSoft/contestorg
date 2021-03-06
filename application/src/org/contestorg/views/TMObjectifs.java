package org.contestorg.views;

import java.awt.Window;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.TableModel;

import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelObjectifNul;
import org.contestorg.infos.InfosModelObjectifPoints;
import org.contestorg.infos.InfosModelObjectifPourcentage;

/**
 * Modèle de données pour un tableau d'objectifs
 */
public class TMObjectifs extends TMAbstract<InfosModelObjectif>
{
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public TMObjectifs(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}

	/**
	 * @see TMAbstract#getAddWindow()
	 */
	public Window getAddWindow () {
		// Créer et retourner la fentre de création
		return new JDObjectifCreer(this.w_parent, this);
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	public Window getUpdateWindow (InfosModelObjectif infos) {
		// Créer et retourner la fenêtre de création
		return new JDObjectifEditer(this.w_parent, this, infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
	public boolean acceptDelete (InfosModelObjectif infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer l'objectif \"" + infos.getNom() + "\" ?")) {
			return true;
		} else {
			return false;
		}
	}

	// Implémentation manquante de TableModel
	
	/**
	 * @see TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	
	/**
	 * @see TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount () {
		return 2;
	}
	
	/**
	 * @see TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName (int column) {
		switch (column) {
			case 0:
				return "Nom";
			case 1:
				return "Valeur";
		}
		return null;
	}
	
	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt (int row, int column) {
		// Récupérer les informations de l'objectif
		InfosModelObjectif infos = this.get(row);

		// Retourner l'information demandée
		switch (column) {
			case 0:
				return infos.getNom();
			case 1:
				// Quel type d'objectif ?
				if (infos instanceof InfosModelObjectifNul) {
					return '-';
				}
				if (infos instanceof InfosModelObjectifPoints) {
					return ((InfosModelObjectifPoints)infos).getPoints() + " pts";
				}
				if (infos instanceof InfosModelObjectifPourcentage) {
					return ((InfosModelObjectifPourcentage)infos).getPourcentage() + " %";
				}
		}

		// Retourner null si pas d'information correspondante
		return null;
	}
	
	/**
	 * @see TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable (int row, int column) {
		// Vérifier s'il ne s'agit pas d'un T nul
		if (this.get(row) instanceof InfosModelObjectifNul && column == 1) {
			return false;
		}

		// Retourner true dans les autres cas
		return true;
	}
	
	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt (Object object, int row, int column) {
		// Caster l'objet en string
		String string = (String)object;

		// Récupérer les informations de l'objectif
		InfosModelObjectif infos = this.get(row);

		// Quel colonne ?
		switch (column) {
			case 0: // Nom
				// Quel type d'objectif ?
				if (infos instanceof InfosModelObjectifPourcentage) {
					this.update(row, new InfosModelObjectifPourcentage(string, ((InfosModelObjectifPourcentage)infos).getPourcentage(), ((InfosModelObjectifPourcentage)infos).getBorneParticipation(), ((InfosModelObjectifPourcentage)infos).getBorneAugmentation()));
				} else if (infos instanceof InfosModelObjectifNul) {
					this.update(row, new InfosModelObjectifNul(string));
				} else if (infos instanceof InfosModelObjectifPoints) {
					this.update(row, new InfosModelObjectifPoints(string, ((InfosModelObjectifPoints)infos).getPoints(), ((InfosModelObjectifPoints)infos).getBorneParticipation()));
				}
				break;
			case 1: // Valeur
				// Vérifier si le format est correct
				Pattern pattern = Pattern.compile("^\\s*([0-9]+(\\.[[0-9]+])?)");
				Matcher matcher = pattern.matcher(string);
				if (matcher.find()) {
					// Parser la valeur
					float valeur = Float.parseFloat(matcher.group(1));

					// Enregistrer la valeur
					if (infos instanceof InfosModelObjectifPourcentage) {
						InfosModelObjectifPourcentage cast = (InfosModelObjectifPourcentage)infos;
						this.update(row, new InfosModelObjectifPourcentage(cast.getNom(), valeur, cast.getBorneParticipation(), cast.getBorneAugmentation()));
					} else if (infos instanceof InfosModelObjectifPoints) {
						InfosModelObjectifPoints cast = (InfosModelObjectifPoints)infos;
						this.update(row, new InfosModelObjectifPoints(cast.getNom(), valeur, cast.getBorneParticipation()));
					}
				} else {
					ViewHelper.derror(this.w_parent, "Edition d'un objectif", "Le nombre de points d'un objectif doit être un nombre décimale.");
				}
				break;
		}
	}
}
