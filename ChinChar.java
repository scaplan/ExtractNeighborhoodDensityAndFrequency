/*
*  Spencer Caplan
*  spcaplan@sas.upenn.edu
*  University of Pennsylvania
*/

package chineseData;

import java.util.ArrayList;
import java.util.Comparator;

public class ChinChar {
	
	static int nextIndexToSet = 0;
	
	private int charID = -1;
	private String writtenForm = "";
	private String pinyin = "";
	private String onset = "";
	private String rhyme = "";
	private String nucleus = "";
	private String coda = "";
	private String vowel = "";
	private int tone = -1;
	private int frequency = -1;
	private double logFreq = -1.0;
	
	private ArrayList<ChinChar> homophones = new ArrayList<ChinChar>();
	private ArrayList<ChinChar> tonalMinPairs = new ArrayList<ChinChar>();
	private ArrayList<ChinChar> vowelMinPairs = new ArrayList<ChinChar>();
	
	static Character[] vowels = {'a', 'i', 'u', 'e', 'o', 'v', ':'};
	
	public ChinChar(int myID) {
		charID = myID;
	}
	
	public String toString() {
		return (writtenForm + " " + pinyin + " " + tone + " Freq: " + frequency + " VowelNeighbors: " + vowelMinPairs.size() + " ToneNeighbors: " + tonalMinPairs.size());
	}
	
	public void setWrittenForm(String myWrittenForm){
		writtenForm = myWrittenForm;
	}
	
	public void addHomophone(ChinChar toAdd) {
		homophones.add(toAdd);
	}
	
	public void addTonalMinPiar(ChinChar toAdd) {
		tonalMinPairs.add(toAdd);
	}
	
	public void addVowelMinPair(ChinChar toAdd) {
		vowelMinPairs.add(toAdd);
	}
	
	public int getNumTonalMinPairs() {
		return tonalMinPairs.size();
	}
	
	public int getNumVowelMinPairs() {
		return vowelMinPairs.size();
	}
	
	public void setPinyin(String myPinyin){
		pinyin = myPinyin;
		int nucleusStartIndex = -1;
		int nucleusEndIndex = -1;
		
		if (containsDigit(pinyin)) {
			tone = Integer.parseInt(pinyin.substring(pinyin.length() - 1, pinyin.length()));
			pinyin = pinyin.substring(0, pinyin.length() - 1);
		} else {
			tone = 0;
		}
		
		for (int n = 0; n < pinyin.length(); n++){
			char currChar = pinyin.charAt(n);
			if (contains(vowels, currChar)){
				nucleusStartIndex = n;
				break;
			}
		}
		
		for (int n = pinyin.length(); n > 0; n--) {
			char currChar = pinyin.charAt(n - 1);
			if (contains(vowels, currChar)){
				nucleusEndIndex = n;
				break;
			}
		}
		
		if (nucleusStartIndex > 0) {
			onset = pinyin.substring(0, nucleusStartIndex);
		}
		
		rhyme = pinyin.substring(nucleusStartIndex, pinyin.length());
		nucleus = pinyin.substring(nucleusStartIndex, nucleusEndIndex);
		coda = pinyin.substring(nucleusEndIndex, pinyin.length());
		
	/*	System.out.println("Pinyin: " + pinyin);
		System.out.println("Onset: " + onset);
		System.out.println("Nucleus: " + nucleus);
		System.out.println("Rhyme: " + rhyme);
		System.out.println("Tone: " + tone);
		System.out.println("Coda: " + coda); */
		
	}
	
	public void setVowel(String toSet) {
		vowel = toSet;
	}
	
	public String getVowel() {
		return vowel;
	}
	
	public String getPinyin() {
		return pinyin;
	}
	
	public int getID() {
		return charID;
	}
	
	public String getWrittenForm() {
		return writtenForm;
	}
	
	public int getTone() {
		return tone;
	}
	
	public String getOnset() {
		return onset;
	}
	
	public String getRhyme() {
		return rhyme;
	}
	
	public String getNucleus() {
		return nucleus;
	}
	
	public String getCoda() {
		return coda;
	}
	
	public int getFreq() {
		return frequency;
	}
	
	public double getLogFreq() {
		return logFreq;
	}
	
	public void setFrequency(int myFrequency){
		frequency = myFrequency;
		logFreq = Math.log(frequency);
	}
	
	public static boolean containsDigit(String s){  
	    boolean containsDigit = false;
	    if(s != null && !s.isEmpty()){
	        for(char c : s.toCharArray()){
	            if(containsDigit = Character.isDigit(c)){
	                break;
	            }
	        }
	    }
	    return containsDigit;
	}
	
	public static <T> boolean contains(final T[] array, final T v) {
	    for (final T e : array)
	        if (e == v || v != null && v.equals(e))
	            return true;

	    return false;
	}


}

class CharFreqComparator implements Comparator<ChinChar> {
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

