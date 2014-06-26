package Bammerbom.UltimateCore.Resources.Databases;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class ItemDatabase {
    static private Plugin plugin;
	public ItemDatabase(Plugin instance){
		plugin = instance;
	}
	public void disable(){
	}
	public void enable(){
	    try {
		//File
	    	@SuppressWarnings("unused")
			Boolean exist = true;
	    File file = new File(plugin.getDataFolder() + File.separator + "Data", "ItemDatabase.db");
	    if(!file.exists()){
	    	plugin.saveResource("Data/ItemDatabase.db", true);
	    	exist = false;
	    }
	    //Connection
		Connection c = null;
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/Data/ItemDatabase.db");
	    //Table REMOVED
		    /*if(!exist){
	    Statement stmt = null;
	    stmt = c.createStatement();
	      String sql = "CREATE TABLE ITEMDATA " +
	    		  " (NAME    TEXT PRIMARY KEY, " + 
                  " ID           TEXT    NOT NULL, " +
	    		  " DURA           TEXT    NOT NULL)";
	      stmt.executeUpdate(sql);
	      stmt.close();
	      addKeys();
		    }*/
	      c.close();
	    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage());
		      e.printStackTrace();
		    }
	}
	@SuppressWarnings("deprecation")
	public static ItemStack getItem(String str){
		String search = "";
		String id = "";
		String dura = "";
		//1.7
		if(str.contains("minecraft:")){
		    str = str.replaceFirst("minecraft:", "");
		}
		//
		if(!str.contains(":") && !r.isNumber(str)){
			search = "NAME='" + str.toLowerCase() + "'";
		}else if(str.contains(":") && !r.isNumber(str.split(":")[0])){
			search = "NAME='" + str.split(":")[0].toLowerCase() + "'";
			dura = str.split(":")[1];
		}else if(str.contains(":") && r.isNumber(str.split(":")[0])){
			search = "ID='" + str.split(":")[0].toLowerCase() + "'";
			dura = str.split(":")[1];
		}else if(!str.contains(":") && r.isNumber(search)){
			search = "ID='" + str.toLowerCase() + "'"; 
		}
		//1.7
		if(Material.matchMaterial(str) != null){
			Material mat = Material.matchMaterial(str);
			ItemStack stack = new ItemStack(mat);
			if(dura != null && !dura.equalsIgnoreCase("")){
				stack.setDurability(Short.parseShort(dura));
			}
			return stack;
		}
		
		//
	    try {
		Connection c = null;
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:Plugins/UltimateCore/Data/ItemDatabase.db");
	      Statement stmt = null;
		    stmt = c.createStatement();
		    String sql = "SELECT * FROM ITEMDATA WHERE " + search;
		    ResultSet set = stmt.executeQuery(sql);
		    //if(set.isClosed()) return null;
		    id = set.getString("ID");
		    if(dura == null || dura.equalsIgnoreCase("")){
		    dura = set.getString("DURA");
		    }
		    stmt.close();
		    c.close();
	    } catch ( Exception e ) {
	    	if(e.getStackTrace().toString().contains("locked")){ r.log(r.error + "ItemDatabase locked."); }else{
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      e.printStackTrace();
	    	}
	    	return null;
	    }
	    ItemStack stack = new ItemStack(Material.getMaterial(Integer.parseInt(id)));
	    stack.setDurability(Short.parseShort(dura));
	    return stack;
	}
	@SuppressWarnings("unused")
	@Deprecated
	private static void addKeys(){
		try{
		File file = new File(plugin.getDataFolder() + "/Data", "items.csv");
		plugin.saveResource("Data/items.csv", true);
	    ManagedFile mfile = new ManagedFile("Data/items.csv", plugin);
	    Connection c = null;
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:Plugins/UltimateCore/Data/ItemDatabase.db");
  	    Statement stmt = null;
  	    stmt = c.createStatement();
  	    Integer a = 0;
  	    Integer b = mfile.getLines().size();
	      for(String line : mfile.getLines()){
	    	if(!line.startsWith("#")){
	    		a++;
	    		if(a % 100 == 0){
	    		  r.log("Progress: " + a + '/' + b);
	    		}
	  	  String lname = line.split(",")[0].toLowerCase();
	  	  Integer lID = Integer.parseInt(line.split(",")[1].toLowerCase());
	  	  Short lDURA = Short.parseShort(line.split(",")[2].toLowerCase());
	      String sql = "INSERT INTO ITEMDATA (NAME, ID, DURA) " +
                  "VALUES ('" + lname + "', '" + lID + "', '" + lDURA + "')";
          stmt.executeUpdate(sql);
	    	}
	    }
          stmt.close();
	    file.delete();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

class ManagedFile
{
  private final transient File file;

  public ManagedFile(String filename, Plugin instance)
  {
    this.file = new File(instance.getDataFolder(), filename);

    if (this.file.exists())
    {
      try
      {
        if ((checkForVersion(this.file, instance.getDescription().getVersion())) && (!this.file.delete()))
        {
          throw new IOException("Could not delete file " + this.file.toString());
        }
      }
      catch (IOException ex)
      {
        Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
      }
    }

    if (!this.file.exists())
    {
      try
      {
        copyResourceAscii("/" + filename, this.file);
      }
      catch (IOException ex)
      {
      }
    }
  }

  public static void copyResourceAscii(String resourceName, File file) throws IOException
  {
    InputStreamReader reader = new InputStreamReader(ManagedFile.class.getResourceAsStream(resourceName));
    try
    {
      MessageDigest digest = getDigest();
      DigestOutputStream digestStream = new DigestOutputStream(new FileOutputStream(file), digest);
      try
      {
        OutputStreamWriter writer = new OutputStreamWriter(digestStream);
        try
        {
          char[] buffer = new char[8192];
          while (true)
          {
            int length = reader.read(buffer);
            if (length < 0)
              break;
            writer.write(buffer, 0, length);
          }

          writer.write("\n");
          writer.flush();
          BigInteger hashInt = new BigInteger(1, digest.digest());
          digestStream.on(false);
          digestStream.write(35);
          digestStream.write(hashInt.toString(16).getBytes());
        }
        finally
        {
        	writer.close();
        }

      }
      finally
      {
      }

    }
    finally
    {
      reader.close();
    }
  }

  public static boolean checkForVersion(File file, String version) throws IOException
  {
    if (file.length() < 33L)
    {
      return false;
    }
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
    try
    {
      byte[] buffer = new byte[(int)file.length()];
      int position = 0;
      do
      {
        int length = bis.read(buffer, position, Math.min((int)file.length() - position, 8192));
        if (length < 0)
        {
          break;
        }
        position += length;
      }
      while (position < file.length());
      ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
      if (bais.skip(file.length() - 33L) != file.length() - 33L)
      {
        return false;
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
      try
      {
        String hash = reader.readLine();
        if ((hash != null) && (hash.matches("#[a-f0-9]{32}")))
        {
          hash = hash.substring(1);
          bais.reset();
          String versionline = reader.readLine();
          if ((versionline != null) && (versionline.matches("#version: .+")))
          {
            String versioncheck = versionline.substring(10);
            if (!versioncheck.equalsIgnoreCase(version))
            {
              bais.reset();
              MessageDigest digest = getDigest();
              DigestInputStream digestStream = new DigestInputStream(bais, digest);
              try
              {
                byte[] bytes = new byte[(int)file.length() - 33];
                digestStream.read(bytes);
                BigInteger correct = new BigInteger(hash, 16);
                BigInteger test = new BigInteger(1, digest.digest());
                if (correct.equals(test))
                {
                  return true;
                }

              }
              finally
              {
              }
            }
          }

        }

      }
      finally
      {
      }

    }
    finally
    {
      bis.close();
    }
    return false;
  }

  public static MessageDigest getDigest() throws IOException
  {
    try
    {
      return MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw new IOException(ex);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public List<String> getLines()
  {
    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(this.file));
      try {
        List lines = new ArrayList();
        String line;
        while (true) {
          line = reader.readLine();
          if (line == null)
          {
            break;
          }

          lines.add(line);
        }

        return lines;
      }
      finally
      {
        reader.close();
      }
    }
    catch (IOException ex)
    {
      Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
    }return Collections.emptyList();
  }
}