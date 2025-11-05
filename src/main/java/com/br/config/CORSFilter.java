package com.br.config;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	/*
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

    	
    	String origin = request.getHeaderValue("Origin");
    	
    	System.out.println("Origin: " + origin);

        if ("http://localhost:3001".equals(origin)) {
            response.getHttpHeaders().add("Access-Control-Allow-Origin", "http://localhost:3001");
        } else {
            // ถ้าอยากเปิดทุก origin (อาจไม่ปลอดภัยถ้ามี credentials)
            response.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
        }
        response.getHttpHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-access-token,x-access-uuid");
        response.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        //response.getHttpHeaders().add("Access-Control-Max-Age", "1209600"); // Optional, to cache preflight request for 2 weeks
        return response;
     
       
    }
     */  
    
    
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
     //response.getHttpHeaders().add("Access-Control-Allow-Origin", "http://localhost:3001");
     
    	
//
//     response.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
//     response.getHttpHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-access-token, x-access-uuid");
//     response.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
//     response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

     
      response.getHttpHeaders().add("Access-Control-Allow-Origin", "*");

	 //   response.getHttpHeaders().add("Access-Control-Allow-Origin", "http://localhost:3001");
		response.getHttpHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-access-token, x-access-uuid");
		response.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
		response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		

     
     
     return response;
    }
   
    
	
	
	 
}
