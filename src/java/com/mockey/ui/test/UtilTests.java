package com.mockey.ui.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mockey.ui.Util;

public class UtilTests {
	
	@Test
	public void getsTheFirstElementFromASequence() {
		List<Integer> numbers = Arrays.asList(1, 2, 3);
		assertEquals(1, Util.getFirstItem(numbers));
	}
	
	@Test
	public void returnsNullForAnEmptySequence() {
		List<Integer> numbers = Arrays.asList();
		assertEquals(null, Util.getFirstItem(numbers));
	}
	
}
