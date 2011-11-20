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
						echo '</xsl:text><li><a href="deconnexion.php" title="Se déconnecter">Déconnexion</a></li><xsl:text disable-output-escaping="yes">';
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
			<xsl:if test="/concours/@participants = 'equipes'">
				<xsl:text disable-output-escaping="yes">
					$form->add(new FormText('membres', 'Membres', FormText::TYPE_MONOLINE,42),false,$group_participant);
				</xsl:text>
			</xsl:if>
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
					// Ajouter le participant
					$xpath = new DOMXPath($document);
					$participants = $xpath->query('//inscription/listeParticipants')->item(0);
					$participant = $participants->appendChild($document->createElement('participant'));
					$participant->appendChild($document->createAttribute('email'))->value = $form->email->getValue();
					$participant->appendChild($document->createAttribute('password'))->value = sha1($form->password->getValue());
					$participant->appendChild($document->createAttribute('nom'))->value = $form->nom->getValue();
					$participant->appendChild($document->createAttribute('ville'))->value = $form->ville->getValue();
				</xsl:text>
				<xsl:if test="/concours/@participants = 'equipes'">
					<xsl:text disable-output-escaping="yes">
						$participant->appendChild($document->createAttribute('membres'))->value = $form->membres->getValue();
					</xsl:text>
				</xsl:if>
				<xsl:text disable-output-escaping="yes">
					$participant->appendChild($document->createAttribute('details'))->value = $form->details->getValue();
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
		TODO
	</xsl:template>
	
	<!-- Template pour la liste des participants -->
	<xsl:template name="organisateurs-participants">
		TODO
	</xsl:template>

</xsl:stylesheet>