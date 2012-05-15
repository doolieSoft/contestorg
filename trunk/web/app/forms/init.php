<?php

// Require de l'outil form
require('form.php');

// Définir les messages en cas de champs requis/invalide
Form::setErrorStart('<p class="message m_error">');
Form::setErrorEnd('</p>');
Form::setMsgRequired('Champ requis');
Form::setMsgInvalid('Champ invalide');