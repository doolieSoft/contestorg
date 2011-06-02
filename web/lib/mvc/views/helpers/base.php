<?php

class BaseHelper
{
	public function base()
	{
		return Request::getBaseUrl();
	}
}