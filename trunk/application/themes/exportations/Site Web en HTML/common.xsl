<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
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
	
	<!-- Template pour le menu -->
	<xsl:template name="menu">
		<!-- Menu -->
		<ul id="menu">
			<li><a href="index.html" title="Revenir à l'accueil">Accueil</a></li>
			<li><a href="pratique.html" title="Afficher les informations pratiques">Informations pratiques</a></li>
		</ul>
	</xsl:template>
	
</xsl:stylesheet>