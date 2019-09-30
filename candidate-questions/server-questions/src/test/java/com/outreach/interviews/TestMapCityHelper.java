package com.outreach.interviews;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.outreach.interviews.map.builder.MapCityHelper;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class TestMapCityHelper 
{	
	
	@Test
	public void testMapCityHelperApiKey1() throws UnsupportedOperationException, IOException {
		double[] location = new MapCityHelper.RoutesBuilder()
            .setAddress("348 Stewart Street, Ottawa, ON, Canada")
            .build()
            .getLatitudeAndLongitude();

        System.out.println("Latitude: " + location[0]);
        System.out.println("Longitude: " + location[1]);
	}
	
	@Test
	public void testMapRoutesHelperApiKey2() throws UnsupportedOperationException, IOException {
        double[] location = new MapCityHelper.RoutesBuilder()
            .setAddress("90 University Private, Ottawa, ON, Canada")
            .build()
            .getLatitudeAndLongitude();
		
		assertNotNull(location);
        assertTrue(location[0]);
        assertTrue(location[1]);
	}
	
	@Test
	public void testMapRoutesHelperApiKey3() throws UnsupportedOperationException, IOException {
		double[] location = new MapCityHelper.RoutesBuilder()
            .setAddress("129 York Street, Ottawa, ON, Canada")
            .build()
            .getLatitudeAndLongitude();
		
		assertNotNull(location);
	}
	
}
