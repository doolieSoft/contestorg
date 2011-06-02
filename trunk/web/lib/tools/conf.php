<?php

/**
 * Class to load and save conf
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2009-10-13
 */
class Conf implements Iterator, Countable, ArrayAccess
{
	// Data
	private $data;
	
	// Constructor
	public function __construct(ConfData $pData)
	{
		$this->data = $pData;
		$this->data->load();
	}
	
	// Iterator implementation
	public function rewind() { $this->data->rewind(); }
	public function key() { return $this->data->key(); }
	public function next() { $this->data->next(); }
	public function current() { return $this->data->current(); }
	public function valid() { return $this->data->valid(); }
	
	// Countable implementation
	public function count() { return $this->data->count(); }
	
	// ArrayAccess implementation
	public function offsetExists($key) { return $this->data->offsetExists($key); }
	public function offsetGet($key) { return $this->data->offsetGet($key); }
	public function offsetSet($key, $value) { $this->data->offsetSet($key,$value); }
	public function offsetUnset($key) { $this->data->offsetUnset($key); }
	
	// ToString
	public function __toString()
	{
		$string = '<ul>';
		foreach($this->data as $category => $parameters) {
			$string .= '<li>'.$category.'<ul>';
			foreach($parameters as $name => $value) {
				$string .= '<li>'.$name.' = '.$value.'</li>';
			}
			$string .= '</ul></li>';
		}
		$string .= '</ul>';
		return $string;
	}
}

?>