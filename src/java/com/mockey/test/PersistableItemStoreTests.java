package com.mockey.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mockey.PersistableItemStore;
import com.mockey.model.PersistableItem;

public class PersistableItemStoreTests {
	
	private class BasicPersistableItem implements PersistableItem {
		private Long id;
		
		public BasicPersistableItem(Long id) {
			this.id = id;
		}
		
		public Long getId() {
			return this.id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
	}
	
	private PersistableItemStore<BasicPersistableItem> items;
	
	@Before
	public void beforeTest() {
		this.items = new PersistableItemStore<BasicPersistableItem>();
	}

	private void saveItems(Long...ids) {
		for (int i = 0; i < ids.length; ++i) {
			this.items.save(new BasicPersistableItem((long)ids[i]));
		}
	}
	
	private List<Long> getOrderedIds() {
		List<Long> ids = new ArrayList<Long>();
		for (BasicPersistableItem item : this.items.getOrderedList()) {
			ids.add(item.getId());
		}
		return ids;
	}
	
	private void verifyIds(Long...ids) {
		assertEquals(Arrays.asList(ids), getOrderedIds());
	}
	
	@Test
	public void returnsItemWhenSaved() {
		BasicPersistableItem item = new BasicPersistableItem(0L);
		assertEquals(item, this.items.save(item));
	}
	
	@Test
	public void returnsNullWhenSavingNull() {
		assertEquals(null, this.items.save(null));
	}
	
	@Test
	public void populatesItemWithIdWhenMissing() {
		BasicPersistableItem item = new BasicPersistableItem(null);
		assertNotNull(this.items.save(item).getId());
	}
	
	@Test
	public void limitsInternalStoreToSpecifiedMaxSize() {
		this.items.setMaxSize(2);
		saveItems(1L, 2L, 3L);
		assertEquals(2, this.items.size());
	}
	
	@Test
	public void assignsItemNewMaxIdWhenIdMissing() {
		saveItems(1L, 2L, 3L);
		BasicPersistableItem item = new BasicPersistableItem(null);
		assertEquals(4L, this.items.save(item).getId());
	}
	
	@Test
	public void dropsItemWithLowestIdWhenExceedingMaxSize() {
		this.items.setMaxSize(2);
		saveItems(1L, 2L, 3L);
		verifyIds(2L, 3L);
	}
	
	@Test
	public void providesListOfItemsOrderedById() {
		saveItems(1L, 2L, 3L);
		verifyIds(1L, 2L, 3L);
	}
}
