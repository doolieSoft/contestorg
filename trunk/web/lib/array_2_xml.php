<?php

/**
* PHP tool to transform associative array to XML
* @author Cyril Perrin
* @license LGPL v3
* @version 2012-03-19
*/

/** 
 * T
 * @param $root string root node name
 * @param $array array array to transform
 * @return string XML
 */  
function array_2_xml($root, $array) {  
    // Create document
    $document = new DOMDocument('1.0', 'utf-8');  
      
    // Create root node
    $root = $document->createElement($root);  
    $document->appendChild($root);  
      
    // Fill document
    foreach ($array as $key => $value) {
    	if(is_int($key)) {
    		// Create and add node
    		$root->appendChild(create_node($document,array_shift($value),$value));
    	} else if (substr($key,0,1) == '@') {  
            // Add attribute
            $root->setAttribute(substr($key,1),$value);  
        } else {  
            // Create and add node
            $root->appendChild(create_node($document,$key,$value));  
        }  
    }  
      
    // Return XML
    return $document->saveXML();  
}  
  
/** 
 * Create node
 * @param $document DOMDocument document 
 * @param $key string node name
 * @param $value string node value
 * @return DOMElement node
 */  
function create_node(DOMDocument $document, $key, $value) {
    // Create node
    $node = $document->createElement($key,is_array($value) ? null : $value);  
    
    // Add sub-nodes
    if (is_array($value)) {  
        foreach ($value as $key_2 => $value_2) {  
            if(is_int($key_2)) {
	    		// Create and add node
	    		$node->appendChild(create_node($document,array_shift($value_2),$value_2));
	    	} else if (substr($key_2,0,1) == '@') {  
                // Add attribute
                $node->setAttribute(substr($key_2,1),$value_2);  
            } else {  
                // Create and add sub-node
                $node->appendChild(create_node($document,$key_2,$value_2));  
            }  
        }  
    }  
      
    // Return node
    return $node;  
}