/*  Fonction de scroll automatique de haut en bas et de bas en haut */
var _scrollit_dir = 'down';
var _scrollit_nb = 0;
var _scrollit_speed = 80;
var _scrollit_wait = 3000;
var _scrollit_refresh = 1;
var _scrollit_step = 1;
function scrollit() {
	if(document.body.clientHeight != document.body.scrollHeight) {
		if(_scrollit_dir == 'down') {
			window.scrollBy(0,+_scrollit_step);  
			if(document.body.scrollTop + document.body.clientHeight > document.body.scrollHeight - 5) {
				_scrollit_dir = 'up';
				setTimeout('scrollit()', _scrollit_wait);
			} else {
				setTimeout('scrollit()', _scrollit_speed);
			}
		} else {
			window.scrollBy(0,-_scrollit_step);
			if(document.body.scrollTop == 0) {
				_scrollit_dir = 'down';
				_scrollit_nb++;
				if(_scrollit_nb >= _scrollit_refresh) {
					location.reload(true);
				} else {
					setTimeout('scrollit()', _scrollit_wait);
				}
			} else {
				setTimeout('scrollit()', _scrollit_speed);
			}
		}
	} else {
		setTimeout(function() {
			location.reload(true);
		}, 10000);
	}
}