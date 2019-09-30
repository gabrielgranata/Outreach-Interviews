package com.outreach.interviews.map.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.outreach.interviews.map.enums.MapModes;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class MapCityHelper
{
	public static class RoutesBuilder {

        private String address;
        private MapOperations operation;
		private JsonObject result;

		private final String URL = "https://maps.googleapis.com/maps/api";
		private CloseableHttpClient httpclient = HttpClients.createDefault();
		
		
		/**
		 * Set the address
		 * @param address Address required to find Lat and Long
         * Address must be submitted in the form "[Number] [Street Name], [City], [State/Province], [Country]"
         * parseAddressString method will take the spaces and replace them with '+' for the request URL
		 * @return {@link RoutesBuilder}
		 */
		public RoutesBuilder setAddress(String address) {
			address = this.parseAddressString(address);
			this.address = address;
			System.out.println(address);
			return this;
		}
		
		/**
		 * Perform the HTTP request and retrieve the data from the HttpClient object
		 * @return {@link RoutesBuilder}  
		 * @throws UnsupportedOperationException Thrown to indicate that the requested operation is not supported.
		 * @throws IOException Thrown to indicate that the requested operation is not supported.
		 * @throws IllegalArgumentException Thrown to indicate that a method has been passed an illegal orinappropriate argument.
		 */
		public RoutesBuilder build() throws UnsupportedOperationException, IOException, IllegalArgumentException {
			String requestURL = this.getURL()  	+ "address=" + getAddress() 
												+ "&key=" + this.getAPIKey();
			
			
			HttpGet httpGet = new HttpGet(requestURL);
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			try {
				HttpEntity entity = response.getEntity();
				String result = IOUtils.toString(entity.getContent(), "UTF-8");
				this.result = new JsonParser().parse(result).getAsJsonObject();
			}
			finally {
				response.close();
			}
			
			return this;
		}
		
		/**
		 * Retrieve the steps required to get from the source to the destination
		 * @return List of String containing the steps to get to the destination
		 */
		public double[] getLatitudeAndLongitude() {
			if(this.operation.equals(MapOperations.geocode) && zeroResults(this.result)) {
				JsonObject locationObject = this.result.get("results").getAsJsonArray().get(0).getAsJsonObject()
					.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
				
                double latitude = locationObject.get("lat").getAsDouble();
                double longitude = locationObject.get("lng").getAsDouble();
                double[] location = new double[2];
                location[0] = latitude;
                location[1] = longitude;
				return location;
			} else {
				throw new IllegalArgumentException("Does not support " + MapOperations.geocode.name());
			}
		}
		

		//*************************For Internal Use Only***********************************//
		private final String getURL() {
			return this.URL + "geocode" + "/json?";
		}

		private final String getAPIKey() {
			return System.getenv("OUTREACH_MAPS_KEY");
        }
        
        private final String parseAddressString(String string) {
            return string.replaceAll("\\s", "+");
        }
		
		
		private final String getAddress() {
			if(this.address == null)
				throw new IllegalArgumentException("Region cannot be empty");
			
			return this.address;
		}
		
		private final boolean zeroResults(JsonObject obj) {
			return !obj.get("status").getAsString().equals("ZERO_RESULTS");
		}

	}
}