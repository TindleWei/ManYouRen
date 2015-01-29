package com.manyouren.manyouren.core.user;

import java.util.Comparator;

import com.manyouren.manyouren.ui.user.CityListActivity.SortCity;


/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<SortCity> {

	public int compare(SortCity o1, SortCity o2) {
		if (o1.getSortLetter().equals("@")
				|| o2.getSortLetter().equals("#")) {
			return -1;
		} else if (o1.getSortLetter().equals("#")
				|| o2.getSortLetter().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetter().compareTo(o2.getSortLetter());
		}
	}

}
