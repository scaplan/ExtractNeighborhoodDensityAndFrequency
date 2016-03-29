/*
*  Spencer Caplan
*  spcaplan@sas.upenn.edu
*  University of Pennsylvania
*/

package chineseData;

import java.io.*;
import java.util.*;

public class ExtractCorpusInfo {
	
	static ArrayList<ChinChar> allCharacters = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> highFreqChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> lowFreqChars = new ArrayList<ChinChar>();
	
	static ArrayList<ChinChar> highFreqTonalLowDensityChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> lowFreqTonalLowDensityChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> highFreqTonalHighDensityChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> lowFreqTonalHighDensityChars = new ArrayList<ChinChar>();
	
	static ArrayList<ChinChar> highFreqVowelLowDensityChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> lowFreqVowelLowDensityChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> highFreqVowelHighDensityChars = new ArrayList<ChinChar>();
	static ArrayList<ChinChar> lowFreqVowelHighDensityChars = new ArrayList<ChinChar>();
	
	static String[] array_HiFr_LoDe_T = {"日", "快", "心", "从", "作", "看", "公", "在", "就", "中"};
	static String[] array_LoFr_LoDe_T = {"鲍", "瞬", "薪", "窃", "葬", "摘", "框", "羞", "裤", "遂"};
	static String[] array_HiFr_HiDe_T = {"烟", "见", "期", "气", "复", "系", "之", "计", "机", "夫"};
	static String[] array_LoFr_HiDe_T = {"咐", "狮", "咽", "荐", "溪", "敷", "隙", "芝", "玑", "於"};
	static String[] array_HiFr_LoDe_V = {"二", "共", "任", "安", "分", "空", "性", "天", "面", "生"};
	static String[] array_LoFr_LoDe_V = {"颂", "翁", "妄", "腕", "氛", "赠", "潘", "坑", "棒", "衫"};
	static String[] array_HiFr_HiDe_V = {"教", "列", "落", "类", "六", "略", "业", "又", "药", "亚"};
	static String[] array_LoFr_HiDe_V = {"寨", "谊", "鹿", "骆", "辣", "廖", "漏", "佑", "钥", "讶"};
	
	static List<String> HiFr_LoDe_T = new ArrayList<String>();
	static List<String> LoFr_LoDe_T = new ArrayList<String>();
	static List<String> HiFr_HiDe_T = new ArrayList<String>();
	static List<String> LoFr_HiDe_T = new ArrayList<String>();
	static List<String> HiFr_LoDe_V = new ArrayList<String>();
	static List<String> LoFr_LoDe_V = new ArrayList<String>();
	static List<String> HiFr_HiDe_V = new ArrayList<String>();
	static List<String> LoFr_HiDe_V = new ArrayList<String>();
	
	static int minFreqCutoff = 3;
	
	public static void main(String[] args) throws IOException {
		
		/*temp
		 * 
		 * Only for using manually specified character lists
		 */
		for (String elem : array_HiFr_LoDe_T) {
			HiFr_LoDe_T.add(elem);
		}
		for (String elem : array_LoFr_LoDe_T) {
			LoFr_LoDe_T.add(elem);
		}
		for (String elem : array_HiFr_HiDe_T) {
			HiFr_HiDe_T.add(elem);
		}
		for (String elem : array_LoFr_HiDe_T) {
			LoFr_HiDe_T.add(elem);
		}
		for (String elem : array_HiFr_LoDe_V) {
			HiFr_LoDe_V.add(elem);
		}
		for (String elem : array_LoFr_LoDe_V) {
			LoFr_LoDe_V.add(elem);
		}
		for (String elem : array_HiFr_HiDe_V) {
			HiFr_HiDe_V.add(elem);
		}
		for (String elem : array_LoFr_HiDe_V) {
			LoFr_HiDe_V.add(elem);
		}
		
		
		String source = "/home1/s/spcaplan/Dropbox/penn_CS_account/chinese-words.txt";
		iterateCorpusData(source);
		
		String vowelSource = "/home1/s/spcaplan/Dropbox/penn_CS_account/chinese-words-vowels.txt";
		addVowelInfo(vowelSource);
		
		String origOutputDirectory = "/home1/s/spcaplan/Dropbox/penn_CS_account/";
		fixOutputData(origOutputDirectory);
		
	   }
	
	private static void addVowelInfo(String dataSourceDirectory) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dataSourceDirectory));
		String origLine;
		while ((origLine = reader.readLine()) != null) {
			String[] origLineTokens = origLine.split(" ");
			if (origLineTokens.length > 3) {
				findCharByWriting(origLineTokens[0]).setVowel(origLineTokens[3]);
			}
		}
		
		reader.close();
	}
	
	
	private static void fixOutputData(String dataSourceDirectory) throws IOException {
		String origDataPath = dataSourceDirectory + "bucketed_output.txt";
		String newOutputDataPath = dataSourceDirectory +  "bucketed_cleaned_output.txt";
		BufferedReader reader = new BufferedReader(new FileReader(origDataPath));
		BufferedWriter writer = new BufferedWriter(new FileWriter(newOutputDataPath));
		String origLine;
		
		while ((origLine = reader.readLine()) != null) {
			String[] origLineTokens = origLine.split("	");
			if (origLineTokens.length > 0) {
				if (origLineTokens[0].equals("Filename")){
					writer.write("\"" + origLineTokens[0] + "\",");
					//"mwbg","talker","vowel","dur"
					writer.write("\"Char\",\"Pinyin\",\"Tone\",\"Vowel\",\"Freq\",\"TonalNeighbors\",\"VowelNeighbors\",\"GroupName\",\"RepNumber\",\"Subject\"");
					for (int i = 1; i < origLineTokens.length; i++) {
						writer.write(",\"" + origLineTokens[i] + "\"");
					}
					writer.write("\n");
				} else {
					String nameToParse = origLineTokens[0];
					nameToParse = nameToParse.substring(4); //remove leading "Sub_"
					int underscoreLoc = nameToParse.indexOf('_');
					int subjectNumber = Integer.parseInt(nameToParse.substring(0, underscoreLoc));
					nameToParse = nameToParse.substring(underscoreLoc + 1);
					int charID = nameToParse.indexOf('_');
					int currCharID = Integer.parseInt(nameToParse.substring(0, charID));
					nameToParse = nameToParse.substring(charID + 1);
					int extLoc = nameToParse.indexOf(".mat");
					int repitition = -1;
					if (extLoc == 0) {
						repitition = 1;
					} else {
						String writenRep = nameToParse.substring(0, extLoc);
						repitition = Integer.parseInt(writenRep) + 1;
					}
					writer.write("\"" + origLineTokens[0] + "\",");
					ChinChar currChinChar = findCharByID(currCharID);
					String groupName = findGroupName(currChinChar.getWrittenForm());
					//"GroupName	RepNumber	Subject"
					writer.write(currChinChar.getWrittenForm() + "," + currChinChar.getPinyin() + "," + currChinChar.getTone() + "," + currChinChar.getVowel() + "," + currChinChar.getFreq() + "," + currChinChar.getNumTonalMinPairs() + "," + currChinChar.getNumVowelMinPairs());
					writer.write("," + groupName + "," + repitition + "," + subjectNumber);
					for (int i = 1; i < origLineTokens.length; i++) {
						writer.write(",\"" + origLineTokens[i] + "\"");
					}
					writer.write("\n");
				}
			}
		}
		
		reader.close();
		writer.close();
		System.out.println("Done");
	}
	
	private static ChinChar findCharByID(int id) {
		for (ChinChar currChar : allCharacters) {
			if (currChar.getID() == id) {
				return currChar;
			}
		}
		return null;
	}
	
	private static ChinChar findCharByWriting(String writtenForm) {
		for (ChinChar currChar : allCharacters) {
			if (currChar.getWrittenForm().equals(writtenForm)) {
				return currChar;
			}
		}
		return null;
	}
	
	private static String findGroupName(String currChar) {

		if (HiFr_LoDe_T.contains(currChar)) {
			return "HiFr_LoDe_T";
		} else if (LoFr_LoDe_T.contains(currChar)) {
			return "LoFr_LoDe_T";
		} else if (HiFr_HiDe_T.contains(currChar)) {
			return "HiFr_HiDe_T";
		} else if (LoFr_HiDe_T.contains(currChar)) {
			return "LoFr_HiDe_T";
		} else if (HiFr_LoDe_V.contains(currChar)) {
			return "HiFr_LoDe_V";
		} else if (LoFr_LoDe_V.contains(currChar)) {
			return "LoFr_LoDe_V";
		} else if (HiFr_HiDe_V.contains(currChar)) {
			return "HiFr_HiDe_V";
		} else if (LoFr_HiDe_V.contains(currChar)) {
			return "LoFr_HiDe_V";
		}
		return "None";
	}
	
	private static void iterateCorpusData(String corpusSource) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(corpusSource));
		String currLine;
		
		while ((currLine = reader.readLine()) != null) {
		//	System.out.println(currLine);
			String[] currLineTokens = currLine.split("	");
			if (currLineTokens.length > 4) {
				int currID = Integer.parseInt(currLineTokens[0]);
				String currWrittenForm = currLineTokens[1];
				int freq = Integer.parseInt(currLineTokens[2]);
				String currPronounce = cleanMultipleReaders(currLineTokens[4]);
				
				if (freq > minFreqCutoff){
					ChinChar newChar = new ChinChar(currID);
					newChar.setFrequency(freq);
					newChar.setPinyin(currPronounce);
					newChar.setWrittenForm(currWrittenForm);
					allCharacters.add(newChar);
				}
				
			}
		}
		
		TreeSet<ChinChar> charactersSortedByTonalNeighborDensity = new TreeSet<ChinChar>(new CharToneNeighborhoodDensitySorter());
		TreeSet<ChinChar> charactersSortedByVowelNeighborDensity = new TreeSet<ChinChar>(new CharVowelNeighborhoodDensitySorter());
	//	System.out.println("Found Num Chars: " + allCharacters.size());
		for (int n = 0; n < allCharacters.size(); n++) {
			ChinChar currChar = allCharacters.get(n);
			for (int x = 0; x < allCharacters.size(); x++) {
				if (x != n){
					ChinChar currCandidate = allCharacters.get(x);
					//check relationship and add to currChar's lists as needed
					if (currChar.getPinyin().equals(currCandidate.getPinyin())){
						if (currChar.getTone() == currCandidate.getTone()) {
							currChar.addHomophone(currCandidate);
						}else{
							currChar.addTonalMinPiar(currCandidate);
						}
					} else {
						if (currChar.getOnset().equals(currCandidate.getOnset())) {
							if (currChar.getTone() == currCandidate.getTone()) {
								if (currChar.getCoda().equals(currCandidate.getCoda())) {
									currChar.addVowelMinPair(currCandidate);
								}
							}
						}
					}
				}
			}
			charactersSortedByTonalNeighborDensity.add(currChar);
			charactersSortedByVowelNeighborDensity.add(currChar);
		}
		
		
		reader.close();
		
		TreeSet<ChinChar> highFreqCharsByTonalNeighborDensity = new TreeSet<ChinChar>(new CharToneNeighborhoodDensitySorter());
		TreeSet<ChinChar> lowFreqCharsByTonalNeighborDensity = new TreeSet<ChinChar>(new CharToneNeighborhoodDensitySorter());
		TreeSet<ChinChar> highFreqCharsByVowelNeighborDensity = new TreeSet<ChinChar>(new CharVowelNeighborhoodDensitySorter());
		TreeSet<ChinChar> lowFreqCharsByVowelNeighborDensity = new TreeSet<ChinChar>(new CharVowelNeighborhoodDensitySorter());
		
		//make high frequency set
		for (int n = 0; n < 1000; n++) {
			ChinChar currChar = allCharacters.get(n);
			
			if ((currChar.getTone() == 1) || (currChar.getTone() == 4)){
				highFreqChars.add(currChar);
				highFreqCharsByTonalNeighborDensity.add(currChar);
				highFreqCharsByVowelNeighborDensity.add(currChar);
			}
		}
		
		//make low frequency set
		for (int n = 2000; n < 3000; n++) {
			ChinChar currChar = allCharacters.get(n);
			
			if ((currChar.getTone() == 1) || (currChar.getTone() == 4)) {
				lowFreqChars.add(currChar);
				lowFreqCharsByTonalNeighborDensity.add(currChar);
				lowFreqCharsByVowelNeighborDensity.add(currChar);
			}
		}
		
			//within each search make high and low density sets
		Iterator<ChinChar> highFreqTonalDensityIterator = highFreqCharsByTonalNeighborDensity.iterator();
		int counter = 0;
		while (highFreqTonalDensityIterator.hasNext()){
			ChinChar currChar = highFreqTonalDensityIterator.next();
			if (counter < 100){
				highFreqTonalLowDensityChars.add(currChar);
			//	System.out.println(currChar);
			}
			if ((highFreqCharsByTonalNeighborDensity.size() - counter) < 100) {
				highFreqTonalHighDensityChars.add(currChar);
				
			}
			counter++;
		}
		
		Iterator<ChinChar> lowFreqTonalDensityIterator = lowFreqCharsByTonalNeighborDensity.iterator();
		counter = 0;
		while (lowFreqTonalDensityIterator.hasNext()){
			ChinChar currChar = lowFreqTonalDensityIterator.next();
			if (counter < 100){
				lowFreqTonalLowDensityChars.add(currChar);
			}
			if ((lowFreqCharsByTonalNeighborDensity.size() - counter) < 100) {
				lowFreqTonalHighDensityChars.add(currChar);
				
			}
			counter++;
		}
		
		Iterator<ChinChar> highFreqVowelDensityIterator = highFreqCharsByVowelNeighborDensity.iterator();
		counter = 0;
		while (highFreqVowelDensityIterator.hasNext()){
			ChinChar currChar = highFreqVowelDensityIterator.next();
			if (counter < 100){
				highFreqVowelLowDensityChars.add(currChar);
			}
			if ((highFreqCharsByVowelNeighborDensity.size() - counter) < 100) {
				highFreqVowelHighDensityChars.add(currChar);
				
			}
			counter++;
		}
		
		Iterator<ChinChar> lowFreqVowelDensityIterator = lowFreqCharsByVowelNeighborDensity.iterator();
		counter = 0;
		while (lowFreqVowelDensityIterator.hasNext()){
			ChinChar currChar = lowFreqVowelDensityIterator.next();
			if (counter < 100){
				lowFreqVowelLowDensityChars.add(currChar);
			}
			if ((lowFreqCharsByVowelNeighborDensity.size() - counter) < 100) {
				lowFreqVowelHighDensityChars.add(currChar);
			}
			counter++;
		}
		
		
		
	//	System.out.println("Done");
	}
	
	
	/*
	 * Convert entries with multiple listed pronounciations into a single pinyin form (return the first one)
	 */
	private static String cleanMultipleReaders(String inputPinyin) {
		int multiPosition = inputPinyin.indexOf('/');
		if (multiPosition > -1) {
			String firstReading = inputPinyin.substring(0, multiPosition);
			return firstReading;
		} else {
			return inputPinyin;
		}
	}


}

	
	