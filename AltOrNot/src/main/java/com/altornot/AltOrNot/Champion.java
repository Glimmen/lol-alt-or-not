package com.altornot.AltOrNot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion {
	int championId = -1;
	int championPoints = -1;
	String puuid;
	
	public Champion() {}
	
	public int getChampionId() {return championId;}
    public void setChampionId(int championId) {this.championId = championId;}

    public int getChampionPoints() {return championPoints;}
    public void setChampionPoints(int championPoints) {this.championPoints = championPoints;}
}