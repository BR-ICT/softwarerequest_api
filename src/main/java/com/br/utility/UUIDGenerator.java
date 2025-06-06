package com.br.utility;

import java.util.UUID;

public class UUIDGenerator {
	
	public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "");
//		return UUID.randomUUID().toString();
        
    }

}
