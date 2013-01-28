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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * A list of postings for a given word.
 */
public class PostingsList implements Serializable, Comparable<PostingsList> {

	private static final long serialVersionUID = 2230139515028354609L;

	/** The postings list as a linked list. */
	public LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();

	public PostingsList() {

	}

	/*
	 * public PostingsList(LinkedList<PostingsEntry> list) { this.list = list; }
	 */

	/** Number of postings in this list */
	public int size() {
		return list.size();
	}

	/** Returns the ith posting */
	public PostingsEntry get(int i) {
		return list.get(i);
	}

	/** Removes the ith posting */
	private void remove(int i) {
		list.remove(i);
	}

	/** Returns true if the list contains entry with docID */
	private boolean contains(PostingsEntry postingsEntry) {
		for (PostingsEntry p : list)
			if (postingsEntry.equals(p))
				return true;
		return false;
	}

	/** Add docID to the list */
	public void add(int docID, int offset) {
		// TODO score should not be 0;
		PostingsEntry pe = null;
		int i = 0;
		for (PostingsEntry p : list) {
			if (p.docID == docID) {
				pe = p;
				break;
			} else if (p.docID < docID)
				i++;
			else
				break;

		}
		if (pe == null)
			list.add(i, new PostingsEntry(docID, offset, 0));
		else
			pe.addOffset(offset);

	}

	public PostingsEntry getByDocID(int docID) {
		for (PostingsEntry p : list)
			if (p.docID == docID)
				return p;
		return null;
	}

	public static PostingsList removeAllNotIn(PostingsList firstList,
			PostingsList secondList) {
		int i = 0;
		int j = 0;
		PostingsList returnList = new PostingsList();
		while (i < secondList.size() && j < firstList.size()) {
			int secondDoc = secondList.get(i).docID;
			int firstDoc = firstList.get(j).docID;
			if (secondDoc == firstDoc) {
				returnList.add(firstDoc, 0); // Should merge offsets
				i++;
				j++;
			} else if (secondDoc > firstDoc) {
				j++;
			} else if (secondDoc < firstDoc) {
				i++;
			}
		}
		return returnList;
	}

	public static PostingsList removeAllNotFollowedBy(PostingsList firstList,
			PostingsList secondList) {
		int i = 0;
		int j = 0;
		PostingsList returnList = new PostingsList();
		while (i < secondList.size() && j < firstList.size()) {
			int firstDoc = firstList.get(j).docID;
			int secondDoc = secondList.get(i).docID;
			if (secondDoc == firstDoc) {
				PostingsEntry first = firstList.get(j);
				PostingsEntry second = secondList.get(i);
				int ii = 0;
				int jj = 0;
				while (ii < second.offsets.size() && jj < first.offsets.size()) {
					int firstOff = first.offsets.get(jj);
					int secondOff = second.offsets.get(ii);
					if (firstOff == (secondOff - 1)) {
						returnList.add(firstDoc, secondOff);
						ii++;
						jj++;
					} else if (secondOff > firstOff) // Only one separated is
														// captured above
					{
						jj++;
					} else
						ii++;
				}
				i++;
				j++;
			} else if (secondDoc > firstDoc) {
				j++;
			} else if (secondDoc < firstDoc) {
				i++;
			}
		}
		return returnList;
	}

	public static PostingsList phrase_query(PostingsList a, PostingsList b) {
		PostingsList result = new PostingsList();
		int i = 0;
		int j = 0;

		while (i < a.size() && j < b.size()) {
			int ai = a.get(i).docID;
			int bj = b.get(j).docID;
			if (ai == bj) {
				ArrayList<Integer> offsets = PostingsEntry.is_followed_by(
						a.get(i), b.get(j));
				for (int off : offsets) {
					result.add(ai, off);
				}
				i++;
				j++;
			} else if (ai < bj) {
				i++;
			} else {
				j++;
			}
		}
		return result;
	}

	/*
	 * @SuppressWarnings("unchecked") public PostingsList clone() { return new
	 * PostingsList((LinkedList<PostingsEntry>) list.clone()); }
	 */
	@Override
	public int compareTo(PostingsList o) {
		return (int) Math.signum(size() - o.size());
	}

	@Override
	public String toString() {
		return list.toString();
	}

	public void merge(PostingsList otherList) {
		// Collections.sort(thisList.list);
		// Collections.sort(otherList.list);
		int i = 0;
		int j = 0;
		// System.out.println("Lets i " + i);
		while (i < size() && j < otherList.size()) {
			// System.out.println("Lets  " + j);
			if (otherList.get(j).docID < get(i).docID) {
				list.add(i, otherList.get(j));
				i++;
				j++;
			} else if (otherList.get(j).docID > get(i).docID) {
				i++;
			} else {
				i++;
				j++;
			}
		}
		while (j < otherList.size()) {
			list.add(i, otherList.get(j));
			j++;
			i++;
		}

	}

}
