/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Finalized by: Christian Wemstad, 2013
 */

package ir;

import java.io.Serializable;

public class PostingsEntry implements Comparable<PostingsEntry>, Serializable {

	private static final long serialVersionUID = 8415974529423644037L;
	public int docID;
	public double score;

	public PostingsEntry(int docID, double score) {
		this.docID = docID;
		this.score = score;
	}

	/**
	 * PostingsEntries are compared by their score (only relevant in ranked
	 * retrieval).
	 * 
	 * The comparison is defined so that entries will be put in descending
	 * order.
	 */
	public int compareTo(PostingsEntry other) {
		return Double.compare(other.score, score);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof PostingsEntry)
			return docID == ((PostingsEntry) other).docID;
		return false;
	}

	@Override
	public int hashCode() {
		System.err.println("ERROR: USING hashcode()!!!!!!!!!!");
		return 0;
	}

}
