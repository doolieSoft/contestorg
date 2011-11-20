<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />
	<xsl:param name="idPhase" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<!-- Head -->
			<head>
				<title>Matchs de la phase qualificative <xsl:value-of select="//phaseQualificative[id=$idPhase]/@numero+1" /></title>
				<link rel="shortcut icon" href="favicon.png" type="image/x-icon" />
				<link href="common.css" rel="stylesheet" type="text/css" />
				<link href="style.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="common.js"></script>
			</head>
			<!-- Body -->
			<body onload="setTimeout('scrollit()', 3000);">
				<xsl:choose>
					<!-- Phase qualificative existante -->
					<xsl:when test="count(//phaseQualificative[@id=$idPhase]) = 1">
						<!-- Titre -->
						<xsl:if test="count(//phaseQualificative[@id=$idPhase]/../../../../../categorie) != 1">
							<h1><xsl:value-of select="//phaseQualificative[@id=$idPhase]/../../../../@nom" /></h1>
						</xsl:if>
						<xsl:if test="count(//phaseQualificative[@id=$idPhase]/../../../poule) != 1">
							<h2><xsl:value-of select="//phaseQualificative[@id=$idPhase]/../../@nom" /></h2>
						</xsl:if>
						<h3>Matchs de la phase qualificative <xsl:value-of select="//phaseQualificative[@id=$idPhase]/@numero+1" /></h3>
						
						<!-- Appel du template -->
						<xsl:apply-templates select="//phaseQualificative[@id=$idPhase]">
						</xsl:apply-templates>
					</xsl:when>
					<!-- Phase qualificative inexistance -->
					<xsl:otherwise>
						<p class="erreur">La phase qualificative indiquée n'existe pas.</p>
					</xsl:otherwise>
				</xsl:choose>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une phase qualificative -->
	<xsl:template match="phaseQualificative">
		<table>
			<thead>
				<tr>
					<th id="th-participantA">
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe A
							</xsl:when>
							<xsl:otherwise>
								Joueur A
							</xsl:otherwise>
						</xsl:choose>
					</th>
					<th id="th-participantB">
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe B
							</xsl:when>
							<xsl:otherwise>
								Joueur B
							</xsl:otherwise>
						</xsl:choose>
					</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="./matchPhaseQualificative">
				</xsl:apply-templates>
			</tbody>
		</table>
	</xsl:template>
	
	<!-- Template d'un match -->
	<xsl:template match="matchPhaseQualificative">
		<xsl:variable name="idA" select="./participation[1]/@refParticipant" />
		<xsl:variable name="idB" select="./participation[2]/@refParticipant" />
		<tr>
			<td class="td-participantA">
				<xsl:choose>
					<xsl:when test="$idA != ''">
						<xsl:value-of select="//participant[@id=$idA]/@nom" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe fantôme
							</xsl:when>
							<xsl:otherwise>
								Joueur fantôme
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="td-participantB">
				<xsl:choose>
					<xsl:when test="$idB != ''">
						<xsl:value-of select="//participant[@id=$idB]/@nom" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe fantôme
							</xsl:when>
							<xsl:otherwise>
								Joueur fantôme
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
