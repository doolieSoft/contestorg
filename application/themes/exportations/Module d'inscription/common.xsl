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
		</head>
	</xsl:template>
	
	<!-- Template pour le menu -->
	<xsl:template name="menu">
		<!-- Menu -->
		<ul id="menu">
			<li><a href="index.php" title="Revenir à l'accueil">Accueil</a></li>
			<li><a href="inscription.php" title="Accèder à la zone d'inscription">Inscription</a></li>
			<li><a href="administration.php" title="Accèder à la zone d'administration">Administration</a></li>
		</ul>
	</xsl:template>

</xsl:stylesheet>