/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Finalized by: Christian Wemstad, 2013
 */

package ir;

import java.util.LinkedList;
import java.io.Serializable;

/**
 * A list of postings for a given word.
 */
public class PostingsList implements Serializable {

	private static final long serialVersionUID = 2230139515028354609L;

	/** The postings list as a linked list. */
	private LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();

	public PostingsList() {

	}

	public PostingsList(LinkedList<PostingsEntry> list) {
		this.list = list;
	}

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
		// TODO score will not be 0;
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

	public void removeAllNotIn(PostingsList otherList) {
		int i = 0;
		int j = 0;
		while (i < size() && j < otherList.size()) {
			int thisDoc = get(i).docID;
			int otherDoc = otherList.get(j).docID;
			if (thisDoc == otherDoc) {
				i++;
				j++;
			} else if (thisDoc > otherDoc) {
				j++;
			} else if (thisDoc < otherDoc) {
				remove(i);
			}
		}
		while (i < size())
			remove(i);
	}

	public void removeAllNotFollowedBy(PostingsList otherList, int differOffset) {
		// TODO Naive implementation: REFACTOR
		for (int i = 0; i < size();) {
			PostingsEntry toCheck = get(i);
			if (!otherList.contains(toCheck))
				remove(i);
			else {
				PostingsEntry otherEntry = otherList.getByDocID(toCheck.docID)
						.clone();

				for (int j = 0; j < toCheck.offsets.size();) {
					int offset = toCheck.offsets.get(j);
					if (!otherEntry.offsets.contains(offset + differOffset))
						toCheck.offsets.remove((Integer) offset);
					else
						j++;
				}
				if (toCheck.offsets.isEmpty())
					remove(i);
				else
					i++;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public PostingsList clone() {
		return new PostingsList((LinkedList<PostingsEntry>) list.clone());
	}

}
