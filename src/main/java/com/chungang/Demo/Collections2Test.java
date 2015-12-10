package com.chungang.Demo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class Collections2Test {

	public static void main(String[] args) {
		List<String> list1 = null; //Lists.newArrayList("a", "b", "c");
		final List<String> list2 = Lists.newArrayList("a", "b");
		
		for (String s : list2) {
			s.charAt(0);
		}
		
		Collection<String> r = Collections2.filter(list1, new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return CollectionUtils.isEmpty(list2) || !list2.contains(input);
			}
		});
		System.out.println(r);
	}
}
