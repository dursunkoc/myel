/**
 *
 */
package com.aric.myel.statements;

import java.util.Comparator;

/**
 * @author Dursun KOC
 *
 */
public class StatementComplexityComparator implements Comparator<Statement> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Statement o1, Statement o2) {
		long i = o1.getComplexityLevel();
		long j = o2.getComplexityLevel();
		if (i == j) {
			return 0;
		}
		return new Long((i - j) / (Math.abs(i - j))).intValue();
	}

}
