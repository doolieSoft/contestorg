// Quand le DOM de la page est prêt
$(document).ready(function() {
	// Images fancybox
	$('a.fancybox').fancybox({
		transitionIn:'elastic',
		transitionOut:'elastic',
		titlePosition:'over'
	});

	// Présentation youtube
	$('#youtube').fancybox({
		transitionIn:'elastic',
		transitionOut:'elastic',
		type:'swf',
		'swf':{'wmode':'transparent','allowfullscreen':'true'},
		titleShow:false,
		width:640,
		height:390
	});
});

// Google analytics
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-9937933-7']);
_gaq.push(['_trackPageview']);

(function() {
  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();