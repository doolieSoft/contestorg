@echo OFF

REM Génére l'aide de l'application en PDF et en HTML

REM Se rendre dans le répertoire de la documentation
cd ..\..\doc

REM Générer l'aide HTML
java -jar ..\lib\saxon-9he\saxon.jar Aide.xml ..\make\generer-doc\docbook-xsl-1.76.1\html\docbook.xsl > Aide.html

REM Générer l'aide PDF
java -jar ..\lib\saxon-9he\saxon.jar Aide.xml ..\make\generer-doc\docbook-xsl-1.76.1\fo\docbook.xsl > Aide.fo
java -jar ..\lib\fop-1.0\fop-1.0.jar Aide.fo -pdf Aide.pdf
del Aide.fo