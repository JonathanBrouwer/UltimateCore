package Bammerbom.UltimateCore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import Bammerbom.UltimateCore.Resources.Utils.StreamUtil;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class UltimateConfiguration extends BasicConfiguration
{
  private final File file;
  public static UltimateConfiguration loadConfiguration(File file){
	  return new UltimateConfiguration(file);
  }
  public UltimateConfiguration(String filename){
	  this(new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder(), filename));
  }
  public UltimateConfiguration(String filepath, String filename) {
    this(new File(filepath, filename));
  }

  public UltimateConfiguration(File file) {
    if (file == null) {
      throw new IllegalArgumentException("File is not allowed to be null!");
    }
    this.file = file;
    try {
		load();
	} catch (Exception e) {
		e.printStackTrace();
	}
  }

  public boolean exists() {
    return this.file.exists();
  }
  
  private void load() throws Exception
  {
    if (!this.file.exists()){
    	throw new Exception("File doesnt exists!");
    }
      
    try
    {
      loadFromStream(new FileInputStream(this.file));
    } catch (Throwable t) {
      r.log(ChatColor.RED + "Your confiruation " + file.getName() + " is invalid.");
      try {
        File backup = new File(this.file.getPath() + ".old");
        StreamUtil.copyFile(this.file, backup);
       r.log(ChatColor.RED + "A backup file " + backup.getName() + " has been made.");
      } catch (IOException ex) {
    	  ex.printStackTrace();
      }
    }
  }
  public void save(File f){
	    try
	    {
	      saveToStream(StreamUtil.createOutputStream(f));
	    }
	    catch (Exception ex) {
	      r.log(ChatColor.RED + "Failed to save file " + file.getName());
	      ex.printStackTrace();
	    }
  }
  public void save()
  {
    try
    {
      saveToStream(StreamUtil.createOutputStream(this.file));
    }
    catch (Exception ex) {
      r.log(ChatColor.RED + "Failed to save file " + file.getName());
      ex.printStackTrace();
    }
  }
}

class BasicConfiguration extends ConfigurationNode
{

  public String getPath()
  {
    return "";
  }

  public String getPath(String append)
  {
    return append == null || append.isEmpty() ? "" : append;
  }

  public void setIndent(int indent)
  {
    getSource().options().indent(indent);
  }

  public int getIndent()
  {
    return getSource().options().indent();
  }

  protected void loadFromStream(InputStream stream)
    throws IOException
  {
    try
    {
      InputStreamReader reader = new InputStreamReader(stream);
      StringBuilder builder = new StringBuilder();
      BufferedReader input = new BufferedReader(reader);
      try
      {
        HeaderBuilder header = new HeaderBuilder();
        NodeBuilder node = new NodeBuilder(getIndent());
        StringBuilder mainHeader = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
          //line = fixLine(line);
          int indent = StringUtil.getSuccessiveCharCount(line, ' ');
          String trimmedLine = line.substring(indent);

          if (trimmedLine.equals("*:")) {
            trimmedLine = "'*':";
            line = StringUtil.getFilledString(" ", indent) + trimmedLine;
          }

          if (trimmedLine.startsWith("#> ")) {
            mainHeader.append('\n').append(trimmedLine.substring("#> ".length()));
          }
          else if (!header.handle(trimmedLine))
          {
            node.handle(trimmedLine, indent);

            if (header.hasHeader()) {
              setHeader(node.getPath(), header.getHeader());
              header.clear();
            }
            builder.append(line).append('\n');
          }
        }
        if (mainHeader.length() > 0)
          setHeader(mainHeader.toString());
      }
      finally {
        input.close();
      }
      try {
        getSource().loadFromString(builder.toString());
      } catch (InvalidConfigurationException e) {
        throw new IOException("YAML file is corrupt", e);
      }
    }
    catch (FileNotFoundException ex)
    {
    	ex.printStackTrace();
    }
  }

  /*private String fixLine(String line)
  {
    String fixedLine = StringUtil.ampToColor(line);

    int count = StringUtil.getSuccessiveCharCount(fixedLine, '\t');
    if (count > 0) {
      fixedLine = StringUtil.getFilledString(" ", count * getIndent()) + fixedLine.substring(count);
    }
    return fixedLine;
  }*/

  private void writeHeader(boolean main, BufferedWriter writer, String header, int indent) throws IOException {
    if (header != null)
      for (String headerLine : header.split("\n", -1)) {
        writeIndent(writer, indent);
        if (main) {
          writer.write("#> ");
          writer.write(headerLine);
        } else if (headerLine.trim().length() > 0) {
          writer.write("# ");
          writer.write(headerLine);
        }
        writer.newLine();
      }
  }
  private static void writeIndent(BufferedWriter writer, int indent) throws IOException {
	    for (int i = 0; i < indent; i++)
	      writer.write(32);
	  }

  protected void saveToStream(OutputStream stream)
		    throws IOException
		  {
		    for (String key : getSource().getKeys(true)) {
		      Object value = getSource().get(key);
		      if ((value instanceof String)) {
		        String text = (String)value;
		        if (text.contains("\n")) {
		          getSource().set(key, Arrays.asList(text.split("\n", -1)));
		        }
		      }
		    }

		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
		    try
		    {
		      writeHeader(true, writer, getHeader(), 0);

		      HashMap<Integer, Object> anchorData = new HashMap<Integer, Object>();

		      int anchId = -1; int anchDepth = 0; int anchIndent = 0;

		      StringBuilder refData = new StringBuilder();
		      NodeBuilder node = new NodeBuilder(getIndent());
		      for (String line : getSource().saveToString().split("\n", -1)) {
		    	  //line = StringUtil.colorToAmp(line);
		        int indent = StringUtil.getSuccessiveCharCount(line, ' ');
		        line = line.substring(indent);
		        boolean wasAnchor = false;

		        if (line.equals("'*':")) {
		          line = "*:";
		        }

		        if (node.handle(line, indent))
		        {
		          if ((anchId >= 0) && (node.getDepth() <= anchDepth)) {
		            anchorData.put(anchId, refData.toString());
		            refData.setLength(0);
		            anchId = -1;
		          }

		          writeHeader(false, writer, getHeader(node.getPath()), indent);

		          int refStart = line.indexOf("*id", node.getName().length());
		          int refEnd = line.indexOf(' ', refStart);
		          if (refEnd == -1) {
		            refEnd = line.length();
		          }
		          if ((refStart > 0) && (refEnd > refStart))
		          {
		            int refId = parseInt(line.substring(refStart + 3, refEnd), -1);
		            if (refId >= 0)
		            {
		              String data = (String)anchorData.get(refId);
		              if (data != null)
		              {
		                line = StringUtil.trimEnd(line.substring(0, refStart)) + " " + data;
		              }
		            }

		          }

		          int anchStart = line.indexOf("&id", node.getName().length());
		          int anchEnd = line.indexOf(' ', anchStart);
		          if (anchEnd == -1) {
		            anchEnd = line.length();
		          }
		          if ((anchStart > 0) && (anchEnd > anchStart))
		          {
		            anchId = parseInt(line.substring(anchStart + 3, anchEnd), -1);
		            anchDepth = node.getDepth();
		            anchIndent = indent;
		            if (anchId >= 0)
		            {
		              anchEnd += StringUtil.getSuccessiveCharCount(line.substring(anchEnd), ' ');

		              refData.append(line.substring(anchEnd));

		              //line = StringUtil.replace(line, anchStart, anchEnd, "");
		            }
		            wasAnchor = true;
		          }
		        }
		        if ((!wasAnchor) && (anchId >= 0))
		        {
		          refData.append('\n').append(StringUtil.getFilledString(" ", indent - anchIndent)).append(line);
		        }
		    	  line = line.replaceAll("\\\\xa7", "&").replaceAll("\"", "'");
		        if (containsChar('\n', line)) {
		          for (String part : line.split("\n", -1)) {
		            StreamUtil.writeIndent(writer, indent);
		            writer.write(part);
		            writer.newLine();
		          }
		        } else {
		          StreamUtil.writeIndent(writer, indent);
		          writer.write(line);
		          writer.newLine();
		        }
		      }
		    } finally {
		      writer.close();
		    }
		  }
  private static boolean containsChar(char value, CharSequence sequence)
  {
    for (int i = 0; i < sequence.length(); i++) {
      if (sequence.charAt(i) == value) {
        return true;
      }
    }
    return false;
  }

  private static int parseInt(String text, int def)
  {
    return parseInt(text, Integer.valueOf(def)).intValue();
  }

  private static Integer parseInt(String text, Integer def)
  {
    return r.isNumber(text) ? Integer.parseInt(text) : def;
  }
}
class ConfigurationNode
implements Cloneable
{
private final MemorySection sourc;
private final YamlConfiguration source;
private final Map<String, String> headers;
private final Set<String> readkeys;

@SuppressWarnings({ "unchecked", "rawtypes" })
public ConfigurationNode()
{
  this(new HashSet(), new HashMap(), new YamlConfiguration());
}

private ConfigurationNode(ConfigurationNode source, String root) {
  this.readkeys = source.readkeys;
  this.headers = source.headers;
  MemorySection sect = (MemorySection)source.source.getConfigurationSection(root);
  if (sect == null){
    this.sourc = ((MemorySection)source.sourc.createSection(root));
  this.source = (YamlConfiguration) sourc.getRoot();
  } else {
    this.sourc = sect;
    this.source = (YamlConfiguration) sourc.getRoot();
  }
  setRead();
}

private ConfigurationNode(Set<String> readkeys, Map<String, String> headers, MemorySection source) {
  this.readkeys = readkeys;
  this.sourc = source;
  this.headers = headers;
  this.source = (YamlConfiguration) sourc.getRoot();
}
//
public long getLong(String path){
	return source.getLong(path);
}
public boolean getBoolean(String path){
	return source.getBoolean(path);
}
public String getString(String path){
	return source.getString(path);
}
public double getDouble(String path){
	return source.getDouble(path);
}
public int getInt(String path){
	return source.getInt(path);
}
public ConfigurationSection getConfigurationSection(String path){
	return source.getConfigurationSection(path);
}
public void addDefault(String s, Object o){
	if(!source.contains(s)){
		set(s, o);
	}
}



//
public boolean hasParent()
{
  return this.source.getParent() != null;
}

public boolean isEmpty()
{
  return getKeys().isEmpty();
}

public ConfigurationNode getParent()
{
  MemorySection sec = (MemorySection)this.source.getParent();
  if (sec == null) {
    return null;
  }
  return new ConfigurationNode(this.readkeys, this.headers, sec);
}

public String getPath(String append)
{
  String p = getPath();
  if (append == null || append.isEmpty()) {
    return p;
  }
  if (p == null || p.isEmpty()) {
    return append;
  }
  return p + "." + append;
}

public String getPath()
{
  return this.source.getCurrentPath();
}

public String getName()
{
  return this.source.getName();
}

public String getHeader()
{
  return (String)this.headers.get(getPath());
}

public String getHeader(String path)
{
  return (String)this.headers.get(getPath(path));
}

public void removeHeader()
{
  setHeader(null);
}

public void removeHeader(String path)
{
  setHeader(path, null);
}

public void setHeader(String header)
{
  setHeader("", header);
}

public void setHeader(String path, String header)
{
  if (header == null)
    this.headers.remove(getPath(path));
  else
    this.headers.put(getPath(path), header);
}

public void addHeader(String header)
{
  addHeader("", header);
}

public void addHeader(String path, String header)
{
  String oldheader = getHeader(path);
  if (oldheader == null)
    setHeader(path, header);
  else
    setHeader(path, oldheader + "\n" + header);
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public Map<String, String> getHeaders()
{
  String root = getPath();
  Map rval = new HashMap(this.headers.size());
  if (root == null || root.isEmpty())
    rval.putAll(this.headers);
  else {
    for (Map.Entry entry : this.headers.entrySet()) {
      if (((String)entry.getKey()).startsWith(root)) {
        rval.put(entry.getKey(), entry.getValue());
      }
    }
  }
  return rval;
}

@SuppressWarnings("rawtypes")
public void clearHeaders()
{
  String root = getPath();
  if ((root == null) || (root.length() == 0)) {
    this.headers.clear();
  } else {
    Iterator iter = this.headers.entrySet().iterator();
    while (iter.hasNext())
      if (((String)((Map.Entry)iter.next()).getKey()).startsWith(root))
        iter.remove();
  }
}

public MemorySection getSection()
{
  return this.source;
}

public YamlConfiguration getSource()
{
  return (YamlConfiguration)this.source.getRoot();
}

public boolean isNode(String path)
{
  return this.source.isConfigurationSection(path);
}

public ConfigurationNode getNode(String path)
{
  return new ConfigurationNode(this, path);
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public Set<ConfigurationNode> getNodes()
{
  Set rval = new HashSet();
  for (String path : getKeys()) {
    if (isNode(path)) {
      rval.add(getNode(path));
    }
  }
  return rval;
}

public Map<String, Object> getValues()
{
  return this.source.getValues(false);
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public <T> Map<String, T> getValues(Class<T> type)
{
  Map values = getValues();
  Iterator iter = values.entrySet().iterator();
  while (iter.hasNext()) {
    Map.Entry entry = (Map.Entry)iter.next();
    Object newvalue = entry.getValue();
    if (newvalue == null)
      iter.remove();
    else {
      entry.setValue(newvalue);
    }
  }
  return values;
}

public Set<String> getKeys()
{
  return this.source.getKeys(false);
}

public ConfigurationNode clone()
{
  ConfigurationNode cloned = new ConfigurationNode();
  cloned.setHeader(getHeader());
  for (String key : getKeys()) {
    if (isNode(key))
      cloned.set(key, getNode(key).clone());
    else {
      cloned.set(key, get(key));
    }
  }
  return cloned;
}

public boolean isRead() {
  return isRead(null);
}

public boolean isRead(String path) {
  return this.readkeys.contains(getPath(path));
}

public void setRead() {
  setRead(null);
}

public void setRead(String path) {
  setReadFullPath(getPath(path));
}

private void setReadFullPath(String path) {
  if (this.readkeys.add(path)) {
    int dotindex = path.lastIndexOf('.');
    if (dotindex > 0)
      setReadFullPath(path.substring(0, dotindex));
  }
}

public void trim()
{
  for (String key : getKeys())
    if (isRead(key)) {
      if (isNode(key))
        getNode(key).trim();
    }
    else
      remove(key);
}

public boolean contains(String path)
{
  return this.source.contains(path);
}

public void clear()
{
  for (String key : getKeys())
    remove(key);
}

public void remove(String path)
{
  set(path, null);
}

public void set(String path, Object value)
{
  if (value != null) {
    setRead(path);
    if (value.getClass().isEnum()) {
      String text = value.toString();
      if (text.equals("true"))
        value = Boolean.valueOf(true);
      else if (text.equals("false"))
        value = Boolean.valueOf(false);
      else {
        value = text;
      }
    }
  }
  this.source.set(path, value);
}

@SuppressWarnings("rawtypes")
public List getList(String path)
{
  setRead(path);
  return this.source.getList(path);
}
@SuppressWarnings("unchecked")
public List<String> getStringList(String path){
	return getList(path);
}

@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> List<T> getList(String path, Class<T> type)
{
  return getList(path, type, new ArrayList());
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public <T> List<T> getList(String path, Class<T> type, List<T> def)
{
  List list = getList(path);
  Iterator i$;
  if (list != null) {
    def = new ArrayList();

    for (i$ = list.iterator(); i$.hasNext(); ) { Object o = i$.next();
      Object val = o;
      if (val != null)
        def.add((T) val);
    }
  }
  set(path, def);
  return def;
}

public Object get(String path)
{
	return source.get(path);
}

public Object get(String path, Object def)
{
    return source.get(path) != null ? source.get(path) : def;
}

public void shareWith(Map<String, Object> target, String path, Object def)
{
  Object value = get(path);
  if (value != null) {
    target.put(path, value);
  } else {
    value = target.get(path);
    if (value == null) {
      value = def;
      target.put(path, value);
    }
    set(path, value);
  }
}
}
class HeaderBuilder
{
  private StringBuilder buffer = new StringBuilder();

  private StringBuilder add() {
    return this.buffer.append('\n');
  }

  public boolean handle(String line)
  {
    if (line.isEmpty())
      add().append(' ');
    else if (line.startsWith("# "))
      add().append(line.substring(2));
    else if (line.startsWith("#"))
      add().append(line.substring(1));
    else {
      return false;
    }
    return true;
  }

  public void clear()
  {
    this.buffer.setLength(0);
  }

  public boolean hasHeader()
  {
    return this.buffer.length() > 0;
  }

  public String getHeader()
  {
    return hasHeader() ? this.buffer.substring(1) : null;
  }
}
class NodeBuilder
{
  @SuppressWarnings({ "unchecked", "rawtypes" })
private LinkedList<String> nodes = new LinkedList();
  private final int indent;

  public NodeBuilder(int indent)
  {
    this.indent = indent;
  }

  public boolean handle(String line, int preceedingSpaces)
  {
    if (line.startsWith("#")) {
      return false;
    }
    int nodeIndex = preceedingSpaces / this.indent;
    String nodeName = StringUtil.getLastBefore(line, ":");
    if (!nodeName.isEmpty())
    {
      while (this.nodes.size() >= nodeIndex + 1) {
        this.nodes.pollLast();
      }
      this.nodes.offerLast(nodeName);
      return true;
    }
    return false;
  }

  public String getName()
  {
    return (String)this.nodes.peekLast();
  }

  public int getDepth()
  {
    return this.nodes.size();
  }

  public String getPath()
  {
    return StringUtil.join(".", this.nodes);
  }
}