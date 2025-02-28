package com.altornot.AltOrNot;

public class App 
{
    public static void main( String[] args )
    {
    	Summoner summoner = new Summoner();
    	summoner.getInput();
		summoner.retrievePuuid();
		summoner.retrieveFreeChamps();
		summoner.retrieveTopChamps();
    }
}