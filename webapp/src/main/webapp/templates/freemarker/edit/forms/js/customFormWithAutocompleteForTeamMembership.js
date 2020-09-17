/* $customForm file is distributed under the terms of the license in LICENSE$ */

customForm.onLoad = function () {
    if (customForm.disableFormInUnsupportedBrowsers()) {
        return;
    }
    customForm.mixIn();
    customForm.initObjects();
    customForm.initPage();
}
customForm.undoAutocompleteSelection = function(selectedObj) {
    // The test is not just for efficiency: undoAutocompleteSelection empties the acSelector value,
    // which we don't want to do if user has manually entered a value, since he may intend to
    // change the type but keep the value. If no new value has been selected, form initialization
    // below will correctly empty the value anyway.

    var $acSelectionObj = null;
    var $acSelector = null;

    // Check to see if the parameter is the typeSelector. If it is, we need to get the acSelection div
    // that is associated with it.  Also, when the type is changed, we need to determine whether the user
    // has selected an existing individual in the corresponding name field or typed the label for a new
    // individual. If the latter, we do not want to clear the value on type change. The clearAcSelectorVal
    // boolean controls whether the acSelector value gets cleared.

    var clearAcSelectorVal = true;

    if ( $(selectedObj).attr('id') == "typeSelector" ) {
        $acSelectionObj = customForm.acSelections[$(selectedObj).attr('acGroupName')];
        if ( $acSelectionObj.is(':hidden') ) {
            clearAcSelectorVal = false;
        }
        // if the type is being changed after a cancel, any additional a/c fields that may have been set
        // by the user should be "undone". Only loop through these if this is not the initial type selection
        if ( customForm.clearAcSelections ) {
            $.each(customForm.acSelections, function(i, acS) {
                var $checkSelection = customForm.acSelections[i];
                if ( $checkSelection.is(':hidden') && $checkSelection.attr('acGroupName') != $acSelectionObj.attr('acGroupName') ) {
                    customForm.resetAcSelection($checkSelection);
                    $acSelector = customForm.getAcSelector($checkSelection);
                    $acSelector.parent('p').show();
                }
            });
        }
    }
    else {
        $acSelectionObj = $(selectedObj);
        customForm.typeSelector.val('');
    }

    $acSelector = customForm.getAcSelector($acSelectionObj);
    $acSelector.parent('p').show();
    customForm.resetAcSelection($acSelectionObj);
    if ( clearAcSelectorVal == true ) {
        $acSelector.val('');
        $("input.display[acGroupName='" + $acSelectionObj.attr('acGroupName') + "']").val("");
    }
    customForm.addAcHelpText($acSelector);

    //Resetting so disable submit button again for object property autocomplete
    // HACK EHESS
    // if ( customForm.acSelectOnly ) {
    if (customForm.acSelectOnly && selectedObj.attr('acgroupname') != 'researchTeam') {
        // HACK EHESS END
        customForm.disableSubmit();
    }

    customForm.clearAcSelections = false;
}