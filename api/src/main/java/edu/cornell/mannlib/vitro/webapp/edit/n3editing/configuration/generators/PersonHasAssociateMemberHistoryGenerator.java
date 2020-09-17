/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

public class PersonHasAssociateMemberHistoryGenerator extends PersonHasResearchUnitAndTeamMembershipHistoryGenerator {


    final static String specificResearchUnitMembershipLabel = "\"Appartenance à l'unité de recherche à titre d'associé\"@fr , \"Associate Research Unit Membership\"@en";
    final static String specificTeamMembershipLabel = "\"Appartenance à l'équipe à titre d'associé\"@fr , \"Associate Team Membership\"@en";

    final static String associateMembershipClass = ehess + "AppartenanceATitreDAssocie";
    final static String entryTypeForTitle = "associate_membership_entry_for";

    public PersonHasAssociateMemberHistoryGenerator() {
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
        return associateMembershipClass;
    }

    @Override
    String getEntryTypeForTitle() {
        return entryTypeForTitle;
    }

}
