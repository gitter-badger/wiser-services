/*
 * Created by Wiser Research Group UFBA
 */
package br.dcc.ufba.wiser.smartufba.actuator.lamp;
 
import java.util.HashMap;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
 
// URL: devices/actuator/camera[?move=left;right;up;down]
 
@Path("/devices/actuator/camera")
public class CameraService {
	
    private String cameraIp = "192.168.0.199:7777";
    private HashMap<String, Integer> mapaStringCodigos = new HashMap<String, Integer>() {
            private static final long serialVersionUID = 1L;
            {
                put("left", 4);
                put("right", 3);
                put("down", 2);
                put("up", 1);
            }
    };
 
    // Realiza um sleep por s segundos
    private void sleepFor(int s) {
    	try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @GET
    @Produces("*/*")
    public Response getSnapshot(@DefaultValue("none") @QueryParam("move") String direction) { // Response, StreamingOutput...
   
            Client client = ClientBuilder.newClient();
            Response response;
           
            if (mapaStringCodigos.containsKey(direction)) {
            	response = client.target("http://" + cameraIp + "/media/?action=cmd&code=2&value=" + mapaStringCodigos.get(direction)).request().header("Authorization", "Basic YWRtaW46YWRtaW4=").get();     
	            sleepFor(300);                       
	            response = client.target("http://" + cameraIp + "/media/?action=cmd&code=3&value=" + mapaStringCodigos.get(direction)).request().header("Authorization", "Basic YWRtaW46YWRtaW4=").get();                            
	            sleepFor(2000);   
	            response = client.target("http://" + cameraIp + "/media/?action=snapshot").request().header("Authorization", "Basic YWRtaW46YWRtaW4=").get();
	                                     
	            return response;
	        }
           
        return client.target("http://" + cameraIp + "/media/?action=snapshot").request().header("Authorization", "Basic YWRtaW46YWRtaW4=").get();
    }
}

/**
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.ufba.dcc.wiser.smartufba.tatu.drivers.DriverMQTT;

@Path("/devices/actuator/lamp")
public class CameraService {
    
    public CameraService() {
    }

    @GET
    @Produces("application/json")
    public Response getStatusLamp() throws Exception {
    	
    	Lamp l = new Lamp();
        
        DriverMQTT lamp = new DriverMQTT("temp-lamp", "device", "boteco@wiser");
        String status = lamp.getInfo("lamp");
        
        
        if(status.contentEquals("ON")){ 
        	l.setStatus(true);
        }else{
        	l.setStatus(false);
        }
    	
        ResponseBuilder rb = Response.ok(l)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
            .header("Access-Control-Allow-Headers", "Content-Type")
            .allow("OPTIONS");
        return rb.build();
    }
 
    @POST
    @Produces("application/json")
    public Response setStatusLamp(@FormParam("status") boolean status) throws Exception {
    	Lamp l = new Lamp();    	
    	l.setStatus(status);
    	
        DriverMQTT lamp = new DriverMQTT("temp-lamp", "device", "boteco@wiser"); 
        
        lamp.setInfo("lamp", l.getStatus() ? 1 : 0);      
        
        ResponseBuilder rb = Response.ok(l)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
            .header("Access-Control-Allow-Headers", "Content-Type")
            .allow("OPTIONS");
        return rb.build();
    }
}

*/