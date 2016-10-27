package citycircle.com.OA.uitls;

import java.util.Comparator;

import citycircle.com.OA.OAAdapter.CityNameMod;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<CityNameMod> {

	public int compare(CityNameMod o1, CityNameMod o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
