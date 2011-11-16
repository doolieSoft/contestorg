package views;

import infos.InfosModelDiffusion;
import infos.InfosModelTheme;

import java.awt.Window;

import common.Pair;

public class TMDiffusions extends TMAbstract<Pair<InfosModelDiffusion,InfosModelTheme>>
{

	// Constructeur
	public TMDiffusions(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}

	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDDiffusionCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (Pair<InfosModelDiffusion,InfosModelTheme> infos) {
		return new JDDiffusionEditer(this.w_parent, this, infos);
	}
	@Override
	public boolean acceptDelete (Pair<InfosModelDiffusion,InfosModelTheme> infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer la diffusion \"" + infos.getSecond().getNom() + "\" écoutant sur le port "+ infos.getFirst().getPort() +" ?")) {
			return true;
		} else {
			return false;
		}
	}

	// Implémentation de manquante de TableModel
	@Override
	public Class<?> getColumnClass (int column) {
		switch (column) {
			case 0:
				return String.class;
			case 1:
				return Integer.class;
		}
		return null;
	}
	@Override
	public int getColumnCount () {
		return 2;
	}
	@Override
	public String getColumnName (int column) {
		switch (column) {
			case 0:
				return "Nom";
			case 1:
				return "Port";
		}
		return null;
	}
	@Override
	public Object getValueAt (int row, int column) {
		// Retourner l'information demandée
		switch (column) {
			case 0:
				return this.get(row).getFirst().getNom();
			case 1:
				return this.get(row).getFirst().getPort();
		}
		return null;
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		switch(column) {
			case 0:
				this.update(row,new Pair<InfosModelDiffusion,InfosModelTheme>(new InfosModelDiffusion((String)object,this.get(row).getFirst().getPort()), this.get(row).getSecond()));
				break;
			case 1:
				this.update(row,new Pair<InfosModelDiffusion,InfosModelTheme>(new InfosModelDiffusion(this.get(row).getFirst().getNom(),(Integer)object), this.get(row).getSecond()));
				break;
		}
	}

}
