<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<head>
				<title>Phases éliminatoires</title>
				<link rel="shortcut icon" href="sport.png" type="image/x-icon" />
				<link href="common.css" rel="stylesheet" type="text/css" />
				<link href="style.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="common.js"></script>
			</head>
			<body onload="setTimeout('scrollit()', 3000);">
				<!-- Titre -->
				<xsl:choose>
					<xsl:when test="count(/concours/listeCategories/categorie) > 1">
						<h1><xsl:value-of select="/concours/listeCategories/categorie[@id=$idCategorie]/@nom" /></h1>
					</xsl:when>
					<xsl:otherwise>
						<h1>Phases éliminatoires</h1>
					</xsl:otherwise>
				</xsl:choose>
				
		
				<!-- Matchs des phases éliminatoires -->
				<xsl:choose>
					<!-- Catégorie existante -->
					<xsl:when test="count(/concours/listeCategories/categorie[@id=$idCategorie]) = 1">
						<xsl:apply-templates select="/concours/listeCategories/categorie[@id=$idCategorie]">
						</xsl:apply-templates>
					</xsl:when>
					<!-- Catégorie inexistante -->
					<xsl:otherwise>
						<p class="erreur">La catégorie indiquée n'existe pas.</p>
					</xsl:otherwise>
				</xsl:choose>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une catégorie -->
	<xsl:template match="categorie">
		<table width="100%">
			<tbody>
				<tr>
					<td width="{100 div (count(./listePhasesEliminatoires/phaseEliminatoire)+1)}%">
						<table width="100%">
							<xsl:apply-templates select="./listePhasesEliminatoires/phaseEliminatoire[position() = 1]" mode="diagramme-equipes">
								<xsl:sort select="@numero" data-type="number" />
								<xsl:with-param name="nbCellulesA">1</xsl:with-param>
								<xsl:with-param name="nbCellulesB">3</xsl:with-param>
							</xsl:apply-templates>
						</table>
					</td>
					<xsl:apply-templates select="./listePhasesEliminatoires/phaseEliminatoire[position() = 1]" mode="diagramme-matchs">
						<xsl:sort select="@numero" data-type="number" />
						<xsl:with-param name="nbCellulesA">1</xsl:with-param>
						<xsl:with-param name="nbCellulesB">3</xsl:with-param>
					</xsl:apply-templates>
				</tr>
			</tbody>
		</table>
	</xsl:template>
	
	<!-- Template d'une phase éliminatoire -->
	<xsl:template match="phaseEliminatoire" mode="diagramme-equipes">
		<xsl:apply-templates select="./matchPhaseEliminatoire" mode="diagramme-equipes">
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="phaseEliminatoire" mode="diagramme-matchs">
		<!-- Paramètres -->
		<xsl:param name="nbCellulesA" /> <!-- Nombre de cases vides avant et après les cases pleines -->
		<xsl:param name="nbCellulesB" /> <!-- Nombre de cases vides entre chaque case pleine -->
		
		<!-- Cellule de la phase éliminatoire -->
		<td width="{100 div (count(../phaseEliminatoire)+1)}%">
			<table width="100%">
				<!-- Cellules d'espacement du haut -->
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:value-of select="$nbCellulesA" /></xsl:with-param>
					<xsl:with-param name="contenu"><tr><td class="videPhasesEliminatoires"></td></tr></xsl:with-param>
				</xsl:call-template>

				<!--  Cellules des matchs -->
				<xsl:apply-templates select="./matchPhaseEliminatoire" mode="diagramme-matchs">
					<xsl:with-param name="nbCellules"><xsl:value-of select="$nbCellulesB" /></xsl:with-param>
				</xsl:apply-templates>
				
				<!-- Cellules d'espacement du bas -->
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:value-of select="$nbCellulesA" /></xsl:with-param>
					<xsl:with-param name="contenu"><tr><td class="videPhasesEliminatoires"></td></tr></xsl:with-param>
				</xsl:call-template>
			</table>
		</td>
		
		<!-- Appeller la prochaine phase éliminatoire -->
		<xsl:variable name="numero"><xsl:value-of select="./@numero" /></xsl:variable>
		<xsl:apply-templates select="../phaseEliminatoire[@numero = $numero+1]" mode="diagramme-matchs">
			<xsl:sort select="@numero" data-type="number" />
			<xsl:with-param name="nbCellulesA"><xsl:number value="$nbCellulesB" /></xsl:with-param>
			<xsl:with-param name="nbCellulesB"><xsl:number value="number($nbCellulesB)*2+1" /></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	
	<!-- Template d'un match de phase éliminatoire -->
	<xsl:template match="matchPhaseEliminatoire" mode="diagramme-equipes">
		<tr>
			<td class="equipePhasesEliminatoires">
				<!-- Equipe A -->
				<xsl:call-template name="equipe">
				  <xsl:with-param name="id" select="./participation[1]/@refEquipe" />
				</xsl:call-template>
			</td>
		</tr>
		<tr><td class="videPhasesEliminatoires"></td></tr>
		<tr>
			<td class="equipePhasesEliminatoires">
				<!-- Equipe B -->
				<xsl:call-template name="equipe">
				  <xsl:with-param name="id" select="./participation[2]/@refEquipe" />
				</xsl:call-template>
			</td>
		</tr>
		<xsl:if test="position() != last()">
			<tr><td class="videPhasesEliminatoires"></td></tr>
		</xsl:if>
	</xsl:template>
	<xsl:template match="matchPhaseEliminatoire" mode="diagramme-matchs">
		<!-- Paramètres -->
		<xsl:param name="nbCellules" />
		
		<!-- Vérifier s'il ne s'agit pas de la petite finale -->
		<xsl:if test="count(./@petiteFinale) = 0 or ./@petiteFinale != 'oui'">
			<!-- Cellules d'espacement -->
			<xsl:if test="position() != 1">
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:value-of select="$nbCellules" /></xsl:with-param>
					<xsl:with-param name="contenu"><tr><td class="videPhasesEliminatoires"></td></tr></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- Cellule du match -->
			<tr>
				<td class="matchPhasesEliminatoires">
					<xsl:choose>
						<xsl:when test="count(./participation[@resultat = 'victoire']/@refEquipe) = 1">
							<xsl:call-template name="equipe">
							  <xsl:with-param name="id" select="./participation[@resultat = 'victoire']/@refEquipe" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							...
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une équipe -->
	<xsl:template name="equipe">
		<xsl:param name="id" />
		<xsl:value-of select="//equipe[@id=$id]/@nom" />
	</xsl:template>
</xsl:stylesheet>
