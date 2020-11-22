/* $This file is distributed under the terms of the license in LICENSE$ */

// Change form actions in account main page

$(document).ready(function(){

    // The externalAuthOnly checkbox drives the display of the password and re-set
    // password fields. When checked, the password fields are hidden
	$.fn.keepLabelAction = function(checked){ 
		if ( checked ) {
	         // If checked, hide those puppies
	            $('#positionTitleContainer').addClass('hidden');
	        // And clear any values entered in the password fields
	            $('input[name=positionTitle]').val("");
	         }
	         else {
	         // if not checked, display them
	            $('#positionTitleContainer').removeClass('hidden');
	         }
	}
	
	
	if( !$('input[name=positionTitle]').val() ) {
		$('input[name=keepLabel]').prop( "checked", true );
	} else {
		$('input[name=keepLabel]').prop( "checked", false );
	}
	$.fn.keepLabelAction($('input[name=keepLabel]').prop('checked'));
	
	
	
	$('input:checkbox[name=keepLabel]').click(function(){
		 $.fn.keepLabelAction(this.checked );
    });

});
