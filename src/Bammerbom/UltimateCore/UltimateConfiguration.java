package Bammerbom.UltimateCore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import Bammerbom.UltimateCore.Resources.Utils.StreamUtil;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class UltimateConfiguration implements Cloneable{
	private final File file;
	private final YamlConfiguration conf;
	  private final Map<String, String> headers;
	private final Set<String> readkeys;
	public UltimateConfiguration(File file2){
		file = file2;
		conf = YamlConfiguration.loadConfiguration(file2);
		headers = new HashMap<String, String>();
		readkeys = new HashSet<String>();
		try {
			loadFromStream(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void loadFromStream(InputStream stream)
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
		        conf.loadFromString(builder.toString());
		      } catch (InvalidConfigurationException e) {
		        throw new IOException("YAML file is corrupt", e);
		      }
		    }
		    catch (FileNotFoundException ex)
		    {
		    }
		  }
	//TODO save
	public void save(){
		save(file);
	}
	public void save(File fi){
	    for (String key : conf.getKeys(true)) {
	        Object value = conf.get(key);
	        if ((value instanceof String)) {
	          String text = (String)value;
	          if (text.contains("\n")) {
	            conf.set(key, Arrays.asList(text.split("\n", -1)));
	          }
	        }
	      }

	      BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(StreamUtil.createOutputStream(fi)));
		} catch (SecurityException | IOException e1) {
			e1.printStackTrace();
		}
	      try
	      {
	    	  if (getHeader() != null)
	    	      for (String headerLine : getHeader().split("\n", -1)) {
	    	        StreamUtil.writeIndent(writer, 0);
	    	        writer.write("#> ");
	    	          writer.write(headerLine);
	    	        writer.newLine();
	    	      }
	        HashMap<Integer, String> anchorData = new HashMap<Integer, String>();

	        int anchId = -1; int anchDepth = 0; int anchIndent = 0;

	        StringBuilder refData = new StringBuilder();
	        NodeBuilder node = new NodeBuilder(getIndent());
	        for (String line : conf.saveToString().split("\n", -1)) {
	          line = StringUtil.colorToAmp(line);
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
		    	  if (getHeader() != null)
		    	      for (String headerLine : getHeader().split("\n", -1)) {
		    	        StreamUtil.writeIndent(writer, indent);
		    	        if (headerLine.trim().length() > 0) {
		    	          writer.write("# ");
		    	          writer.write(headerLine);
		    	        }
		    	        writer.newLine();
		    	      }

	            int refStart = line.indexOf("*id", node.getName().length());
	            int refEnd = line.indexOf(' ', refStart);
	            if (refEnd == -1) {
	              refEnd = line.length();
	            }
	            if ((refStart > 0) && (refEnd > refStart))
	            {
	              int refId = Integer.parseInt(line.substring(refStart + 3, refEnd), -1);
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
	              anchId = Integer.parseInt(line.substring(anchStart + 3, anchEnd), -1);
	              anchDepth = node.getDepth();
	              anchIndent = indent;
	              if (anchId >= 0)
	              {
	                anchEnd += StringUtil.getSuccessiveCharCount(line.substring(anchEnd), ' ');

	                refData.append(line.substring(anchEnd));

	                line = StringUtil.replace(line, anchStart, anchEnd, "");
	              }
	              wasAnchor = true;
	            }
	          }
	          if ((!wasAnchor) && (anchId >= 0))
	          {
	            refData.append('\n').append(StringUtil.getFilledString(" ", indent - anchIndent)).append(line);
	          }

	          if (StringUtil.containsChar('\n', line)) {
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
	      }catch(Exception ex){
	    	  ex.printStackTrace();
	      } finally {
	        try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	      }
	      
	}
	//TODO keys/values
	public Set<String> getKeys(){
		return conf.getKeys(true);
	}
	public Map<String, Object> getValues(){
		return conf.getValues(true);
	}
	//TODO file
	public File getFile(){
		return file;
	}
	//TODO headers
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
	  //TODO options
	  public YamlConfigurationOptions options(){
		  return conf.options();
	  }
	  //TODO confsection
	  public ConfigurationSection getConfigurationSection(String path){
		  return conf.getConfigurationSection(path);
	  }
    //TODO path
	  public String getPath(String append)
	  {
	    String p = getPath();
	    if (StringUtil.nullOrEmpty(append)) {
	      return p;
	    }
	    if (StringUtil.nullOrEmpty(p)) {
	      return append;
	    }
	    return p + "." + append;
	  }

	  public String getPath()
	  {
	    return conf.getCurrentPath();
	  }
	  //TODO name
	  public String getName()
	  {
	    return conf.getName();
	  }  
    //TODO indent
	  public void setIndent(int indent)
	  {
	    conf.options().indent(indent);
	  }

	  public int getIndent()
	  {
	    return conf.options().indent();
	  }
	  //TODO read
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
	  //TODO get
	  public boolean contains(String path){
		  return conf.contains(path);
	  }
	  public Object get(String path){
		  return conf.get(path);
	  }
	  public String getString(String path){
		  return conf.getString(path);
	  }
	  public Boolean getBoolean(String path){
		  return conf.getBoolean(path);
	  }
	  public Double getDouble(String path){
		  return conf.getDouble(path);
	  }
	  public Integer getInt(String path){
		  return conf.getInt(path);
	  }
	  public Long getLong(String path){
		  return conf.getLong(path);
	  }
	  public List<?> getList(String path){
		  return conf.getList(path);
	  }
	  public List<String> getStringList(String path){
		  return conf.getStringList(path);
	  }
	  //TODO set
	  public void set(String path, Object value){
		  if(value != null){
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
		  conf.set(path, value);
	  }
	  public void addDefault(String path, Object value){
		  if(!contains(path)){
			  set(path, value);
		  }
	  }
}
//TODO
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
//TODO
class NodeBuilder
{
  private LinkedList<String> nodes = new LinkedList<String>();
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