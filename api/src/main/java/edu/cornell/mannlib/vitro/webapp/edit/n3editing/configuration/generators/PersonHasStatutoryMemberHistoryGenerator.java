/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

public class PersonHasStatutoryMemberHistoryGenerator extends PersonHasResearchUnitAndTeamMembershipHistoryGenerator {


    final static String specificResearchUnitMembershipLabel = "\"Appartenance statutaire à l'unité de recherche\"@fr , \"Research Unit Statutory Membership\"@en";
    final static String specificTeamMembershipLabel = "\"Appartenance statutaire à l'équipe\"@fr , \"Team Statutory Membership\"@en";
    final static String statutoryMembershipClass = ehess + "AppartenanceStatutaire";
    final static String membershipLabel = "\"Appartenance\"@fr , \"Membership\"@en";
    final static String entryTypeForTitle = "statutory_membership_entry_for";

    public PersonHasStatutoryMemberHistoryGenerator() {
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
        return statutoryMembershipClass;
    }

    @Override
    String getEntryTypeForTitle() {
        return entryTypeForTitle;
    }

}
