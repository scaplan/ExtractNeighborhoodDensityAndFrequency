/*
*  Spencer Caplan
*  spcaplan@sas.upenn.edu
*  University of Pennsylvania
*/

package chineseData;

import java.util.Comparator;

public class CharVowelNeighborhoodDensitySorter implements Comparator<ChinChar>{
	
	@Override
	public int compare(ChinChar charOne, ChinChar charTwo) {
		
		if (charOne.getNumVowelMinPairs() > charTwo.getNumVowelMinPairs()){
			return 1;
		} else if (charOne.getNumVowelMinPairs() < charTwo.getNumVowelMinPairs()) {
			return -1;
		} else {
			return 0;
		}
		
	}

}
