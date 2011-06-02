<?php

class ConfHelper
{
	public function conf()
	{
		return Application::getService('conf');
	}
}