package com.mockey.storage;

import java.util.Collection;

import com.mockey.PersistableItemStore;
import com.mockey.model.ApiDocService;
import com.mockey.model.PersistableItem;

public class IApiStorageInMemory implements IApiStorage {

	private PersistableItemStore<ApiDocService> apiStore = new PersistableItemStore<ApiDocService>();
	private static IApiStorageInMemory store = new IApiStorageInMemory();

	/**
	 * 
	 * @return
	 */
	public static IApiStorageInMemory getInstance() {
		return store;
	}


	public ApiDocService getApiDocServiceById(Long serviceId) {
		return apiStore.get(serviceId);
	}

	public Collection<ApiDocService> getApiDocServices() {
		return this.apiStore.getOrderedList();

	}

	public ApiDocService saveOrUpdateService(ApiDocService apiDocService) {
		PersistableItem item = apiStore.save(apiDocService);
		return (ApiDocService) item;
	}


	public ApiDocService getApiDocServiceByName(String name) {
		ApiDocService service = null;
		for(ApiDocService s : getApiDocServices()){
			if(name!=null && name.trim().equalsIgnoreCase(s.getName()) ){
				service = s;
				break;
			}
			
		}
		
		return service;
	}

}
