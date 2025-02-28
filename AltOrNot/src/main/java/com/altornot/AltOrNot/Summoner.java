package com.altornot.AltOrNot;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Summoner {
	private final int MAX_TOP_CHAMPS = 10;
	private String apiKey = System.getenv("RIOT_API_KEY");
	private String gameName = null;
	private String tagLine = null;
	private String puuid = null;
	private Champion[] topChamps;
	private int[] freeChampionIds;
    private int[] freeChampionIdsForNewPlayers;
    private int maxNewPlayerLevel;
	
	public void retrievePuuid() {
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
			Summoner temp = new Summoner();
			Gson g = new Gson();
			temp = g.fromJson(response.body(), Summoner.class);
			this.puuid = temp.puuid;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void retrieveTopChamps() {
		// Set up HTTP communication
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://na1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/" + this.puuid + "/top?count=" + MAX_TOP_CHAMPS))
				.header("X-Riot-Token", apiKey)
				.GET()
				.build();
		HttpResponse<String> response = null;
		
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			// Parse JSON response using GSON
			TypeToken<List<Champion>> typeToken =  new TypeToken<List<Champion>>() {};
			List<Champion> temp = new Gson().fromJson(response.body(), typeToken.getType());
			this.topChamps = temp.toArray(new Champion[0]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void retrieveFreeChamps() {
    	// Set up HTTP communication
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://na1.api.riotgames.com/lol/platform/v3/champion-rotations"))
				.header("X-Riot-Token", apiKey)
				.GET()
				.build();
		HttpResponse<String> response = null;
		
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			// Parse JSON response using GSON
			Summoner temp = new Summoner();
			Gson g = new Gson();
			temp = g.fromJson(response.body(), Summoner.class);
			this.freeChampionIds = temp.freeChampionIds;
			this.freeChampionIdsForNewPlayers = temp.freeChampionIdsForNewPlayers;
			this.maxNewPlayerLevel = temp.maxNewPlayerLevel;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
    	} finally {
    		scanner.close();
    	}
    }
}