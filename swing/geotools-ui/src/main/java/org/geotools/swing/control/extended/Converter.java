/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing.control.extended;

/**
 *
 * @author RizzoM
 */
import java.util.List;

import org.cts.CRSFactory;
import org.cts.IllegalCoordinateException;
import org.cts.crs.CRSException;
import org.cts.crs.CoordinateReferenceSystem;
import org.cts.crs.GeodeticCRS;
import org.cts.op.CoordinateOperation;
import org.cts.op.CoordinateOperationFactory;
import org.cts.registry.EPSGRegistry;
import org.cts.registry.RegistryManager;


public class Converter {
	public static void main(String[] args){
//            public Converter (){
		CRSFactory cRSFactory = new CRSFactory();

		// Add the appropriate registry to the CRSFactory's registry manager. Here the EPSG registry is used.
		RegistryManager registryManager = cRSFactory.getRegistryManager();
		registryManager.addRegistry(new EPSGRegistry());

		// CTS will read the EPSG registry seeking the 4326 code, when it finds it,
		// it will create a CoordinateReferenceSystem using the parameters found in the registry.
		try {
			CoordinateReferenceSystem epsg2161 = cRSFactory.getCRS("EPSG:32628"); // UTM28
			CoordinateReferenceSystem epsg2162 = cRSFactory.getCRS("EPSG:32629"); // UTM29
			CoordinateReferenceSystem epsg2159 = cRSFactory.getCRS("EPSG:2159"); // New Colony (Colonial?)

			List<CoordinateOperation> coordOps = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS)epsg2162,(GeodeticCRS)epsg2161);

			// Note that we get a List and not a single CoordinateTransformation, because several methods may exist to
			// transform a position from crs1 to crs2
	    	double[] source = {867537.576,114230.608}; // source is {latitude,longitude} according to crs1

			if (coordOps.size() != 0) {
			    // Test each transformation method (generally, only one method is available)
			    for (CoordinateOperation op : coordOps) {
			        // Transform coord using the op CoordinateOperation from crs1 to crs2
			        double[] dest  = op.transform(source); // dest is {latitude,longitude} according to crs2
			        System.out.println("Tranformation: " + op.getName() + ", source: " + source[0] + ", " + source[1] + ", dest: " + dest[0] + ", " + dest[1]);
			    }
		    }

			coordOps = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS)epsg2161,(GeodeticCRS)epsg2162);

			if (coordOps.size() != 0) {

			    for (CoordinateOperation op : coordOps) {

			        double[] dest  = op.transform(source);
			        System.out.println("Tranformation: " + op.getName() + ", source: " + source[0] + ", " + source[1] + ", dest: " + dest[0] + ", " + dest[1]);
			    }
		    }

			coordOps = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS)epsg2162,(GeodeticCRS)epsg2159);

			if (coordOps.size() != 0) {

			    for (CoordinateOperation op : coordOps) {

			        double[] dest  = op.transform(source);
			        System.out.println("Tranformation: " + op.getName() + ", source: " + source[0] + ", " + source[1] + ", dest: " + dest[0] + ", " + dest[1]);
			    }
		    }

			coordOps = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS)epsg2159,(GeodeticCRS)epsg2161);

			if (coordOps.size() != 0) {

			    for (CoordinateOperation op : coordOps) {

			        double[] dest  = op.transform(source);
			        System.out.println("Tranformation: " + op.getName() + ", source: " + source[0] + ", " + source[1] + ", dest: " + dest[0] + ", " + dest[1]);
			    }
		    }

			coordOps = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS)epsg2161,(GeodeticCRS)epsg2162);

			if (coordOps.size() != 0) {

			    for (CoordinateOperation op : coordOps) {

			        double[] dest  = op.transform(source);
			        System.out.println("Tranformation: " + op.getName() + ", source: " + source[0] + ", " + source[1] + ", dest: " + dest[0] + ", " + dest[1]);
			    }
		    }
		} catch (CRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalCoordinateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
