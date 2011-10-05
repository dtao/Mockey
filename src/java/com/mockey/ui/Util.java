/*
¿ * This file is part of Mockey, a tool for testing application 
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
package com.mockey.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.mockey.model.ApiDocService;
import com.mockey.model.Scenario;
import com.mockey.model.Service;
import com.mockey.model.ServicePlan;

public class Util {

	public static final String SUCCESS = "successMessages";
	public static final String ERROR = "errorMessages";

	/**
	 * Saves the LAST message.
	 * 
	 * @param message
	 * @param req
	 */
	private static void save(String message, String messageKey, HttpServletRequest req) {

		// HISTORY: This method use to save a List of messages
		// for the purpose to display to the end user. But since
		// this solution can be tweak by a head-less client,
		// the list of informative messages to the user became
		// perplexing.
		List<String> msgs = new ArrayList<String>();
		msgs.add(message);
		req.getSession().setAttribute(messageKey, msgs);
	}

	/**
	 * Saves the last (most recent) error message.
	 * 
	 * @param message
	 * @param req
	 */
	public static void saveErrorMessage(String message, HttpServletRequest req) {
		save(message, ERROR, req);
	}

	/**
	 * Saves the last (most recent) success message.
	 * 
	 * @param message
	 * @param req
	 */
	public static void saveSuccessMessage(String message, HttpServletRequest req) {
		save(message, SUCCESS, req);

	}

	@SuppressWarnings("unchecked")
	public static void saveErrorMap(Map errorMap, HttpServletRequest req) {
		if (errorMap != null) {
			Iterator<String> iter = errorMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = (String) errorMap.get(key);
				save((key + " : " + value), ERROR, req);
			}
		}

	}
	
	public static <T> List<T> orderBy(Collection<T> collection, Comparator<T> comparator) {
		List<T> ordered = new ArrayList<T>();
		ordered.addAll(collection);
		
		Collections.sort(ordered, comparator);
		
		return ordered;
	}

	/**
	 * Returns the services list ordered alphabetically.
	 * 
	 * @param services
	 * @return
	 */
	public static List<Service> orderAlphabeticallyByServiceName(Collection<Service> services) {

		// Custom comparator
		class ServiceNameComparator implements Comparator<Service> {

			public int compare(Service s1, Service s2) {
				return s1.getServiceName().compareToIgnoreCase(s2.getServiceName());

			}

		}
		
		return orderBy(services, new ServiceNameComparator());
	}

	/**
	 * Returns the services list ordered alphabetically.
	 * 
	 * @param services
	 * @return
	 */
	public static List<ServicePlan> orderAlphabeticallyByServicePlanName(Collection<ServicePlan> servicePlans) {

		// Custom comparator
		class ServicePlanNameComparator implements Comparator<ServicePlan> {

			public int compare(ServicePlan s1, ServicePlan s2) {
				return s1.getName().compareToIgnoreCase(s2.getName());

			}

		}
		
		return orderBy(servicePlans, new ServicePlanNameComparator());
	}
	
	/**
	 * Returns the services list ordered alphabetically.
	 * 
	 * @param services
	 * @return
	 */
	public static List<ApiDocService> orderAlphabeticallyByApiName(Collection<ApiDocService> apiDocServices) {

		// Custom comparator
		class ApiDocServiceComparator implements Comparator<ApiDocService> {

			public int compare(ApiDocService s1, ApiDocService s2) {
				return s1.getName().compareToIgnoreCase(s2.getName());

			}

		}
		
		return orderBy(apiDocServices, new ApiDocServiceComparator());
	}

	/**
	 * Returns the services list ordered alphabetically.
	 * 
	 * @param services
	 * @return
	 */
	public static List<Scenario> orderAlphabeticallyByScenarioName(Collection<Scenario> scenarios) {
		// Custom comparator
		class ScenarioNameComparator implements Comparator<Scenario> {
			public int compare(Scenario s1, Scenario s2) {
				return s1.getScenarioName().compareToIgnoreCase(s2.getScenarioName());
			}
		}
		
		return orderBy(scenarios, new ScenarioNameComparator());
	}

	/**
	 * 
	 * @param objectMap
	 * 
	 * @return
	 */
	public static String getJSON(Map<String, String> objectMap) {
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		Iterator<String> errorIter = objectMap.keySet().iterator();
		String result = null;
		try {
			while (errorIter.hasNext()) {
				String key = errorIter.next();
				String value = (String) objectMap.get(key);
				jsonObject.put(key, value);
			}
			jsonResult.put("result", jsonObject);
			result = jsonResult.toString();
		} catch (JSONException je) {
			result = "Unable to create JSON format response. " + je.getMessage();
		}

		return result;
	}
	
	public static <T> T getFirstItem(Iterable<T> sequence) {
		for (T element : sequence) {
			return element;
		}
		
		return null;
	}

}