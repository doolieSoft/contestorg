@echo OFF

REM Optimiser la taille un fichier PNG donné
REM @param %1 Répertoire du fichier PNG
REM @param %2 Nom du fichier PNG

REM Créer le répertoire temporaire
mkdir .\_temp

REM Optimisation 1
.\pngnq-0.5\pngnq.exe -vf -s1 -d .\_temp -e .png %1%2

REM Optimisation 2
REM .\optipng-0.6.5\optipng.exe -o7 %1%2

REM Déplacer les fichiers du répertoire temporaire vers le répertoire d'origine
move .\_temp\%2 %1

REM Supprimer le répertoire temporaire
del /q .\_temp\*
rmdir .\_temp