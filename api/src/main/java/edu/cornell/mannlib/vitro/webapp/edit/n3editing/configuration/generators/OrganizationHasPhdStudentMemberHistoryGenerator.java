/* $This file is distributed under the terms of the license in /doc/license.txt$ */
package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

public class OrganizationHasPhdStudentMemberHistoryGenerator extends OrganizationHasMemberHistoryGenerator {

	private final static String URI_DOCTORANT_MEMBERSHIP_CLASS = ehess + "AppartenanceEnTantQueDoctorant";
	private final static String URI_TARGET_ASSOCIATE_CLASS =  ehess + "Doctorant";

	public String getMemberClass() {
		return URI_DOCTORANT_MEMBERSHIP_CLASS;
	}
	public String getTargetMemberClass() {
		return URI_TARGET_ASSOCIATE_CLASS;
	}

	@Override
	protected String getTemplate() {
		return "organizationHasPhdStudentMemberHistory.ftl";
	}
}
