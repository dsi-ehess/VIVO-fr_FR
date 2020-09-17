/* $This file is distributed under the terms of the license in /doc/license.txt$ */
package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

public class OrganizationHasPostdocMemberHistoryGenerator extends OrganizationHasMemberHistoryGenerator {

	private final static String URI_POSTDOC_MEMBERSHIP_CLASS = ehess + "AppartenanceEnTantQuePostDoctorant";
	private final static String URI_TARGET_ASSOCIATE_CLASS =  ehess + "PostDoctorant";

	public String getMemberClass() {
		return URI_POSTDOC_MEMBERSHIP_CLASS;
	}
	public String getTargetMemberClass() {
		return URI_TARGET_ASSOCIATE_CLASS;
	}

	@Override
	protected String getTemplate() {
		return "organizationHasPostdocMemberHistory.ftl";
	}

}
