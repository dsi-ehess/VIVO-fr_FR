/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

public class PersonHasPhdStudentMemberHistoryGenerator extends PersonHasMemberHistoryGenerator {

    final static String phdStudentMemberClass = ehess + "AppartenanceEnTantQueDoctorant";

    final static String phdStudentClass = ehess + "Doctorant";

    @Override
    public EditConfigurationVTwo getEditConfiguration(VitroRequest vreq, HttpSession session) throws Exception {
        EditConfigurationVTwo conf = super.getEditConfiguration(vreq, session);
        conf.setN3Required(Arrays.asList(getN3ForNewPosition(), n3ForExistingOrg, getN3ForAssociatedType()));
        return conf;
    }

    @Override
    public String getAssociateClass() {
        return phdStudentMemberClass;
    }

    @Override
    public String getTemplate() {
        return "personHasPhdStudentMemberHistory.ftl";
    }

    @Override
    public String getAssociateClassLabel() {
        return "\"Appartenance en tant que doctorant\"@fr , \"Membership as Phd Student\"@en";
    }

    private String getN3ForAssociatedType() {
        return "?person a  <" + getAssociatedTypeClass() + "> . ";
    }

    public String getAssociatedTypeClass() {
        return phdStudentClass;
    }

}
