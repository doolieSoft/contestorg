<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<head>
				<title>Liste des diffusions</title>
				<link rel="shortcut icon" href="sport.png" type="image/x-icon" />
				<link href="common.css" rel="stylesheet" type="text/css" />
				<link href="style.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="common.js"></script>
			</head>
			<body>
				<!-- Titre -->
				<h1>Liste des diffusions</h1>
		
				<!-- Liste des diffusions -->
				<table>
					<thead>
						<tr>
							<th id="th-nom">Nom</th>
							<th id="th-lien">Adresse</th>
						</tr>
					</thead>
					<tbody>
						<xsl:apply-templates select="/concours/listeDiffusions/diffusion">
							<xsl:sort select="@port" data-type="number" />
						</xsl:apply-templates>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une diffusion -->
	<xsl:template match="diffusion">
		<tr>
			<td class="td-nom">
				<xsl:value-of select="./@nom" />
			</td>
			<td class="td-lien">
				<a href="http://{/concours/listeDiffusions/@hote}:{./@port}/">
					http://<xsl:value-of select="/concours/listeDiffusions/@hote" />:<xsl:value-of select="./@port" />/
				</a>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
