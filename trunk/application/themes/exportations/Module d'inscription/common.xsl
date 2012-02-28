<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>

	<!-- Template pour le head -->
	<xsl:template name="head">
		<!-- Head -->
		<head>
			<title>
				<xsl:value-of select="/concours/@nom" />
				<xsl:if test="/concours/@lieu != ''">
					(<xsl:value-of select="/concours/@lieu" />)
				</xsl:if>
			</title>
			<link href="style.css" rel="stylesheet" type="text/css" />
			<link href="common.css" rel="stylesheet" type="text/css" />
			<link rel="shortcut icon" type="image/x-icon" href="./favicon.png" />
		</head>
	</xsl:template>
	
	<!-- Template pour le menu -->
	<xsl:template name="menu">
		<!-- Menu -->
		<ul id="menu">
			<li><a href="index.php" title="Revenir à l'accueil">Accueil</a></li>
			<li>
				<a href="participants.php" title="Accèder à l'espace des participants">
					Espace des 
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							équipes
						</xsl:when>
						<xsl:otherwise>
							joueurs
						</xsl:otherwise> 
					</xsl:choose>
				</a>
			</li>
			<li><a href="organisateurs.php" title="Accèder à l'espace des organisateurs">Espace des organisateurs</a></li>
			<xsl:call-template name="php-start" />
				<xsl:text disable-output-escaping="yes">
					// Vérifier si l'utilisateur courant est connecté
					if(isset($_SESSION['participant']) || isset($_SESSION['organisateur'])) {
						echo '</xsl:text><li><a href="actions.php?action=deconnexion" title="Se déconnecter">Déconnexion</a></li><xsl:text disable-output-escaping="yes">';
					}
				</xsl:text>
			<xsl:call-template name="php-end" />
		</ul>
	</xsl:template>

	<!-- Template pour le formulaire d'inscription des participants -->
	<xsl:template name="participants-inscription">
		<xsl:call-template name="php-start" />
			// Groupes d'entrées
			$group_connexion = 'Informations de connexion';
			$group_participant = 'Informations '.('<xsl:value-of select="/concours/@participants" />' == 'equipes' ? 'de l\'équipe' : 'du joueur');
		
			<xsl:text disable-output-escaping="yes">
				// Créer le formulaire 
				$form = new Form(Form::METHOD_POST,'#inscription');
				$form->add(new FormText('email', 'Adresse email', FormText::TYPE_MONOLINE,40),true,$group_connexion);
				function check_email(FormText $email,DOMDocument $document) {
					// Vérifier la validité de l'adresse email
					if(filter_var($email->getValue(), FILTER_VALIDATE_EMAIL) === false) {
						return false;
					}
					
					// Vérifier si l'adresse email est déjà enregistrée
					$xpath = new DOMXPath($document);
					if($xpath->query('//inscription/listeParticipants/participant[@email="'.str_replace(array('\\','"'),array('\\\\','\"'),$email->getValue()).'"]')->length != 0) {
						$email->setError('Cette adresse email est déjà utilisée');
						return false;
					}
					
					// Adresse email valide
					return true;
				}
				$form->email->setCallback('check_email',array($document));
				$form->add(new FormText('password', 'Mot de passe', FormText::TYPE_PASSWORD,40),true,$group_connexion);
				$form->add(new FormText('confirmation', 'Confirmation', FormText::TYPE_PASSWORD,40),true,$group_connexion);
				function check_password(FormText $password,FormText $confirmation) {
					// Vérifier si le mot de passe correspond avec la confirmation
					if($password->getValue() != $confirmation->getValue()) {
						$password->setError('Le mot de passe ne correspond pas');
						return false;
					}
					
					// Mot de passe correspondant avec la confirmation
					return true;
				}
				$form->password->setCallback('check_password',array($form->confirmation));
				$form->add(new FormText('nom', 'Nom', FormText::TYPE_MONOLINE,42),true,$group_participant);
				function check_nom(FormText $nom,DOMDocument $document) {
					// Vérifier si le nom est déjà enregistrée
					$xpath = new DOMXPath($document);
					if($xpath->query('//inscription/listeParticipants/participant[translate(@nom,\'ABCDEFGHIJKLMNOPQRSTUVWXYZ\', \'abcdefghijklmnopqrstuvwxyz\')="'.str_replace(array('\\','"'),array('\\\\','\"'),strtolower($nom->getValue())).'"]')->length != 0) {
						$nom->setError('Ce nom est déjà utilisé');
						return false;
					}
					
					// Nom valide
					return true;
				}
				$form->nom->setCallback('check_nom',array($document));
				$form->add(new FormText('ville', 'Ville', FormText::TYPE_MONOLINE,42),false,$group_participant);
			</xsl:text>
			<xsl:for-each select="/concours/listeProprietes/propriete">
				<xsl:text disable-output-escaping="yes"> 
					$propriete = $form->add(new FormText('propriete_</xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">', '</xsl:text><xsl:value-of select="@nom" /><xsl:text disable-output-escaping="yes">', FormText::TYPE_MONOLINE,42),</xsl:text><xsl:choose><xsl:when test="@obligatoire = 'oui'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose><xsl:text disable-output-escaping="yes">,$group_participant);
				</xsl:text>								
				<xsl:choose>
					<xsl:when test="@type = 'decimal'">
						<xsl:text disable-output-escaping="yes">
							function check_propriete_</xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">(FormText $propriete) {
								if(!preg_match('/^[0-9]+(\.[0-9]+)?$/',$propriete->getValue())) {
									$propriete->setError('Ce champ doit être un décimal');
									return false;
								}
								return true;
							}
							$propriete->setCallback('check_propriete_</xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">');
						</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'entier'">
						<xsl:text disable-output-escaping="yes">
							function check_propriete_</xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">(FormText $propriete) {
								if(!preg_match('/^[0-9]+$/',$propriete->getValue())) {
									$propriete->setError('Ce champ doit être un entier');
									return false;
								}
								return true;
							}
							$propriete->setCallback('check_propriete_</xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">');
						</xsl:text>
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
			<xsl:text disable-output-escaping="yes">
				$form->add(new FormText('details', 'Détails', FormText::TYPE_MULTILINE,array(32,5)),false,$group_participant);
				$form->add(new FormSubmit('inscription','Inscription'));
				
				// Valider le formulaire
				if($form->validate(true)) {
					// Récupérer une instance de xpath
					$xpath = new DOMXPath($document);
				
					// Récupérer l'identifiant
					$dernier = $xpath->query('//inscription/listeParticipants/participant[last()]');
					$id = $dernier->length == 0 ? 0 : $dernier->item(0)->getAttribute('id')+1;
				
					// Ajouter le participant
					$participants = $xpath->query('//inscription/listeParticipants')->item(0);
					$participant = $participants->appendChild($document->createElement('participant'));
					$participant->appendChild($document->createAttribute('id'))->value = $id;
					$participant->appendChild($document->createAttribute('email'))->value = $form->email->getValue();
					$participant->appendChild($document->createAttribute('password'))->value = sha1($form->password->getValue());
					$participant->appendChild($document->createAttribute('nom'))->value = $form->nom->getValue();
					$participant->appendChild($document->createAttribute('ville'))->value = $form->ville->getValue();
					$participant->appendChild($document->createAttribute('details'))->value = $form->details->getValue();
					$participant->appendChild($document->createAttribute('refStatut'))->value = $xpath->query('//inscription/listeStatuts/statut')->item(0)->getAttribute('id');
					$proprietes = $participant->appendChild($document->createElement('listeProprietes'));
				</xsl:text>
				<xsl:for-each select="/concours/listeProprietes/propriete">
					<xsl:text disable-output-escaping="yes">
						$propriete = $proprietes->appendChild($document->createElement('propriete'));
						$propriete->appendChild($document->createAttribute('id'))->value = </xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">;
						$propriete->appendChild($document->createAttribute('valeur'))->value = $form->propriete_</xsl:text><xsl:value-of select="@id" /><xsl:text disable-output-escaping="yes">->getValue();
					</xsl:text>
				</xsl:for-each>
				<xsl:text disable-output-escaping="yes">
					$document->save('donnees.xml');
					
					// Confirmer l'inscription
					echo '</xsl:text><p class="success">Votre inscription a bien été prise en compte. Vous pouvez désormais vous connecter.</p><xsl:text disable-output-escaping="yes">';
				}
			</xsl:text>
		<xsl:call-template name="php-end" />
	</xsl:template>
	
	<!-- Template pour le formulaire d'inscription des participants -->
	<xsl:template name="participants-avancement-inscription">
		TODO
	</xsl:template>
	
	<!-- Template pour le formulaire de connexion des participants -->
	<xsl:template name="participants-connexion">
			<xsl:call-template name="php-start" />
				<xsl:text disable-output-escaping="yes">
					// Créer le formulaire 
					$form = new Form(Form::METHOD_POST,'#connexion');
					$form->add(new FormText('email', 'Adresse email', FormText::TYPE_MONOLINE, 40));
					$form->add(new FormText('password', 'Mot de passe', FormText::TYPE_PASSWORD, 40));
					$form->add(new FormSubmit('connexion','Connexion'));
					
					// Vérifier si les identifiants sont corrects
					function check_identifiants(FormText $email,FormText $password,DOMDocument $document) {
						// Vérifier si les identifiants sont corrects
						$xpath = new DOMXPath($document);
						if($xpath->query('//inscription/listeParticipants/participant[@email="'.str_replace(array('\\','"'),array('\\\\','\"'),$email->getValue()).'"][@password="'.sha1($password->getValue()).'"]')->length == 0) {
							$email->setError('Vos identifiants sont incorrects');
							return false;
						}
						
						// Indentifiants valides
						return true;
					}
					$form->email->setCallback('check_identifiants',array($form->password,$document));
					
					// Valider le formulaire
					if($form->validate(true)) {
						// Connecter l'utilisateur
						$_SESSION['participant'] = 1;
						$_SESSION['email'] = $form->email->getValue();
						
						// Rafraichir la page
						echo '</xsl:text><script type="text/javascript">window.location = \'participants.php\';</script><xsl:text disable-output-escaping="yes">';
					}
				</xsl:text>
			<xsl:call-template name="php-end" />
	</xsl:template>
	
	<!-- Template pour le formulaire de connexion des organisateurs -->
	<xsl:template name="organisateurs-connexion">
		<xsl:param name="password" />
		<xsl:call-template name="php-start" />
			<xsl:text disable-output-escaping="yes">
				// Créer le formulaire 
				$form = new Form(Form::METHOD_POST,'#connexion');
				$form->add(new FormText('password', 'Mot de passe', FormText::TYPE_PASSWORD, 40));
				$form->add(new FormSubmit('connexion','Connexion'));
				
				// Vérifier si le mot de passe est correct
				function check_password(FormText $password) {
					// Vérifier si le mot de passe est correct
					if(sha1($password->getValue()) != '</xsl:text><xsl:value-of select="java:org.contestorg.common.Tools.hash($password,'SHA-1')" /><xsl:text disable-output-escaping="yes">') {
						$password->setError('Le mot de passe n\'est pas correct');
						return false;
					}
					
					// Mot de passe valide
					return true;
				}
				$form->password->setCallback('check_password');
				
				// Valider le formulaire
				if($form->validate(true)) {
					// Connecter l'utilisateur
					$_SESSION['organisateur'] = 1;
					
					// Rafraichir la page
					echo '</xsl:text><script type="text/javascript">window.location = \'organisateurs.php\';</script><xsl:text disable-output-escaping="yes">';
				}
			</xsl:text>
		<xsl:call-template name="php-end" />
	</xsl:template>
	
	<!-- Template pour la liste des statuts -->
	<xsl:template name="organisateurs-statuts">
		<xsl:call-template name="php-start" />
			<xsl:text disable-output-escaping="yes">
				$xpath = new DOMXPath($document);
				$statuts = $xpath->query('//inscription/listeStatuts/statut');
				if($statuts->length == 0) {
					echo 'Pas de statuts';
				} else {
			</xsl:text>
		<xsl:call-template name="php-end" />		
		<script type="text/javascript">
			function statut_modifier_suivants(statut,select) {
				var suivants = new Array();
				for (var i in select.options) {
					if(select.options[i].selected) {
						suivants.push(select.options[i].value);
					}
				}
				window.location = 'actions.php?action=statut-modifier-suivants&amp;statut='+statut+'&amp;suivants='+suivants.join('-');
			}
		</script>	
		<table class="cute" style="width:100%;">
			<thead>
				<tr>
					<th>Nom</th>
					<th>
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">Equipes acceptées ?</xsl:when>
							<xsl:otherwise>Joueurs acceptés ?</xsl:otherwise> 
						</xsl:choose>
					</th>
					<th>Statuts suivants</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>	
				<xsl:call-template name="php-start" />
					<xsl:text disable-output-escaping="yes">
						foreach($statuts as $statut) {
							echo '</xsl:text><tr><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><td><xsl:text disable-output-escaping="yes">',$statut->getAttribute('nom'),'</xsl:text></td><xsl:text disable-output-escaping="yes">';
							if($statut->getAttribute('type') == 'accepte') {
								echo '</xsl:text><td><input type="checkbox" checked="checked" onclick="window.location = \'actions.php?action=statut-modifier-type&amp;statut=',$statut->getAttribute('id'),'\'; return false;" /></td><xsl:text disable-output-escaping="yes">';
							} else {
								echo '</xsl:text><td><input type="checkbox" onclick="window.location = \'actions.php?action=statut-modifier-type&amp;statut=',$statut->getAttribute('id'),'\'; return false;" /></td><xsl:text disable-output-escaping="yes">';
							}
							echo '</xsl:text><td><select multiple="multiple" rows="3" onchange="statut_modifier_suivants(\'',$statut->getAttribute('id'),'\',this);"><xsl:text disable-output-escaping="yes">';
							$suivants = array();
							foreach($xpath->query('//inscription/listeStatuts/statut[@id='.$statut->getAttribute('id').']/listeSuivants/suivant') as $suivant) {
								$suivants[] = $suivant->getAttribute('refStatut');
							}
							foreach($xpath->query('//inscription/listeStatuts/statut[@id!='.$statut->getAttribute('id').']') as $suivant) {
								if(in_array($suivant->getAttribute('id'),$suivants)) {
									echo '</xsl:text><option value="',$suivant->getAttribute('id'),'" selected="selected"><xsl:text disable-output-escaping="yes">',$suivant->getAttribute('nom'),'</xsl:text></option><xsl:text disable-output-escaping="yes">';
								} else {
									echo '</xsl:text><option value="',$suivant->getAttribute('id'),'"><xsl:text disable-output-escaping="yes">',$suivant->getAttribute('nom'),'</xsl:text></option><xsl:text disable-output-escaping="yes">';
								}
							}
							echo '</xsl:text></select></td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><a onclick="var nom = window.prompt(\'Nom du statut :\',\'',$statut->getAttribute('nom'),'\'); if(nom != null) window.location = \'actions.php?action=statut-modifier-nom&amp;statut=',$statut->getAttribute('id'),'&amp;nom=\'+nom; return false;" href="#" title="Renommer le statut">Renommer</a><xsl:text disable-output-escaping="yes"> - ',
							     '</xsl:text><a onclick="return window.confirm(\'Désirez-vous vraiment supprimer ce statut ?\');" href="actions.php?action=statut-supprimer&amp;statut=',$statut->getAttribute('id'),'" title="Supprimer le statut">Supprimer</a><xsl:text disable-output-escaping="yes">',
							     '</xsl:text></td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text></tr><xsl:text disable-output-escaping="yes">';
						}
					</xsl:text>
				<xsl:call-template name="php-end" />	
			</tbody>
		</table>
		<xsl:call-template name="php-start" />
			<xsl:text disable-output-escaping="yes">
				}
			</xsl:text>
		<xsl:call-template name="php-end" />
		<p>
			<a href="#" onclick="var nom = window.prompt('Nom du statut :'); if(nom != null) window.location = 'actions.php?action=statut-ajouter&amp;nom='+nom; return false;">Ajouter un statut</a>
		</p>
	</xsl:template>
	
	<!-- Template pour la liste des participants -->
	<xsl:template name="organisateurs-participants">
		<xsl:call-template name="php-start" />
			<xsl:text disable-output-escaping="yes">
				$xpath = new DOMXPath($document);
				$participants = $xpath->query('//inscription/listeParticipants/participant');
				if($participants->length == 0) {
					echo 'Pas encore de participants';
				} else {
			</xsl:text>
		<xsl:call-template name="php-end" />
		<table class="cute" style="width:100%;">
			<thead>
				<tr>
					<th>Nom</th>
					<th>Email</th>
					<th>Statut</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<xsl:call-template name="php-start" />
					<xsl:text disable-output-escaping="yes">
						foreach($participants as $participant) {
							echo '</xsl:text><tr><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><td><xsl:text disable-output-escaping="yes">',$participant->getAttribute('nom'),'</xsl:text></td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><td><a href="mailto:',$participant->getAttribute('email'),'" title="Contacter le participant"><xsl:text disable-output-escaping="yes">',$participant->getAttribute('email'),'</xsl:text></a></td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><td><select onchange="window.location=\'actions.php?action=participant-modifier-statut&amp;participant=',$participant->getAttribute('id'),'&amp;statut=\'+this.options[this.selectedIndex].value;"><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><option value="',$participant->getAttribute('refStatut'),'" selected="selected"><xsl:text disable-output-escaping="yes">',$xpath->query('//inscription/listeStatuts/statut[@id='.$participant->getAttribute('refStatut').']')->item(0)->getAttribute('nom'),'</xsl:text></option><xsl:text disable-output-escaping="yes">';
							$xpath = new DOMXPath($document);
							foreach($xpath->query('//inscription/listeStatuts/statut[@id='.$participant->getAttribute('refStatut').']/listeSuivants/suivant') as $suivant) {
								echo '</xsl:text><option value="',$suivant->getAttribute('refStatut'),'"><xsl:text disable-output-escaping="yes">',$xpath->query('//inscription/listeStatuts/statut[@id='.$suivant->getAttribute('refStatut').']')->item(0)->getAttribute('nom'),'</xsl:text></option><xsl:text disable-output-escaping="yes">';
							}
							echo '</xsl:text></select></td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text><td><a onclick="return window.confirm(\'Désirez-vous vraiment supprimer ce participant ?\');" href="actions.php?action=participant-supprimer&amp;participant=',$participant->getAttribute('id'),'" title="Supprimer le participant">Supprimer</a></td><xsl:text disable-output-escaping="yes">',
							     '</xsl:text></tr><xsl:text disable-output-escaping="yes">';
						}
					</xsl:text>
				<xsl:call-template name="php-end" />
			</tbody>
		</table>
		<p>
			<a href="actions.php?action=telecharger">
				Télécharger la liste des
				<xsl:choose>
					<xsl:when test="/concours/@participants = 'equipes'">équipes</xsl:when>
					<xsl:otherwise>joueurs</xsl:otherwise>
				</xsl:choose>
			</a> -
			<xsl:choose>
				<xsl:when test="/concours/@participants = 'equipes'">
					<a href="actions.php?action=participants-vider" onclick="return window.confirm('Désirez-vous vraiment vider la liste des équipes ?');">Vider la liste des équipes</a>
				</xsl:when>
				<xsl:otherwise>
					<a href="actions.php?action=participants-vider" onclick="return window.confirm('Désirez-vous vraiment vider la liste des joueurs ?');">Vider la liste des joueurs</a>
				</xsl:otherwise> 
			</xsl:choose>
			
		</p>
		<xsl:call-template name="php-start" />
			<xsl:text disable-output-escaping="yes">
				}
			</xsl:text>
		<xsl:call-template name="php-end" />
	</xsl:template>

</xsl:stylesheet>