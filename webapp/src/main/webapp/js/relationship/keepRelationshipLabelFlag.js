/* $This file is distributed under the terms of the license in LICENSE$ */

$(document).ready(function() {
	
	var relType = customFormData['relType'];

	$.fn.updateCustomLabelBehavior = function() {
		var $checkBox = $('input:checkbox[name=keepLabel]');
		var checked = $checkBox.is(":checked");
		var relationshipType = $('select[name='+relType+'Type]').val();
		var customLabelIsMandatory = false;
		$(".generic-"+relType+"-class-indicator").each(function(i, e) {
			// User as selected on of the generic relationship types :
			if ($(e).val() == relationshipType) {
				customLabelIsMandatory = true;
			}
		});
		if (customLabelIsMandatory) {
			$checkBox.prop("checked", false).prop("disabled", true);
			$('label[for="'+relType+'Title"] .requiredHint').show();
		} else {
			$checkBox.prop("disabled", false);
			$('label[for="'+relType+'Title"] .requiredHint').hide();
		}

	}	

	$.fn.updateCustomLabelBehavior();

	$('select[name='+relType+'Type]').change(function() {
		$.fn.updateCustomLabelBehavior();
	});

});
