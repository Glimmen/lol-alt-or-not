package com.altornot.AltOrNot;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;
import com.google.gson.Gson;

public class Summoner {
	private String apiKey = System.getenv("RIOT_API_KEY");
	private String gameName = null;
	private String tagLine = null;
	private String puuid = null;
	private Champion[] topChamps = new Champion[10];
	
	public void puuid() {
		// Set up HTTP communication
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine))
				.header("X-Riot-Token", apiKey)
				.GET()
				.build();
		HttpResponse<String> response = null;
		
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			// Parse JSON response using GSON
			Summoner summoner = new Summoner();
			summoner = Summoner.fromJson(response.body());
			this.puuid = summoner.puuid;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Summoner fromJson(String response) {
		Gson g = new Gson();
		return g.fromJson(response, Summoner.class);
	}
	
	public void getInput() {
    	Scanner scanner = new Scanner(System.in);
    	
    	try {
    		System.out.print("Please enter summoner name: ");
    		this.gameName = scanner.nextLine();
    		System.out.print("Please enter summoner tagline: ");
    		this.tagLine = scanner.nextLine();
    	} catch (InputMismatchException e) {
    		e.printStackTrace();
    	}
    }
}