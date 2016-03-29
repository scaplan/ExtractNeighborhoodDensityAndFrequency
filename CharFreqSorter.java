/*
*  Spencer Caplan
*  spcaplan@sas.upenn.edu
*  University of Pennsylvania
*/

package chineseData;

import java.util.Comparator;

public class CharFreqSorter implements Comparator<ChinChar>{

	@Override
	public int compare(ChinChar charOne, ChinChar charTwo) {
		
		if (charOne.getFreq() > charTwo.getFreq()){
			return 1;
		} else if (charOne.getFreq() < charTwo.getFreq()) {
			return -1;
		} else {
			return 0;
		}
		
	}

}
