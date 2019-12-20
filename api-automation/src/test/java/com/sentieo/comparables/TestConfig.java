package com.sentieo.comparables;

import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.sentieo.constants.Configuration;

public class TestConfig {
	
	String env = "citadel";
	
	@Test
	public void whenLoadMultipleYAMLDocuments_thenLoadCorrectJavaObjects1() throws IOException {
		 	Yaml yaml = new Yaml(new Constructor(Configuration.class));
		    InputStream inputStream = this.getClass()
		      .getClassLoader()
		      .getResourceAsStream("details.yaml");
		 
		    for (Object object : yaml.loadAll(inputStream)) {
		    	if(((Configuration)object).getEnvName().equals(env)) {
			        System.out.println(((Configuration)object).getAppURL());
			        System.out.println(((Configuration)object).getUserName());
			        System.out.println(((Configuration)object).getPassword());
		    	}
		    }
		    
	}

}
