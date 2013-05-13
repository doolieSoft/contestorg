<?php

/**
 * Interface to implement to be considered as a form renderer
 */
interface IFormRenderer
{

	/**
	 * Render a form in HTML
	 * @param $form Form form
	 * @return string form in HTML
	 */
	public function renderForm(Form $form);

}

/**
 * Abstract form renderer
 */
abstract class AbstractFormRenderer implements IFormRenderer
{
	/**
	 * @see IFormRenderer#renderForm(Form)
	 */
	public function renderForm(Form $form)
	{
		// Form start
		$string = $form->getStart()."\n";
		
		// Display form error
		if(($error = $form->getError($form)) != null) {
			$string .= $error."\n";
		}
		
		// Display hidden elements
		foreach($form->getElements() as $element) {
			if($element instanceof FormHidden) {
				$string .= $this->renderElement($form,$element);
			}
		}
		
		// Group elements
		$groups = array();
		foreach($form->getElements() as $element) {
			// Add element to group
			if(!($element instanceof FormHidden) && !($element instanceof FormSequence) && !($element instanceof FormSubmit) && ($names = $form->getGroup($element)) !== null) {
				$group =& $groups;
				while(count($names)) {
					$name = array_shift($names);
					if(!isset($group[$name])) {
						$group[$name] = array();
					}
					$group =& $group[$name];
				}
				$group[] = $element;
			}
		}
		
		// List started ?
		$startedList = false;
		
		// Display elements
		$done = array();
		foreach($form->getElements() as $element) {
			if(!($element instanceof FormHidden) && !($element instanceof FormSequence) && !($element instanceof FormSubmit)) {
				// Display element or group
				if(($names = $form->getGroup($element)) === null) {
					// Start list
					if(!$startedList) {
						$string .= $this->getListStart();
						$startedList = true;
					}
					
					// Display element
					$string .= $this->renderElement($form,$element);
				} else {
					// Get group name
					$name = array_shift($names);
					
					if(!in_array($name,$done)) {
						// Close list
						if($startedList) {
							$string .= $this->getListEnd();
							$startedList = false;
						}
						
						// Display group
						$string .= $this->renderGroup($form,$name,$groups[$name]);
						           
						// Group is done
						$done[] = $name;
					}
				}
			}
		}
		if($startedList) {
			$string .= $this->getListEnd();
		}
		
		// Display submit elements
		foreach($form->getElements() as $element) {
			if($element instanceof FormSubmit) {
				$string .= $this->renderElement($form,$element).' ';
			}
		}
		
		// Form end
		$string .= $form->getEnd()."\n";
		
		// Return string
		return $string;
	}
	
	/**
	 * Render a group in HTML
	 * @param $form Form form
	 * @param $name string group name
	 * @param $elements array goup elements
	 * @return string group in HTML
	 */
	private function renderGroup($form,$name,$elements)
	{
		// Init string
		$string = '';
		
		// Fieldset
		if(is_string($name)) {
			$string .= '<fieldset>'."\n".'<legend>'.$name.'</legend>'."\n"; 
		}
		
		// List started ?
		$startedList = false;
		           
		// Display group elements
		foreach($elements as $key => $element) {
			// Display element or group
			if(is_array($element)) {
				// Close list
				if($startedList) {
					$string .= $this->getListEnd();
					$startedList = false;
				}
				
				// Display group
				$string .= $this->renderGroup($form,$key,$element);
			} else {
				// Start list
				if(!$startedList) {
					$string .= $this->getListStart();
					$startedList = true;
				}
				
				// Display element
				$string .= $this->renderElement($form,$element,is_string($name));
			}
		}
		if($startedList) {
			$string .= $this->getListEnd();
		}
		
		// Fieldset
		if(is_string($name)) {
			$string .= '</fieldset>'."\n";
		}
		
		// Return string
		return $string;
	}
	
	/**
	 * Render an element in HTML
	 * @param $form Form form
	 * @param $element FormElement element
	 * @return string element in HTML
	 */
	abstract protected function renderElement(Form $form,FormElement $element);
	
	/**
	 * Get list start
	 * @return string list start
	 */
	abstract protected function getListStart();
	
	/**
	 * Get list end
	 * @return string list end
	 */
	abstract protected function getListEnd();
}

/**
 * Form renderer with table
 */
class FormRendererTable extends AbstractFormRenderer
{

	/**
	 * @see AbstractFormRenderer#renderElement(Form,FormElement)
	 */
	protected function renderElement(Form $form,FormElement $element)
	{
		return '<tr>'.
					($element->getDescription() != null ? '<td style="vertical-align:top;">'.$element->getDescription().' :</td>' : '').
					'<td'.($element->getDescription() == null ? ' colspan="2"' : '' ).'>'.$element->__toString().' '.$form->getError($element).'</td>'.
			   '</tr>'."\n";
	}
	
	/**
	 * @see AbstractFormRenderer#getListStart()
	 */
	protected function getListStart()
	{
		return '<table>'."\n";
	}
	
	/**
	 * @see AbstractFormRenderer#getListEnd()
	 */
	protected function getListEnd()
	{
		return '</table>'."\n";
	}
	
	
}

/**
 * Form renderer with labels
 */
class FormRendererLabels extends AbstractFormRenderer
{
	/**
	 * @see AbstractFormRenderer#renderElement(Form,FormElement)
	 */
	protected function renderElement(Form $form,FormElement $element)
	{
		return '<div>'.
					($element->getDescription() != null ? '<label>'.$element->getDescription().'&nbsp;:&nbsp;</label> ' : '').
					$element->__toString().' '.$form->getError($element).
			   '</div>'."\n";
	}

	/**
	 * @see AbstractFormRenderer#getListStart()
	 */
	protected function getListStart()
	{
		return '';
	}
	
	/**
	 * @see AbstractFormRenderer#getListEnd()
	 */
	protected function getListEnd()
	{
		return '';
	}
}