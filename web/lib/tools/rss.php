<?php

/**
 * Class to build RSS flux
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2011-04-29
 */
class RSS {
	/// @var DOMDocument Flux
	private $document;
	
	/// @var DOMElement Channel
	private $channel;

	/**
	 * Constructor
	 * @param $title string channel title
	 * @param $link string channel link
	 * @param $description string channel description
	 * @param $category string channel category
	 * @param $language string language used
	 * @param $refresh int caching in minutes
	 * @param $build int channel build timestamp
	 */
	public function __construct($title,$link,$description,$category=null,$language=null,$refresh=null,$build=null) {
		// Create document
		$this->document = new DOMDocument('1.0', 'utf-8');
		
		// Create root
		$root = $this->document->createElement('rss');
		$root->setAttribute('version','2.0');
		$this->document->appendChild($root);
		
		// Create channel
		$this->channel = $this->document->createElement('channel');
		$this->channel->appendChild($this->document->createElement('title',$title));
		$this->channel->appendChild($this->document->createElement('link',$link));
		$this->channel->appendChild($this->document->createElement('description',$description));
		if($category !== null) $this->channel->appendChild($this->document->createElement('category',$category));
		if($language !== null) $this->channel->appendChild($this->document->createElement('language',$language));
		if($refresh !== null) $this->channel->appendChild($this->document->createElement('ttl',$refresh));
		if($build !== null) $this->channel->appendChild($this->document->createElement('lastBuildDate',date('r',$build)));
		
		$root->appendChild($this->channel);
	}
	
	/**
	 * Define channel image
	 * @param $title string image title
	 * @param $url string image url
	 * @param $link string image link
	 * @param $height string image description
	 * @param $width string image width
	 * @param $height string image height
	 */
	public function setImage($title,$url,$link,$description=null,$width=null,$height=null) {
		$image = $this->document->createElement('image');
		$image->appendChild($this->document->createElement('url',$url));
		$image->appendChild($this->document->createElement('title',$title));
		$image->appendChild($this->document->createElement('link',$link));
		if($description !== null) $image->appendChild($this->document->createElement('description',$description));
		if($width !== null) $image->appendChild($this->document->createElement('width',$width));
		if($height !== null) $image->appendChild($this->document->createElement('height',$height));
		$this->channel->appendChild($image);
	}
	
	/**
	 * Add an item
	 * @param $id string item id
	 * @param $title string item title
	 * @param $link string item link
	 * @param $description string item description
	 * @param $category string item category
	 * @param $author string item author
	 * @param $publication int item publication timestamp
	 * @param $comments string item comments
	 * @param $source string item source
	 */
	public function addItem($id,$title,$link,$description,$category=null,$author=null,$publication=null,$comments=null,$source=null) {
		$item = $this->document->createElement('item');
		$item->appendChild($this->document->createElement('guid',$id));
		$item->appendChild($this->document->createElement('title',$title));
		$item->appendChild($this->document->createElement('link',$link));
		$item->appendChild($this->document->createElement('description',$description));
		if($category !== null) $item->appendChild($this->document->createElement('category',$category));
		if($author !== null) $item->appendChild($this->document->createElement('author',$author));
		if($publication !== null) $item->appendChild($this->document->createElement('pubDate',date('r',$publication)));
		if($comments !== null) $item->appendChild($this->document->createElement('comments',$comments));
		if($source !== null) $item->appendChild($this->document->createElement('source',$source));
		$this->channel->appendChild($item);
	}
	
	// ToString
	public function __toString() {
		header('content-type:application/rss+xml');
		return $this->document->saveXML();
	}
	
}

/*
// Usage
$rss = new RSS('Test','http://www.example.org','Test RSS','en','120',time());
$rss->setImage('Logo','logo.jpg','http://www.example.org','Website logo');
$rss->addItem('1','Test 1','http://www.example.org','Test item 1','Category 1','Cyril',time(),'Comments','http://www.example.org');
$rss->addItem('2','Test 2','http://www.example.org','Test item 2','Category 1','Cyril',time(),'Comments','http://www.example.org');
$rss->addItem('3','Test 3','http://www.example.org','Test item 3','Category 2','Cyril',time(),'Comments','http://www.example.org');
$rss->addItem('4','Test 4','http://www.example.org','Test item 4','Category 1','Cyril',time(),'Comments','http://www.example.org');
$rss->addItem('5','Test 5','http://www.example.org','Test item 5','Category 3','Cyril',time(),'Comments','http://www.example.org');
$rss->addItem('6','Test 6','http://www.example.org','Test item 6','Category 2','Cyril',time(),'Comments','http://www.example.org');
echo $rss;
//*/