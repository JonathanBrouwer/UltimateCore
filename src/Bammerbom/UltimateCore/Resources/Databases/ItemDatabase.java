package Bammerbom.UltimateCore.Resources.Databases;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateCore;
import Bammerbom.UltimateCore.r;

public class ItemDatabase {
    static private UltimateCore plugin;
	public ItemDatabase(UltimateCore instance){
		plugin = instance;
	}
	  private final transient static Map<String, Integer> items = new HashMap<String, Integer>();
	  private final transient static Map<ItemData, List<String>> names = new HashMap<ItemData, List<String>>();
	  private final transient static Map<ItemData, String> primaryName = new HashMap<ItemData, String>();
	  private final transient static Map<String, Short> durabilities = new HashMap<String, Short>();
	public void disable(){
	}
	public void enable(){
	   InputStream resource = plugin.getResource("Data/items.csv");
	   BufferedReader r = new BufferedReader(new InputStreamReader(resource));
	   ArrayList<String> lines = new ArrayList<String>();
	   String lineC = "";
	   try {
		while((lineC = r.readLine()) != null){
			   lines.add(lineC);
		   }
	   } catch (IOException e) {e.printStackTrace();}
	    durabilities.clear();
	    items.clear();
	    names.clear();
	    primaryName.clear();
	    for (String line : lines)
	    {
	      line = line.trim().toLowerCase(Locale.ENGLISH);
	      if ((line.length() <= 0) || (line.charAt(0) != '#'))
	      {
	        String[] parts = line.split("[^a-z0-9]");
	        if (parts.length >= 2)
	        {
	          int numeric = Integer.parseInt(parts[1]);
	          short data = (parts.length > 2) && (!parts[2].equals("0")) ? Short.parseShort(parts[2]) : 0;
	          String itemName = parts[0].toLowerCase(Locale.ENGLISH);

	          durabilities.put(itemName, Short.valueOf(data));
	          items.put(itemName, Integer.valueOf(numeric));

	          ItemData itemData = new ItemData(numeric, data);
	          if (names.containsKey(itemData))
	          {
	            List<String> nameList = (List<String>)names.get(itemData);
	            nameList.add(itemName);
	            Collections.sort(nameList, new LengthCompare());
	          }
	          else
	          {
	            List<String> nameList = new ArrayList<String>();
	            nameList.add(itemName);
	            names.put(itemData, nameList);
	            primaryName.put(itemData, itemName);
	          }
	        }
	      }
	    }
	}
    @SuppressWarnings("deprecation")
	private static ItemStack get(String id){
	    int itemid = 0;
		String itemname = null;
		short metaData = 0;
		Matcher parts = Pattern.compile("((.*)[:+',;.](\\d+))").matcher(id);
		if (parts.matches()){
		    itemname = parts.group(2);
			metaData = Short.parseShort(parts.group(3));
	    }else{
			itemname = id;
		}
		if (r.isNumber(itemname)){
		    itemid = Integer.parseInt(itemname);
	    }
		else if (r.isNumber(id)){
			itemid = Integer.parseInt(id);
	    }else{
			itemname = itemname.toLowerCase(Locale.ENGLISH);
	    }
	    if (itemid < 1){
	        if (items.containsKey(itemname)){
			    itemid = ((Integer)items.get(itemname)).intValue();
			    if ((durabilities.containsKey(itemname)) && (metaData == 0)){
			        metaData = ((Short)durabilities.get(itemname)).shortValue();
			    }
	        }else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null){
			    Material bMaterial = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH));
			    itemid = bMaterial.getId();
		    }else{
			    try{
			        Material bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemname.toLowerCase(Locale.ENGLISH));
			        itemid = bMaterial.getId();
			    }
			    catch (Throwable throwable){
			          return null;
			    }
			}
	    }
		if (itemid < 1){
			return null;
        }
        Material mat = Material.getMaterial(itemid);
	    if (mat == null){
	        return null;
	    }
	    ItemStack retval = new ItemStack(mat);
		retval.setAmount(mat.getMaxStackSize());
		retval.setDurability(metaData);
		return retval;
    }
	public static ItemStack getItem(String str){
		if(str.contains("minecraft:")){
		    str = str.replaceFirst("minecraft:", "");
		}
		if(Material.matchMaterial(str) != null){
			Material mat = Material.matchMaterial(str);
			ItemStack stack = new ItemStack(mat);
			return stack;
		}
		return get(str);
		/*String search = "";
		String id = "";
		String dura = "";
		//1.7

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
	    
	    ItemStack stack = new ItemStack(Material.getMaterial(Integer.parseInt(id)));
	    stack.setDurability(Short.parseShort(dura));
	    return stack;*/
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
class ItemData
{
  private final int itemNo;
  private final short itemData;

  ItemData(int itemNo, short itemData)
  {
    this.itemNo = itemNo;
    this.itemData = itemData;
  }

  public int getItemNo()
  {
    return this.itemNo;
  }

  public short getItemData()
  {
    return this.itemData;
  }

  public int hashCode()
  {
    return 31 * this.itemNo ^ this.itemData;
  }

  public boolean equals(Object o)
  {
    if (o == null)
    {
      return false;
    }
    if (!(o instanceof ItemData))
    {
      return false;
    }
    ItemData pairo = (ItemData)o;
    return (this.itemNo == pairo.getItemNo()) && (this.itemData == pairo.getItemData());
  }
}
class LengthCompare
implements Comparator<String>
{
public LengthCompare()
{
}

public int compare(String s1, String s2)
{
  return s1.length() - s2.length();
}
}