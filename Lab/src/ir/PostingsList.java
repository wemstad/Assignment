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
 *   A list of postings for a given word.
 */
public class PostingsList implements Serializable {
    
    /** The postings list as a linked list. */
    private LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();


    /**  Number of postings in this list  */
    public int size() {
	return list.size();
    }

    /**  Returns the ith posting */
    public PostingsEntry get( int i ) {
	return list.get( i );
    }

    /**  Removes the ith posting */
    private void remove(int i) {
    	list.remove(i);
    }
    
    /** Returns true if the list contains entry with docID */
    private boolean contains(PostingsEntry postingsEntry) {
    	for(PostingsEntry p : list)
    		if(postingsEntry.equals(p))
    			return true;
    	return false;
    }
    
    
    /** Add docID to the list */
    public void add(int docID) {
    	// TODO score will not be 0;
    	PostingsEntry pe = new PostingsEntry(docID, 0);
    	if(!list.contains(pe))
    			list.add(pe);
    }

	public void removeAllNotIn(PostingsList otherList) {
		// TODO Naive implemention: REFACTOR
		for(int i = 0; i < size();){
			if(!otherList.contains(get(i)))
				remove(i);
			else
				i++;
		}
	}



}
	

			   
