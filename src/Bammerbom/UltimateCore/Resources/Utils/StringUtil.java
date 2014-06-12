package Bammerbom.UltimateCore.Resources.Utils;

import java.util.Collection;

public class StringUtil
{
  public static String joinList(Object[] list)
  {
    return joinList(", ", list);
  }
  public static String consolidateStrings(String[] args, int start)
  {
    String ret = args[start];
    if (args.length > start + 1) {
      for (int i = start + 1; i < args.length; i++)
        ret = ret + " " + args[i];
    }
    return ret;
  }

  @SuppressWarnings("rawtypes")
public static String joinList(String seperator, Object[] list)
  {
    StringBuilder buf = new StringBuilder();
    for (Object each : list)
    {
      if (buf.length() > 0)
      {
        buf.append(seperator);
      }

      if ((each instanceof Collection))
      {
        buf.append(joinList(seperator, ((Collection)each).toArray()));
      }
      else
      {
        try
        {
          buf.append(each.toString());
        }
        catch (Exception e)
        {
          buf.append(each.toString());
        }
      }
    }
    return buf.toString();
  }
  public static String firstUpperCase(String input){
	  return input.substring(0, 1).toUpperCase() + input.substring(1);
  }
  public static boolean isAlphaNumeric(String s){
	    String pattern= "[a-zA-Z0-9]";
	        if(s.matches(pattern)){
	            return true;
	        }
	        return false;   
	}

}