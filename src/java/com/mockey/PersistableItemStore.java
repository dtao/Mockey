/*
 * This file is part of Mockey, a tool for testing application 
 * interactions over HTTP, with a focus on testing web services, 
 * specifically web applications that consume XML, JSON, and HTML.
 *  
 * Copyright (C) 2009-2010  Authors:
 * 
 * chad.lafontaine (chad.lafontaine AT gmail DOT com)
 * neil.cronin (neil AT rackle DOT com) 
 * lorin.kobashigawa (lkb AT kgawa DOT com)
 * rob.meyer (rob AT bigdis DOT com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package com.mockey;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import com.mockey.model.PersistableItem;

/**
 * Keeps an ordered map of items, with a key of an number based on 'when defined
 * and added to this object'. Sort of like an auto-incrementing column in a db.
 * <p>
 * So:
 * <p>
 * <code>
 * PersistableItemStore<MyObj> myStore = new PersistableItemStore<MyObj>();<br>
 * 0 == myStore.save(new MyObj()).getId();<br>
 * 1 == myStore.save(new MyObj()).getId();<br>
 * 2 == myStore.save(new MyObj()).getId();<br>
 * </code>
 * 
 * @author chad.lafontaine
 * 
 */
public class PersistableItemStore<T extends PersistableItem> {

    private static final long serialVersionUID = -1654150132938363942L;
    private ConcurrentSkipListMap<Long, T> map = new ConcurrentSkipListMap<Long, T>();
    private Integer maxSize = null;
    
    /**
     * Size of the store.
     * 
     * @return Returns the current number of items in the store.
     */
    public int size() {
    	return this.map.size();
    }
    
    /**
     * Will save item. If maximum size of this Map is set (non-null, positive),
     * this method will purge the oldest value, the value with the smallest key
     * value.
     * 
     * @param item
     * @return item saved, with ID set.
     * @see #getMaxSize()
     */
    public PersistableItem save(T item) {

        if (item != null) {
            if (item.getId() == null) {
                Long nextNumber = this.getNextValue();
                item.setId(nextNumber);
            }
            
            this.map.put(item.getId(), item);
        }

        if (this.maxSize != null && this.maxSize > 0) {
            while (this.map.size() > this.maxSize) {
                Long removeMe = getSmallestValue();
                this.remove(removeMe);
            }
        }
        
        return item;
    }
    
    public T remove(Long id) {
    	return this.map.remove(id);
    }

    public T get(Object key) {
      if (key == null) {
        return null;
      }
      return this.map.get(key);
    }

    private Long getSmallestValue() {
        return this.map.firstKey();
    }

    private Long getNextValue() {
        return this.map.isEmpty() ? 1L : new Long(this.map.lastKey() + 1L);
    }

	public Collection<T> getOrderedList() {
    	return this.map.values();
    }

    /**
     * 
     * @param maxSize
     * @see #getMaxSize()
     */
    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Maximum number of <code>PersistableItem</code> allowed in this ordered
     * map. Once this map has reached its limit, if set, it takes a first-in,
     * first-out (FIFO) persistence approach, purging the oldest object.
     * 
     * @return null if not set (no size limit), otherwise returns the maximum
     *         size value.
     */
    public Integer getMaxSize() {
        return maxSize;
    }
}
