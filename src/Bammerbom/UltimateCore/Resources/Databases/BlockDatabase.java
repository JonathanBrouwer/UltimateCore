package Bammerbom.UltimateCore.Resources.Databases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class BlockDatabase {
    public Plugin plugin;
	public BlockDatabase(Plugin instance){
		plugin = instance;
	}
	public void disable(){
		save();
	}
	public void enable(){
	    try {
		//File
	    	Boolean exist = true;
	    File file = new File(plugin.getDataFolder() + File.separator + "Data", "BlockDatabase.db");
	    if(!file.exists()){
	    	file.createNewFile();
	    	exist = false;
	    }
	    //Connection
		Connection c = null;
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/Data/BlockDatabase.db");
		    r.log(ChatColor.YELLOW + "Loaded database.");
	    //Table
		    if(!exist){
	    Statement stmt = null;
	    stmt = c.createStatement();
	      String sql = "CREATE TABLE BLOCKDATA " +
	    		  " (ID    INTEGER PRIMARY KEY, " + 
                  " PLAYERID           TEXT    NOT NULL, " +
	    		  " TIME           DATETIME    NOT NULL, " + 
                  " LOCATION           TEXT    NOT NULL, " + 
                  " ACTION            TEXT     NOT NULL, " + 
                  " SPECIAL        TEXT, " + 
                  " SPECIAL2         TEXT)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
		    }
	      c.close();
	    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage());
		      e.printStackTrace();
		    }
	    Thread t = new Thread(
	    new Runnable(){
			@Override
			public void run() {
		    	Connection c = null;
			    try{
				      Class.forName("org.sqlite.JDBC");
				      c = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/Data/BlockDatabase.db");
			    	if(c == null){ return; }
				      String execute = "DELETE FROM BLOCKDATA WHERE TIME <= date('now', '-1 month')";
			    	Statement s = c.createStatement();
			    	s.executeUpdate(execute);
			    	
			    	
		    }catch( Exception e) {
		    	e.printStackTrace();
		    }finally{
		    	if(c != null)
					try {
						c.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		    }
			}
	    });
	    t.start();
	}
	ArrayList<SQLset> unsavedsets = new ArrayList<SQLset>();
	public void add(String playerid, Date date, Location loc, BlockAction action, String special, String special2){
		SQLset set = new SQLset(date, playerid, loc,action,special,special2);
		unsavedsets.add(set);
	}
	public void add(SQLset set){
		unsavedsets.add(set);
	}
	public void save(){
		Thread thread = new Thread(
				new Runnable(){
					public void run(){
						Connection c = null;
					    try {
					      Class.forName("org.sqlite.JDBC");
					      c = DriverManager.getConnection("jdbc:sqlite:Plugins/UltimateCore/Data/BlockDatabase.db");
					      c.setAutoCommit(false);
					      for(SQLset set : unsavedsets){
					  	    Statement stmt = null;
					      stmt = c.createStatement();
					      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					      String date = sdf.format(set.date);
					      String loc = "" + set.loc.getWorld().getName() + " " + set.loc.getBlockX() + " " + set.loc.getBlockY() + " " + set.loc.getBlockZ() + "";
					      String sql = "INSERT INTO BLOCKDATA (PLAYERID, TIME, LOCATION, ACTION, SPECIAL, SPECIAL2) " +
					                   "VALUES ('" + set.playerid.toString() + "', '" + date + "', '" + loc + "', '" + set.action + "', '" + set.special + "', '" + set.special2 + "')";
					      stmt.executeUpdate(sql);
					      stmt.close();
					      }
					      unsavedsets.clear();
					      c.commit();
					      c.close();
					    } catch ( Exception e ) {
					    	if(e.getStackTrace().toString().contains("locked")){ r.log(r.error + "BlockDatabase locked."); }else{
					      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
					      e.printStackTrace();
					    	}
					    }
					}
				});
		thread.start();
	}
	public ArrayList<SQLset2> getActionsAt(Location loc){
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:Plugins/UltimateCore/Data/BlockDatabase.db");
	      c.setAutoCommit(false);
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM BLOCKDATA WHERE LOCATION='" + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "';" );
	      ArrayList<SQLset2> str = new ArrayList<SQLset2>();
	      while ( rs.next() ) {
	         String playerid = rs.getString("playerid");
	         String location = rs.getString("location");
	         String action = rs.getString("action");
	         String special = rs.getString("special");
	         String special2 = rs.getString("special2");
	         String date = rs.getString("time");
	         SQLset2 set = new SQLset2(date,playerid,location,action,special,special2);
	         str.add(set);
	      }
	      rs.close();
	      c.commit();
	      c.close();
	      return str;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      e.printStackTrace();
	    }
		return null;
	}
	public enum BlockAction{
		BLOCKBREAK, BLOCKPLACE, SIGNTEXT, IGNITE, BURN, TOGGLE, GROW, PISTONMOVE, LEAVESECAY, BLOCKFORM, MELT, SPREAD, SNOWFORM, ICEFORM, LIQUIDSPREAD, DRAGONEGGTELEPORT, PORTALCREATE, EXPLODE, BUCKETFILL, BUCKETEMPTY;
	}
	public class SQLset{
		public SQLset(Date date2, String playerid2, Location loc2, BlockAction action2, String special3, String special4){
			loc = loc2;
			action = action2;
			special = special3;
			special2 = special4;
			playerid = playerid2;
			date = date2;
		}
		public Date date;
		public Location loc;
		public BlockAction action;
		public String special;
		public String special2;
		public String playerid;
	}
	public class SQLset2{
		public SQLset2(String date2, String playerid2, String loc2, String action2, String special3, String special4){
			loc = loc2;
			action = action2;
			special = special3;
			special2 = special4;
			playerid = playerid2;
			date = date2;
		}
		public String date;
		public String loc;
		public String action;
		public String special;
		public String special2;
		public String playerid;
	}
}
