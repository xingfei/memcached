/**
 * 
 */
package com.xuetangx.memcached.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * @author xingfei
 *
 */
public class ServletInitializer {
	static final String key = "servlet.config.file";
	
	@SuppressWarnings("unchecked")
	public ServletContainer initServletContainer() throws Exception {
		Yaml yaml = new Yaml();
		
		Map<String, Object> config;
		
		if (System.getProperty(key) == null) {
			config = (Map<String, Object>)yaml.load(getClass().getResourceAsStream("/servlet.conf.yaml"));
		} else {
			File f = new File(System.getProperty(key));
			try(FileInputStream in = new FileInputStream(f)) {
				config = (Map<String, Object>)yaml.load(in);
			}
		}
		
		System.out.println(config);
		
		ServletContainer container = new ServletContainer();
		
		String servletClass = config.get("servlet").toString();
		container.setServlet(instantiateClass(servletClass));
		
		if (config.containsKey("listener")) {
			container.setListener(instantiateClass(config.get("listener").toString()));
		}
		
		
		if (config.containsKey("context_params")) {
			container.setContext(new DefaultServletContext((Map<String, Object>)config.get("context_params")));
		} else {
			container.setContext(new DefaultServletContext());
		}
		
		return container;
		
	}
	
	@SuppressWarnings("unchecked")
	private <T> T instantiateClass(String className) throws Exception {
		Class<?> clazz = Class.forName(className);
		return (T)clazz.newInstance();
	}

}
