<?php

class ConnectedFilter extends Filter
{
	/**
	 * @see Filter::process
	 */
	public function process() {
		// Continuer l'éxecution
		$this->forward();
	}
}