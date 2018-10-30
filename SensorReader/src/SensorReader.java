/*
 * Copyright 2007 Phidgets Inc.  All rights reserved.
 */

import java.net.HttpURLConnection;
import java.net.URL;
//import java.io.IOException;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

public class SensorReader
{
	private static final String USER_AGENT = "Mozilla/1.0";
	private static final String GET_URL = "http://sensorfeed.mybluemix.net/";
	private static InterfaceKitPhidget ik;
	public static final void main(String args[]) throws Exception {
		
		ik = new InterfaceKitPhidget();
		//String [] sensor_names = {"None","Temp","Light","Vibration","Weight","Proximity", "Force"};
		
		System.out.println(Phidget.getLibraryVersion());

		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("acomplamiento de " + ae);				
			}
		});
        //asignar listener de desacoplamiento
		ik.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("desacoplamiento de" + ae);
			}
		});
                
        //asignar listener de error
		ik.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println(ee);
				System.out.print("cerrando error con dispositivo ...");
				System.out.flush();
                try {
                    ik.close();
                } catch (PhidgetException ex) {
                	System.out.print(ex.getMessage());
                    //Logger.getLogger(ik.class.getName()).log(Level.SEVERE, null, ex);
                }
			}
		});
        //listener de sensor	
		ik.addSensorChangeListener(new SensorChangeListener() {
            public void sensorChanged(SensorChangeEvent ae ) {
            	int str = ae.getValue();
            	int sensorType = 0;
            	switch (ae.getIndex()) {
					case 0://Temperature				
						sensorType = 1;
						System.out.println("Temperature");
						break;
					case 1://light						
						sensorType = 2;
						System.out.println("Light");
						break;
					case 3://Force
						sensorType = 6;
						System.out.println("Force");
						break;
					default:
						break;
				}
            	if (str != 0 && sensorType > 0){
            		try{
            			String url = GET_URL + "?sensor="+sensorType+"&value="+str;
            			System.out.println(url);
            			URL obj = new URL(url);
            			HttpURLConnection con = (HttpURLConnection) obj.openConnection();            			
            			con.setRequestMethod("GET");
            			con.setRequestProperty("User-Agent", USER_AGENT);
            			int responseCode = con.getResponseCode();
            			
            			System.out.println("GET Response Code :: " + responseCode);
            			Thread.sleep(5000);
            		}
            		catch (Exception e) {
            			System.out.println(e.getMessage());
					}
            		
            	}
            }
     	});
        //abrir remotamente el ik ip y puerto
        //ik.openAny("phidgetsbc.local",5001);        
        ik.openAny();
		System.out.println("waiting for InterfaceKit attachment...");
		ik.waitForAttachment();
                          
		/*System.out.println("esperando por acoplamiento de IK...");
		ik.waitForAttachment();*/
		ik.setSensorChangeTrigger(2, 10);
		ik.setSensorChangeTrigger(4, 10);
		ik.setSensorChangeTrigger(5, 10);
		ik.setSensorChangeTrigger(6, 10);
		ik.setSensorChangeTrigger(7, 10);
		
		ik.setSensorChangeTrigger(3, 10);
		ik.setSensorChangeTrigger(1, 20);
		ik.setSensorChangeTrigger(0, 5);
        System.out.println(ik.getDeviceName());
        Thread.sleep(1000);
        while (ik.isAttached()) {            
               //     System.out.println(SensorChangeEvent(index,value,source));
        }
	}	
}
