@echo OFF

REM G�n�rer la documentation de l'application en PDF et en HTML

REM Se rendre dans le r�pertoire de la documentation
cd ..\..\doc

REM G�n�rer la documentation HTML
java -jar ..\lib\saxon-9he\saxon.jar documentation.xml ..\make\generer-doc\docbook-xsl-1.76.1\html\docbook.xsl > documentation.html

REM G�n�rer la documentation PDF
java -jar ..\lib\saxon-9he\saxon.jar documentation.xml ..\make\generer-doc\docbook-xsl-1.76.1\fo\docbook.xsl > documentation.fo
java -jar ..\lib\fop-1.0\fop-1.0.jar documentation.fo -pdf documentation.pdf
del documentation.fo