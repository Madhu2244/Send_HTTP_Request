import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main 
{
	
	private static HttpURLConnection connection;

	public static void main(String[] args) 
	{
		//METHOD ONE
		
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();
		
		try 
		{
			URL url = new URL("https://jsonplaceholder.typicode.com/posts");
			connection = (HttpURLConnection) url.openConnection();
			
			//Request setup
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			System.out.println(status);
			
			if (status > 299) 
			{
				reader = new BufferedReader (new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) 
				{
					responseContent.append(line);
				}
				reader.close();
			} 
			else
			{
				reader = new BufferedReader (new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) 
				{
					responseContent.append(line);
				}
				reader.close();
			}
			//System.out.println(responseContent.toString());
			parse(responseContent.toString());
		} 
		
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		finally 
		{
			connection.disconnect();
		}
		/*
		//METHOD TWO:
		//create client
		HttpClient client = HttpClient.newHttpClient();
		//create request using url
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://jsonplaceholder.typicode.com/posts")).build();
		//set request using client. 
		//sendAsync - send asyncrously
		//want to receive response body as a string (HttpResponse.bodyhandlers.offString)
		
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body) // use body method from httpresposne method
			.thenAccept(System.out::println) //uses that body to printout using println method
			.join(); //returns it on console without this, you wouldn't see the console
		*/
	}
	
	public static String parse(String responseBody)
	{
		JSONArray albums = new JSONArray(responseBody);
		for(int i = 0; i < albums.length(); i++)
		{
			JSONObject album = albums.getJSONObject(i);
			int id = album.getInt("id");
			int userId = album.getInt("userId");
			String title = album.getString("title");
			String body = album.getString("body");
			System.out.println(userId + " " + title + " " + id + " " + body);
		}
		return null;
	}

}
