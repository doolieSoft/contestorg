<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Template pour le head -->
	<xsl:template name="head">
		<head>
			<title>
				<xsl:value-of select="/concours/@nom" />
				<xsl:if test="/concours/@lieu != ''">
					(<xsl:value-of select="/concours/@lieu" />)
				</xsl:if>
			</title>
			<link href="style.css" rel="stylesheet" type="text/css" />
			<link rel="shortcut icon" type="image/x-icon" href="./favicon.png" />
			<script src="./jquery.js"></script>
			<script type="text/javascript">
				// Fonction d'affichage d'une catégorie ou d'une poule
				function show(id) {
					$('#'+id).parent().children().hide();
					$('#'+id).show();
				}
			
				// Quand le DOM de la page est prêt
				$(document).ready(function () {
					// Séléctionner les premiers boutons
					$('.buttons-categories a:first, .buttons-poules a:first').attr('class','selected');
				
					// Masquer les catégories et poules qui ne sont pas premières
					$('.categorie').each(function(i,categorie) {
						if(i > 0) {
							$(categorie).hide();
						}
						$(categorie).find('.poule').each(function(j,poule) {
							if(j > 0) {
								$(poule).hide();
							}
						});
					});
				});
			</script>
		</head>
	</xsl:template>
	
	<!-- Template pour le head -->
	<xsl:template name="header">
		<div id="header">
			<!-- Barre colorée -->
			<div id="bar">
				<div id="colorA"></div>
				<div id="colorB"></div>
				<div id="colorC"></div>
				<div id="colorD"></div>
				<div id="colorE"></div>
				<div id="colorF"></div>
				<div id="colorG"></div>
				<div id="colorH"></div>
				<div id="colorI"></div>
				<div id="colorJ"></div>
			</div>
			
			<!-- Titre -->
			<h1>
				<xsl:value-of select="/concours/@nom" />
				<xsl:if test="/concours/@lieu != ''">
					(<xsl:value-of select="/concours/@lieu" />)
				</xsl:if>
			</h1>
			
			<!-- Menu -->
			<ul id="menu">
				<li><a href="index.html" title="Revenir à l'accueil">Accueil</a></li>
				<li><a href="pratique.html" title="Afficher les informations pratiques">Informations pratiques</a></li>
			</ul>
		</div>
	</xsl:template>
	
	<!-- Template pour le footer -->
	<xsl:template name="footer">
		<div id="footer">Page générée à l'aide du logiciel d'organisation de concours <a href="http://www.elfangels.fr/contestorg/">ContestOrg</a></div>
	</xsl:template>

</xsl:stylesheet>