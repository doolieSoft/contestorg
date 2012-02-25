package org.contestorg.views;

import java.awt.Window;

import javax.swing.table.TableModel;

import org.contestorg.infos.InfosModelHoraire;

/**
 * Modèle de données pour un tableau d'horaires
 */
public class TMHoraires extends TMAbstract<InfosModelHoraire>
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public TMHoraires(Window w_parent) {
		// Constructeur
		super(w_parent);
	}
	
	/**
	 * @see TMAbstract#getAddWindow()
	 */
	@Override
	public Window getAddWindow () {
		return new JDHoraireCreer(this.w_parent, this);
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	@Override
	public Window getUpdateWindow (InfosModelHoraire infos) {
		return new JDHoraireEditer(this.w_parent, this, infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
	@Override
	public boolean acceptDelete (InfosModelHoraire infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer l'emplacement \"" + InfosModelHoraire.getJoursHumain(infos.getJours()) + ", " + InfosModelHoraire.getHeureHumain(infos.getDebut()) + " - " + InfosModelHoraire.getHeureHumain(infos.getFin()) + "\" ?")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see TMAbstract#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}

	// Implémentation manquante de TableModel
	
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
		switch(column) {
			case 0 : return "Jour";
			case 1 : return "Heures";
		}
		return null;
	}
	
	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt (int row, int column) {
		switch(column) {
			case 0 : return InfosModelHoraire.getJoursHumain(this.get(row).getJours());
			case 1 : return InfosModelHoraire.getHeureHumain(this.get(row).getDebut())+" - "+InfosModelHoraire.getHeureHumain(this.get(row).getFin());
		}
		return null;
	}
	
	/**
	 * @see TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	
	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt (Object object, int row, int column) {
		// Caster l'objet en string
		String string = (String)object;
		
		// Quel colonne ?
		switch(column) {
			case 0 : // Jours
				// Récupérer le AND binaire sur la valeur des jours
				int jours = InfosModelHoraire.getJoursBinaire(string);
				if(jours != 0) {
					// Mettre à jour le TableModel
					this.update(row, new InfosModelHoraire(jours, this.get(row).getDebut(), this.get(row).getFin()));
				} else {
					// Message d'erreur
					ViewHelper.derror(this.w_parent, "Il faut au moins un jour valide pour un horaire.");
				}
				break;
			case 1 : // Heures
				// Spliter les heures sur le tiret
				String[] heures = string.split("-");
				
				// Vérifier s'il y a bien deux heures
				if(heures.length == 2) {
					// Récupérer l'heure de début
					int debut = InfosModelHoraire.getHeureMinutes(heures[0]);
					
					// Récupérer l'heure de fin
					int fin = InfosModelHoraire.getHeureMinutes(heures[1]);
					
					// Vérifier si les heures sont correctes
					if(debut != -1 && fin != -1) {
						// Mettre à jour le TableModel
						this.update(row, new InfosModelHoraire(this.get(row).getJours(), debut, fin));
					} else {
						// Message d'erreur
						ViewHelper.derror(this.w_parent, "Les heures de début et de fin d'un horaire doivent être données sous la forme \"??h?? - ??h??\".");
					}
				} else {
					// Message d'erreur
					ViewHelper.derror(this.w_parent, "Les heures de début et de fin d'un horaire doivent être données sous la forme \"??h?? - ??h??\".");
				}
				break;
		}
	}

}
