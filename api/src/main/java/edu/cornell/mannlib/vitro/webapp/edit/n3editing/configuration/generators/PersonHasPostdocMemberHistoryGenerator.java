/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

public class PersonHasPostdocMemberHistoryGenerator extends PersonHasResearchUnitAndTeamMembershipHistoryGenerator {


    final static String specificResearchUnitMembershipLabel = "\"Appartenance en tant que postdoctorant à l'unité de recherche\"@fr , \"Research Unit Membership As Postdoc\"@en";
    final static String specificTeamMembershipLabel = "\"Appartenance en tant que postdoctorant à l'équipe\"@fr , \"Team Membership As Postdoc\"@en";
    final static String postdocMembershipClass = ehess + "AppartenanceEnTantQuePostDoctorant";
    final static String entryTypeForTitle = "postdoc_membership_entry_for";

    public PersonHasPostdocMemberHistoryGenerator() {
    }

    @Override
    String getSpecificResearchUnitMembershipLabel() {
        return specificResearchUnitMembershipLabel;
    }

    @Override
    String getSpecificTeamMembershipLabel() {
        return specificTeamMembershipLabel;
    }

    @Override
    String getSpecificMembershipClass() {
        return postdocMembershipClass;
    }

    @Override
    String getEntryTypeForTitle() {
        return entryTypeForTitle;
    }

}