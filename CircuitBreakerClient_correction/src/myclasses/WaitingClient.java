package myclasses;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.config.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;

public class WaitingClient {

	public static void main(String[] args) throws Exception { 

		String url1 = "http://localhost:8080/CircuitBreakerServer/CB?limit=100000000";
		
		HttpGet request1 = new HttpGet(url1);
		CircuitBreaker cb=new CircuitBreaker();
		int loop = 0;
		while(loop <100){
			try{
				System.out.println("trying");
				HttpResponse response = cb.execute(request1, getClient());
				System.out.println("done");
				break;
			}
			catch(Exception e){
				System.out.println(e.getClass().getName());
				System.out.println("do something else");
			}
			loop++;
		}
	}
	
	private static HttpClient getClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(1)
				.setConnectTimeout(1)
				.setSocketTimeout(50).build();
		HttpClient client = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig).build();
		return client;
	}
}







