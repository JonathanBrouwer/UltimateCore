package Bammerbom.UltimateCore.Commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.FormatUtil;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdHelp implements Listener{
	public static Plugin plugin;
	public CmdHelp(Plugin instance){
		plugin = instance;
	}
	public static void handle(CommandSender sender, String args[]){
		if(!r.isPlayer(sender)) return;
		Player p = (Player) sender;
	    String command = "help";
	    String pageStr = args.length > 0 ? args[0] : null;
	    String chapterPageStr = args.length > 1 ? args[1] : null;
	    UText input = new TextInput(p, false);
	    UText output;
	    if (input.getLines().isEmpty())
	    {
	      if ((r.isNumber(pageStr)) || (pageStr == null))
	      {
	        output = new HelpInput(p, "");
	      }
	      else
	      {
	        if (pageStr.length() > 26)
	        {
	          pageStr = pageStr.substring(0, 25);
	        }
	        output = new HelpInput(p, pageStr.toLowerCase(Locale.ENGLISH));
	        command = command.concat(" ").concat(pageStr);
	        pageStr = chapterPageStr;
	      }
	      chapterPageStr = null;
	    }
	    else
	    {
	      output = input;
	    }
	    TextPager pager = new TextPager(output);
	    pager.showPage(pageStr, chapterPageStr, command, p);
		
	}
}
class TextInput
implements UText
{
@SuppressWarnings({ "unchecked", "rawtypes" })
private static final HashMap<String, SoftReference<TextInput>> cache = new HashMap();
private final transient List<String> lines;
private final transient List<String> chapters;
private final transient Map<String, Integer> bookmarks;
private final transient long lastChange;

@SuppressWarnings({ "unchecked", "rawtypes" })
public TextInput(CommandSender sender, boolean createFile)
{
  File file = null;
  if (r.isPlayer(sender))
  {
    Player user = (Player) sender;
    file = new File(CmdHelp.plugin.getDataFolder(), "help_" + StringUtil.sanitizeFileName(user.getName()) + ".txt");
  }
  if ((file == null) || (!file.exists()))
  {
    file = new File(CmdHelp.plugin.getDataFolder(), "help.txt");
  }
  if (file.exists())
  {
    this.lastChange = file.lastModified();
    boolean readFromfile;
    synchronized (cache)
    {
	SoftReference inputRef = (SoftReference)cache.get(file.getName());
      TextInput input;
      if ((inputRef == null) || ((input = (TextInput)inputRef.get()) == null) || (input.lastChange < this.lastChange))
      {
        this.lines = new ArrayList();
        this.chapters = new ArrayList();
        this.bookmarks = new HashMap();
        cache.put(file.getName(), new SoftReference(this));
        readFromfile = true;
      }
      else
      {
        this.lines = Collections.unmodifiableList(input.getLines());
        this.chapters = Collections.unmodifiableList(input.getChapters());
        this.bookmarks = Collections.unmodifiableMap(input.getBookmarks());
        readFromfile = false;
      }
    }
    if (readFromfile)
    {
      Reader reader = null;
	try {
		reader = new InputStreamReader(new FileInputStream(file), "utf-8");
	} catch (UnsupportedEncodingException | FileNotFoundException e) {
		e.printStackTrace();
		return;
	}
      BufferedReader bufferedReader = new BufferedReader(reader);
      try
      {
        int lineNumber = 0;
        while (bufferedReader.ready())
        {
          String line = bufferedReader.readLine();
          if (line == null)
          {
            break;
          }
          if ((line.length() > 1) && (line.charAt(0) == '#'))
          {
            String[] titles = line.substring(1).trim().replace(" ", "_").split(",");
            this.chapters.add(FormatUtil.replaceFormat(titles[0]));
            for (String title : titles)
            {
              this.bookmarks.put(FormatUtil.stripEssentialsFormat(title.toLowerCase(Locale.ENGLISH)), Integer.valueOf(lineNumber));
            }
          }
          this.lines.add(FormatUtil.replaceFormat(line));
          lineNumber++;
        }
      } catch (IOException e) {
		e.printStackTrace();
	}
      finally
      {
        try {
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
      }
    }
  }
  else
  {
    this.lastChange = 0L;
    this.lines = Collections.emptyList();
    this.chapters = Collections.emptyList();
    this.bookmarks = Collections.emptyMap();
    if (createFile)
    {
      InputStream input = CmdHelp.plugin.getResource("help.txt");
      OutputStream output = null;
	try {
		output = new FileOutputStream(file);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
      try
      {
        byte[] buffer = new byte[1024];
        int length = input.read(buffer);
        while (length > 0)
        {
          output.write(buffer, 0, length);
          length = input.read(buffer);
        }
      } catch (IOException e) {
		e.printStackTrace();
	}
      finally
      {
        try {
			output.close();
	        input.close();
		} catch (Exception e) {
		}
      }
      return;
    }
  }
}

public List<String> getLines()
{
  return this.lines;
}

public List<String> getChapters()
{
  return this.chapters;
}

public Map<String, Integer> getBookmarks()
{
  return this.bookmarks;
}
}
class HelpInput
implements UText
{
private final transient List<String> lines = new ArrayList<String>();
private final transient List<String> chapters = new ArrayList<String>();
private final transient Map<String, Integer> bookmarks = new HashMap<String, Integer>();

@SuppressWarnings({ "unchecked", "rawtypes" })
public HelpInput(Player user, String match){
  boolean reported = false;
  List newLines = new ArrayList();
  String pluginName = "";
  String pluginNameLow = "";

  for (Plugin p : Bukkit.getServer().getPluginManager().getPlugins())
  {
    try
    {
      List pluginLines = new ArrayList();
      PluginDescriptionFile desc = p.getDescription();
      Map<String, Map<String, Object>> cmds = desc.getCommands();
      pluginName = p.getDescription().getName();
      pluginNameLow = pluginName.toLowerCase(Locale.ENGLISH);
      if (pluginNameLow.equals(match))
      {
        this.lines.clear();
        newLines.clear();
      }

      for (Map.Entry k : cmds.entrySet())
      {
        try
        {
          if ((pluginNameLow.contains(match)) || (((String)k.getKey()).toLowerCase(Locale.ENGLISH).contains(match))){
              Map value = (Map)k.getValue();
              Object permissions = null;
              if (value.containsKey("permission"))
              {
                permissions = value.get("permission");
              }
              else if (value.containsKey("permissions"))
              {
                permissions = value.get("permissions");
              }
              if(permissions == null || r.perm(user, permissions.toString(), false, false)){
                  pluginLines.add(ChatColor.GOLD + "" +  k.getKey() + ": " + ChatColor.YELLOW + value.get("description"));
              }
          }
        }
        catch (NullPointerException ex)
        {
        	ex.printStackTrace();
        }
      }

      if (!pluginLines.isEmpty())
      {
        newLines.addAll(pluginLines);
        if (pluginNameLow.equals(match))
        {
          break;
        }
        if (match.equalsIgnoreCase(""))
        {
        	lines.add((p.isEnabled() ? ChatColor.DARK_GREEN : ChatColor.RED) + pluginName + ": " + ChatColor.GOLD + "Plugin Help: " + ChatColor.YELLOW + "/help " + pluginNameLow);
        }
      }
    }
    catch (NullPointerException ex)
    {
    }
    catch (Exception ex)
    {
      if (!reported)
      {
      }
      reported = true;
    }
  }
  this.lines.addAll(newLines);
}

public List<String> getLines()
{
  return this.lines;
}

public List<String> getChapters()
{
  return this.chapters;
}

public Map<String, Integer> getBookmarks()
{
  return this.bookmarks;
}
}
abstract interface UText
{
  public abstract List<String> getLines();

  public abstract List<String> getChapters();

  public abstract Map<String, Integer> getBookmarks();
}
class TextPager
{
  private final transient UText text;
  private final transient boolean onePage;

  public TextPager(UText text)
  {
    this(text, false);
  }

  public TextPager(UText text, boolean onePage)
  {
    this.text = text;
    this.onePage = onePage;
  }

public void showPage(String pageStr, String chapterPageStr, String commandName, CommandSender sender)
  {
    List<String> lines = this.text.getLines();
    List<String> chapters = this.text.getChapters();
    Map<String, Integer> bookmarks = this.text.getBookmarks();

    if ((pageStr == null) || (pageStr.isEmpty()) || (pageStr.matches("[0-9]+")))
    {
      if ((!lines.isEmpty()) && (((String)lines.get(0)).startsWith("#")))
      {
        if (this.onePage)
        {
          return;
        }
        sender.sendMessage(ChatColor.GOLD + "Select chapter: ");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String string : chapters)
        {
          if (!first)
          {
            sb.append(", ");
          }
          first = false;
          sb.append(string);
        }
        sender.sendMessage(sb.toString());
        return;
      }

      int page = 1;
      try
      {
        page = Integer.parseInt(pageStr);
      }
      catch (NumberFormatException ex)
      {
        page = 1;
      }
      if (page < 1)
      {
        page = 1;
      }

      int start = this.onePage ? 0 : (page - 1) * 9;
      int end;
      for (end = 0; end < lines.size(); end++)
      {
        String line = (String)lines.get(end);
        if (line.startsWith("#"))
        {
          break;
        }
      }

      int pages = end / 9 + (end % 9 > 0 ? 1 : 0);
      if ((!this.onePage) && (commandName != null))
      {
        StringBuilder content = new StringBuilder();
        String[] title = commandName.split(" ", 2);
        if (title.length > 1)
        {
          content.append(capitalCase(title[0])).append(": ");
          content.append(title[1]);
        }
        else
        {
          content.append(capitalCase(commandName));
        }
        sender.sendMessage(r.default1 + " ---- " + r.default2 + content + " " + r.default1 + "--" + r.default2 + " Page " + Integer.valueOf(page) + "/" + Integer.valueOf(pages) + r.default1 + " ----");
      }
      for (int i = start; i < end; i++) { if (i >= start + (this.onePage ? 20 : 9))
          break;
        sender.sendMessage(new StringBuilder().append("§r").append((String)lines.get(i)).toString());
      }
      if ((!this.onePage) && (page < pages) && (commandName != null))
      {
    	  sender.sendMessage(r.default1 + "Type " + r.default2 + "/help " + Integer.valueOf(page + 1) + r.default1 + " to read the next page.");
      }
      return;
    }

    int chapterpage = 0;
    if (chapterPageStr != null)
    {
      try
      {
        chapterpage = Integer.parseInt(chapterPageStr) - 1;
      }
      catch (NumberFormatException ex)
      {
        chapterpage = 0;
      }
      if (chapterpage < 0)
      {
        chapterpage = 0;
      }

    }

    if (!bookmarks.containsKey(pageStr.toLowerCase(Locale.ENGLISH)))
    {
      sender.sendMessage(ChatColor.DARK_RED + "Unknown chapter.");
      return;
    }

    int chapterstart = ((Integer)bookmarks.get(pageStr.toLowerCase(Locale.ENGLISH))).intValue() + 1;

    int chapterend;
    for (chapterend = chapterstart; chapterend < lines.size(); chapterend++)
    {
      String line = (String)lines.get(chapterend);
      if ((line.length() > 0) && (line.charAt(0) == '#'))
      {
        break;
      }

    }

    int start = chapterstart + (this.onePage ? 0 : chapterpage * 9);
    int page = chapterpage + 1;
    int pages = (chapterend - chapterstart) / 9 + ((chapterend - chapterstart) % 9 > 0 ? 1 : 0);
    if ((!this.onePage) && (commandName != null))
    {
      StringBuilder content = new StringBuilder();
      content.append(capitalCase(commandName)).append(": ");
      content.append(pageStr);
      sender.sendMessage(r.default1 + " ---- " + r.default2 + content + " " + r.default1 + "--" + r.default2 + " Page " + Integer.valueOf(page) + "/" + Integer.valueOf(pages) + r.default1 + " ----");
    }
    for (int i = start; i < chapterend; i++) { if (i >= start + (this.onePage ? 20 : 9))
        break;
      sender.sendMessage(new StringBuilder().append("§r").append((String)lines.get(i)).toString());
    }
    if ((!this.onePage) && (page < pages) && (commandName != null))
    {
  	  sender.sendMessage(r.default1 + "Type " + r.default2 + "/help " + Integer.valueOf(page + 1) + r.default1 + " to read the next page.");
    }
  }
  private static String capitalCase(String input)
  {
    return input.toUpperCase(Locale.ENGLISH).charAt(0) + input.toLowerCase(Locale.ENGLISH).substring(1);
  }
}
enum KeywordType
{
 PLAYER(KeywordCachable.CACHEABLE), 
 DISPLAYNAME(KeywordCachable.CACHEABLE), 
 USERNAME(KeywordCachable.NOTCACHEABLE), 
 BALANCE(KeywordCachable.CACHEABLE), 
 MAILS(KeywordCachable.CACHEABLE), 
 WORLD(KeywordCachable.CACHEABLE), 
 WORLDNAME(KeywordCachable.CACHEABLE), 
 ONLINE(KeywordCachable.CACHEABLE), 
 UNIQUE(KeywordCachable.CACHEABLE), 
 WORLDS(KeywordCachable.CACHEABLE), 
 PLAYERLIST(KeywordCachable.SUBVALUE, true), 
 TIME(KeywordCachable.CACHEABLE), 
 DATE(KeywordCachable.CACHEABLE), 
 WORLDTIME12(KeywordCachable.CACHEABLE), 
 WORLDTIME24(KeywordCachable.CACHEABLE), 
 WORLDDATE(KeywordCachable.CACHEABLE), 
 COORDS(KeywordCachable.CACHEABLE), 
 TPS(KeywordCachable.CACHEABLE), 
 UPTIME(KeywordCachable.CACHEABLE), 
 IP(KeywordCachable.CACHEABLE, true), 
 ADDRESS(KeywordCachable.CACHEABLE, true), 
 PLUGINS(KeywordCachable.CACHEABLE, true), 
 VERSION(KeywordCachable.CACHEABLE, true);

 private final KeywordCachable type;
 private final boolean isPrivate;

 private KeywordType(KeywordCachable type) { this.type = type;
   this.isPrivate = false;
 }

 private KeywordType(KeywordCachable type, boolean isPrivate)
 {
   this.type = type;
   this.isPrivate = isPrivate;
 }

 public KeywordCachable getType()
 {
   return this.type;
 }

 public boolean isPrivate()
 {
   return this.isPrivate;
 }
}
enum KeywordCachable
{
 CACHEABLE, 
 SUBVALUE, 
 NOTCACHEABLE;
}