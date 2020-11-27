/* $This file is distributed under the terms of the license in LICENSE$ */

$(document).ready(function() {

	$.fn.updateCustomLabelBehavior = function() {
		var $checkBox = $('input:checkbox[name=keepLabel]');
		var checked = $checkBox.is(":checked");
		var positionType = $('select[name=positionType]').val();
		var customLabelIsMandatory = false;
		$(".generic-position-class-indicator").each(function(i, e) {
			// User as selected on of the generic position types :
			if ($(e).val() == positionType) {
				customLabelIsMandatory = true;
			}
		});
		if (customLabelIsMandatory) {
			$checkBox.prop("checked", false).prop("disabled", true);
			$('label[for="positionTitle"] .requiredHint').show();
		} else {
			$checkBox.prop("disabled", false);
			$('label[for="positionTitle"] .requiredHint').hide();
		}

	}

	$.fn.updateCustomLabelBehavior();

	$('select[name=positionType]').change(function() {
		$.fn.updateCustomLabelBehavior();
	});

});
