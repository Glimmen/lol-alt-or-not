package com.altornot.AltOrNot;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Summoner {
	private final int MAX_TOP_CHAMPS = 10;
	private String apiKey = System.getenv("RIOT_API_KEY");
	private String gameName = null;
	private String tagLine = null;
	private String puuid = null;
	private Champion[] topChamps;
	@JsonProperty("freeChampionIds")
	private int[] freeChampionIds;
	@JsonProperty("freeChampionIdsForNewPlayers")
    private int[] freeChampionIdsForNewPlayers;
	@JsonProperty("maxNewPlayerLevel")
    private int maxNewPlayerLevel;
	
    public Summoner() {}
    
	public void retrievePuuid() {
		// Encode summoner name for URL
		try {
			String encodedName = URLEncoder.encode(gameName, "UTF-8").replace("+", "%20");
			
			// Set up HTTP communication
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + encodedName + "/" + tagLine))
					.header("X-Riot-Token", apiKey)
					.GET()
					.build();
			HttpResponse<String> response = null;
			
			// send 'er bud
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			// Parse JSON response using Jackson JsonNode class
			ObjectMapper objectMapper = new ObjectMapper();
			System.out.println(response.body());
			JsonNode jsonNode = objectMapper.readTree(response.body());
			this.puuid = jsonNode.get("puuid").asText();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	public int[] getFreeChampionIds() {return freeChampionIds;}
//    public void setChampionId(int[] freeChampionIds) {this.freeChampionIds = freeChampionIds;}
	
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
			
			// Parse JSON response using Jackson
			Summoner temp = new Summoner();
			ObjectMapper objectMapper = new ObjectMapper();
			System.out.println(response.body());
			temp = objectMapper.readValue(response.body(), Summoner.class);
			this.freeChampionIds = temp.freeChampionIds;
			this.freeChampionIdsForNewPlayers = temp.freeChampionIdsForNewPlayers;
			this.maxNewPlayerLevel = temp.maxNewPlayerLevel;
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
			
			// Parse JSON response using Jackson
			ObjectMapper objectMapper = new ObjectMapper();
			Champion[] temp = objectMapper.readValue(response.body(), Champion[].class);
			this.topChamps = temp;
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