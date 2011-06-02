<?php

/**
 * Class to paginate results
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2010-05-10
 */
class Paginator implements Iterator {
	
	/** array Items array */
	private $items;
	
	/** int Current page */
	private $current;
	
	/** int Total pages count */
	private $count;
	
	/**
	 * Constructor
	 * @param items array items array
	 * @param current int current page
	 * @param count int total pages count
	 */
	public function Paginator($items,$current,$count) {
		$this->items = $items;
		$this->current = $current;
		$this->count = $count;
	}
	
	// Getters
	public function getItems() { return $this->items; }
	public function getCurrent() { return $this->current; }
	public function getCount() { return $this->count; }
	
	// Iterator implementation
	public function rewind() { reset($this->items); }
	public function key() { return key($this->items); }
	public function next() { next($this->items); }
	public function current() { return current($this->items); }
	public function valid() { return current($this->items) !== false; }
	
}