<?php

// Démarrer le mécanisme de session
session_start();

// Détruire la session courante
session_destroy();

// Rediriger l'utilisateur sur l'accueil
header('Location:index.php');