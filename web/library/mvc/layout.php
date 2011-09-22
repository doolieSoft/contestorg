<?php

/**
 * Layout class
 */
class Layout extends View
{
	/** @var $view View view */
	private $view;

	/** @var $content string view rendering */
	protected $content;

	/**
	 * Constructor
	 * @param $path string layout path
	 * @param $view View view
	 */
	public function __construct(View $view)
	{
		// Save view
		$this->view = $view;
	}
	
	/**
	 * @see View::__get()
	 */
	public function __get($name) {
		// Get value from view
		return $this->view->$name;
	}

	/**
	 * @see View::__set()
	 */
	public function __set($name,$value)
	{
		// Call parent __set() method
		parent::__set($name, $value);

		// Send value to view
		$this->view->$name = $value;
	}

	/**
	 * @see View::render()
	 */
	public function render(Request $request,$layoutPath,$viewPath)
	{
		// Start view buffering
		ob_start();

		// Render view
		$this->view->render($request,$viewPath);

		// Save view buffering
		$this->content = ob_get_clean();
		
		// Render layout
		parent::render($request,$layoutPath);
	}
}