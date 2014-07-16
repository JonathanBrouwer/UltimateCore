package Bammerbom.UltimateCore.Minigames;

import java.util.ArrayList;

import org.bukkit.Location;

import Bammerbom.UltimateCore.API.UC;
 
/**
*
* @Author Jake + Edit by Bammerbom
*/
public class Arena {
 
//A list of all the Arena Objects
public static ArrayList<Arena> arenaObjects = new ArrayList<Arena>();
 
//Some fields we want each Arena object to store:
private Location joinLocation, startLocation, endLocation; //Some general arena locations
 
private String name; //Arena name
private ArrayList<String> players = new ArrayList<String>(); //And arraylist of players name
private ArrayList<String> spectators = new ArrayList<String>();
 
private int maxPlayers;
private int startPlayers;

private String minigameType;

public boolean isCountdown;
 
private boolean inGame = false; //Boolean to determine if an Arena is ingame or not, automaticly make it false
 
 
//Now for a Constructor:
public Arena (String minigame, String arenaName, Location joinLocation, Location startLocation, Location endLocation, int maxPlayers, int startPlayers) { //So basicly: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)
//Lets initalize it all:
this.name = arenaName;
this.joinLocation = joinLocation;
this.startLocation = startLocation;
this.endLocation = endLocation;
this.maxPlayers = maxPlayers;
this.isCountdown = false;
this.minigameType = minigame;
//Now lets add this object to the list of objects:
arenaObjects.add(this);
 
}
 
//Now for some Getters and Setters, so with our arena object, we can use special methods:
public String getMinigameType(){
	return minigameType;
}
public void setMinigameType(String str){
	this.minigameType = str;
}
public Location getJoinLocation() {
return this.joinLocation;
}
 
public void setJoinLocation(Location joinLocation) {
this.joinLocation = joinLocation;
}
 
public Location getStartLocation() {
return this.startLocation;
}
 
public void setStartLocation(Location startLocation) {
this.startLocation = startLocation;
}
 
public Location getEndLocation() {
return this.endLocation;
}
 
public void setEndLocation(Location endLocation) {
this.endLocation = endLocation;
}
 
public String getName() {
return this.name;
}
 
public void setName(String name) {
this.name = name;
}
 
public int getMaxPlayers() {
return this.maxPlayers;
}
 
public void setMaxPlayers(int maxPlayers) {
this.maxPlayers = maxPlayers;
}
public int getStartPlayers(){
	return this.startPlayers;
}
public void setStartPlayers(int str){
	this.startPlayers = str;
}
 
public ArrayList<String> getPlayers() {
return this.players;
}

public ArrayList<String> getSpectators() {
	return this.spectators;
}

 
 
//And finally, some booleans:
public boolean isFull() { //Returns weather the arena is full or not
return players.size() >= maxPlayers;
}
public boolean canStart(){
	return players.size() >= startPlayers;
}

public boolean isInCountdown(){
	return this.isCountdown;
}
public void setCountdown(Boolean c){
	this.isCountdown = c;
}
 
public boolean isInGame() {
return inGame;
}
 
public void setInGame(boolean inGame) {
this.inGame = inGame;
}
 
//To send each player in the arena a message
public void sendMessage(String message) {
for (String s: players) {
UC.searchPlayer(s).sendMessage(message);
}
for(String s : spectators){
	UC.searchPlayer(s).sendMessage(message);
}
}
 
 
}
 