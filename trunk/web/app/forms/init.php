<?php

// Require de l'outil form
require('form.php');

// Définir les messages en cas de champs requis/invalide
Form::setMsgRequired('<p class="message m_error">Champ requis</p>');
Form::setMsgInvalid('<p class="message m_error">Champ invalide</p>');