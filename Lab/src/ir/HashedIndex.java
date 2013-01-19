/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012
 *   Finalized by: Christian Wemstad, 2013
 */

package ir;

import java.util.HashMap;

/**
 * Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

	/** The index as a hashtable. */
	private HashMap<String, PostingsList> index = new HashMap<String, PostingsList>();

	/**
	 * Inserts this token in the index.
	 */
	public void insert(String token, int docID, int offset) {
		PostingsList list = index.get(token);
		if (list == null) {
			list = new PostingsList();
			index.put(token, list);
		}
		list.add(docID, offset);
	}

	/**
	 * Returns the postings for a specific term, or null if the term is not in
	 * the index.
	 */
	public PostingsList getPostings(String token) {
		PostingsList list = index.get(token);
		if (list == null || list.size() == 0)
			return null;
		return list;
	}

	/**
	 * Searches the index for postings matching the query.
	 */
	public PostingsList search(Query query, int queryType, int rankingType) {
		
		PostingsList all = getPostings(query.terms.getFirst()).clone();
		
		if (queryType == Index.INTERSECTION_QUERY) {
			for (int i = 1; i < query.terms.size(); i++) {
				PostingsList currentList = getPostings(query.terms.get(i));
				all.removeAllNotIn(currentList);
			}
			return all;
		}
		else if (queryType == Index.PHRASE_QUERY) {
			for (int i = 1; i < query.terms.size(); i++) {
				PostingsList currentList = getPostings(query.terms.get(i));
				all.removeAllNotFollowedBy(currentList, i);
			}
			return all;
		}
		return 
			null;
	}

	/**
	 * No need for cleanup in a HashedIndex.
	 */
	public void cleanup() {
	}
}
