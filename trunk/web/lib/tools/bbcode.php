<?php

/**
 * Function to apply BBCode
 * @author Cyril Perrin
 * @license LGPL v3 
 * @version 2011-05-07
 * @param $text string initial string with BBCode
 * @param $level int maximal headers level
 * @param $class string div class surrounding BBCode
 */
function bbcode($text,$level=0,$class='bbcode') {
	// Headers
	$text = str_replace('[h4]','<h'.(4+$level).'>',$text);
	$text = str_replace('[/h4]','</h'.(4+$level).'>',$text);
	$text = str_replace('[h3]','<h'.(3+$level).'>',$text);
	$text = str_replace('[/h3]','</h'.(3+$level).'>',$text);
	$text = str_replace('[h2]','<h'.(2+$level).'>',$text);
	$text = str_replace('[/h2]','</h'.(2+$level).'>',$text);
	$text = str_replace('[h1]','<h'.(1+$level).'>',$text);
	$text = str_replace('[/h1]','</h'.(1+$level).'>',$text);
	
	// Bold texts
	$text = str_replace('[b]','<b>',$text);
	$text = str_replace('[/b]','</b>',$text);
	
	// Italic texts
	$text = str_replace('[i]','<i>',$text);
	$text = str_replace('[/i]','</i>',$text);
	
	// Underline texts
	$text = str_replace('[u]','<u>',$text);
	$text = str_replace('[/u]','</u>',$text);
	
	// Strikes texts
	$text = str_replace('[s]','<s>',$text);
	$text = str_replace('[/s]','</s>',$text);
	
	// Colored texts
	$text = preg_replace('/\[color=(.*?)\](.*?)\[\/color\]/','<span style="color:$1;">$2</span>',$text);
	
	// Links
	$text = preg_replace('/\[url\](.*?)\[\/url\]/','<a href="$1">$1</a>',$text);
	$text = preg_replace('/\[url=(.*?)\](.*?)\[\/url\]/','<a href="$1">$2</a>',$text);
	
	// Emails
	$text = preg_replace('/\[email\](.*?)\[\/email\]/','<a href="mailto:$1">$1</a>',$text);
	$text = preg_replace('/\[email=(.*?)\](.*?)\[\/email\]/','<a href="mailto:$1">$2</a>',$text);
	
	// Images
	$text = preg_replace('/\[img\](.*?)\[\/img\]/','<img src="$1" alt="#" />',$text);
	
	// Codes
	$text = str_replace('[code]','<code>',$text);
	$text = str_replace('[/code]','</code>',$text);
	
	// Quotes
	$text = preg_replace('/\[quote\](.*?)\[\/quote\]/','<q>$1</q>',$text);
	$text = preg_replace('/\[quote="?(.*?)"?\](.*?)\[\/quote\]/','$1 : <q>$2</q>',$text);
	
	// Alignements
	$text = str_replace('[left]','<p style="text-align:left;">',$text);
	$text = str_replace('[/left]','</p>',$text);
	$text = str_replace('[center]','<p style="text-align:center;">',$text);
	$text = str_replace('[/center]','</p>',$text);
	$text = str_replace('[right]','<p style="text-align:right;">',$text);
	$text = str_replace('[/right]','</p>',$text);
	
	// Paragraphs
	$text = str_replace('[p]','<p>',$text);
	$text = str_replace('[/p]','</p>',$text);
	
	// Spoilers
	$text = str_replace('[spoiler]','<div class="spoiler"><p><a href="#" onclick="if(this.innerHTML != \'Cliquez pour afficher\') { this.innerHTML = \'Cliquez pour afficher\'; this.parentNode.parentNode.childNodes[1].style.display = \'none\'; } else {  this.innerHTML = \'Cliquez pour masquer\'; this.parentNode.parentNode.childNodes[1].style.display = \'block\'; } return false;">Cliquez pour afficher</a></p><div style="display:none;">',$text);
	$text = preg_replace('/\[spoiler="(.*?)"\]/','<div class="spoiler"><p><a href="#" onclick="if(this.innerHTML != \'$1 (cliquez pour afficher)\') { this.innerHTML = \'$1 (cliquez pour afficher)\'; this.parentNode.parentNode.childNodes[1].style.display = \'none\'; } else {  this.innerHTML = \'$1 (cliquez pour masquer)\'; this.parentNode.parentNode.childNodes[1].style.display = \'block\'; } return false;">$1 (cliquez pour afficher)</a></p><div style="display:none;">',$text);
	$text = str_replace('[/spoiler]','</div></div>',$text);
	
	// Lines
	$text = str_replace('[hr]','<hr/>',$text);
	
	// Tooltips
	$text = preg_replace('/\[tooltip\](.*?)\[\/tooltip\]/','<span title="$1">(?)</span>',$text);
	
	// Indices/Exposants
	$text = str_replace('[sub]','<sub>',$text);
	$text = str_replace('[/sub]','</sub>',$text);
	$text = str_replace('[sup]','<sup>',$text);
	$text = str_replace('[/sup]','</sup>',$text);
	
	// Lists
	$text = preg_replace('/\[list\](.*?)\[\/list\]/s','<ul>$1</ul>',$text);
	$text = preg_replace('/\[list=1\](.*?)\[\/list\]/s','<ol style="list-style-type:decimal;">$1</ol>',$text);
	$text = preg_replace('/\[list=a\](.*?)\[\/list\]/s','<ol style="list-style-type:lower-alpha;">$1</ol>',$text);
	$text = preg_replace('/\[\*\](.*)/','<li>$1</li>',$text);
	
	// Flash objects
	$text = preg_replace('/\\[embed-flash\(([0-9]+),([0-9]+)\)\]/','<embed type="application/x-shockwave-flash" width="$1" height="$2" allowFullScreen="true" allowScriptAccess="always" src="',$text);
	$text = str_replace('[/embed-flash]','"></embed>',$text);
	
	// Iframes
	$text = preg_replace('/\\[iframe\(([0-9]+),([0-9]+)\)\]/','<iframe width="$1" height="$2" frameborder="0" src="',$text);
	$text = str_replace('[/iframe]','"></iframe>',$text);
	
	// Remove some new lines
	$tags = array('li','[uo]l','h[0-9]+','p','div','hr');
	foreach($tags as $tag) {
		$text = preg_replace('/\s*\<('.$tag.')/','<$1',$text);
		$text = preg_replace('/\<\/('.$tag.')\>\s*/','</$1>',$text);
		$text = preg_replace('/\s*\<'.$tag.'\/\>\s*/','<'.$tag.'/>',$text);
	}
	
	// Surround text
	if($class !== null) {
		$text = '<div class="'.$class.'">'.$text.'</div>';
	}
	
	// Return text
	return str_replace("\n",'<br/>',$text);
}