/*
*  Spencer Caplan
*  spcaplan@sas.upenn.edu
*  University of Pennsylvania
*/

package chineseData;

import java.util.Comparator;

public class CharToneNeighborhoodDensitySorter implements Comparator<ChinChar>{
	
	@Override
	public int compare(ChinChar charOne, ChinChar charTwo) {
		
		if (charOne.getNumTonalMinPairs() > charTwo.getNumTonalMinPairs()){
			return 1;
		} else if (charOne.getNumTonalMinPairs() < charTwo.getNumTonalMinPairs()) {
			return -1;
		} else {
			return 0;
		}
		
	}

}
