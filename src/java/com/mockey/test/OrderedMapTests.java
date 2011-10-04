package com.mockey.test;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import com.mockey.OrderedMap;
import com.mockey.model.PersistableItem;

import static org.junit.Assert.*;

public class OrderedMapTests {
	
	private class LabeledItem implements PersistableItem {
		private Long id;
		private String label;
		
		public LabeledItem(Long id, String label) {
			this.id = id;
			this.label = label;
		}
		
		public Long getId() {
			return this.id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getLabel() {
			return this.label;
		}
	}
	
	private OrderedMap<LabeledItem> items;
	
	@Before
	public void beforeTest() {
		this.items = new OrderedMap<LabeledItem>();
	}
	
	private void saveItems(LabeledItem...items) {
		for (LabeledItem item : items) {
			this.items.save(item);
		}
	}
	
	private void saveThreeItems() {
		saveItems(
			new LabeledItem(1L, "foo"),
			new LabeledItem(2L, "bar"),
			new LabeledItem(3L, "baz")
		);
	}
	
	private List<Long> getOrderedIds() {
		List<Long> ids = new ArrayList<Long>();
		for (LabeledItem item : this.items.getOrderedList()) {
			ids.add(item.getId());
		}
		return ids;
	}
	
	private void verifyIds(Long...ids) {
		assertEquals(Arrays.asList(ids), getOrderedIds());
	}
	
	@Test
	public void returnsItemWhenSaved() {
		LabeledItem item = new LabeledItem(0L, "foo");
		assertEquals(item, this.items.save(item));
	}
	
	@Test
	public void returnsNullWhenSavingNull() {
		assertEquals(null, this.items.save(null));
	}
	
	@Test
	public void populatesItemWithIdWhenMissing() {
		LabeledItem item = new LabeledItem(null, "foo");
		assertNotNull(this.items.save(item).getId());
	}
	
	@Test
	public void limitsInternalStoreToSpecifiedMaxSize() {
		this.items.setMaxSize(2);
		saveThreeItems();
		assertEquals(2, this.items.size());
	}
	
	@Test
	public void assignsItemNewMaxIdWhenIdMissing() {
		saveThreeItems();
		LabeledItem item = new LabeledItem(null, "blah");
		assertEquals(4L, this.items.save(item).getId());
	}
	
	@Test
	public void dropsItemWithLowestIdWhenExceedingMaxSize() {
		this.items.setMaxSize(2);
		saveThreeItems();
		verifyIds(2L, 3L);
	}
	
	@Test
	public void providesListOfItemsOrderedById() {
		saveThreeItems();
		verifyIds(1L, 2L, 3L);
	}
}
