package Bammerbom.UltimateCore.Resources;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import net.minecraft.util.org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("serial")
class LogWindow extends JFrame{
  public int width;

  public int height;

  public JTextArea textArea = null;

  public JScrollPane pane = null;

  public LogWindow(String title, int width, int height, Plugin plugin) {
    super(title);
    setSize(width, height);
    setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - 400, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 300);
    textArea = new JTextArea();
    pane = new JScrollPane(textArea);
    DefaultCaret caret = (DefaultCaret)textArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    pane.setAutoscrolls(true);
    getContentPane().add(pane);
    textArea.setEditable(false);
    textArea.setForeground(null);
    textArea.setBackground(new Color(245,245,245));
    //
    final JTextField text = new JTextField();
    text.setForeground(new Color(80,80,80));
    text.setText("Write a command here...");
    text.setFocusable(true);
    text.setVisible(true);
    text.setBackground(new Color(230,230,230));
    text.setToolTipText("Write a command to execute...");
    text.addKeyListener(new KeyListener(){
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 10){
				if(!text.getText().equalsIgnoreCase("Write a command here...")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text.getText());
			text.setText("Write a command here...");
				}
			}
		}
		@Override public void keyReleased(KeyEvent e){} @Override public void keyTyped(KeyEvent e) {}
    });
    text.addFocusListener(new FocusListener(){

		@Override
		public void focusGained(FocusEvent arg0) {
			text.setText("");
		}

		@Override
		public void focusLost(FocusEvent arg0){
			if(text.getText().equalsIgnoreCase("") || text.getText().startsWith(" ")){
				text.setText("Write a command here...");
			}
		}
    	
    });
    text.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			text.setText("");
		}
    });
    this.getContentPane().add(text, "South");
  }
  public JTextArea getTextArea(){
	  return textArea;
  }
  public JScrollPane getPane(){
	  return pane;
  }
  /**
   * This method appends the data to the text area.
   * 
   * @param data
   *            the Logging information data
   */
  public void showInfo(String data) {
    textArea.append(data);
    this.getContentPane().validate();
  }
}

class WindowHandler extends Handler implements Listener{
  //the window to which the logging is done
  public LogWindow window = null;
  public JMenuBar menubar = new JMenuBar();
  public JMenu menuserver = new JMenu("Server");
  public JMenu menuplayers = new JMenu("Players");
  public Formatter formatter = null;

  public Level level = null;

  //the singleton instance
  private static WindowHandler handler = null;

  /**
   * private constructor, preventing initialization
   */
  @EventHandler
  public void join(PlayerJoinEvent e){
	  Player p = e.getPlayer();
 	 String pname = p.getName();
 	 JMenu item = new JMenu(pname);
 	 item.setName(p.getName());
 	 addPlayerBindings(item, p);
 	 menuplayers.add(item);
 	 //menubar.remove(menuplayers);
 	 menubar.add(menuplayers);
	  menuplayers.revalidate();
	  menubar.revalidate();
  }
  @EventHandler
  public void quit(PlayerQuitEvent e){
	  /*this.menuplayers = new JMenu("Players ");
	  for(Player p : Bukkit.getOnlinePlayers()){
		  if(!p.equals(e.getPlayer())){
			  JMenu menu = new JMenu(p.getName());
			  this.addPlayerBindings(menu, p);
			  menuplayers.add(menu);
		  }
	  }*/
	  /*Player p = e.getPlayer();
	  JMenu menu = new JMenu(p.getName());
	  addPlayerBindings(menu, p);
	  menuplayers.remove(menu);
	  menuplayers.revalidate();
	  menubar.revalidate();*/
	  for(Component comp : menuplayers.getMenuComponents()){
		  if(comp.getName().equalsIgnoreCase(e.getPlayer().getName())){
			  menuplayers.remove(comp);
			  comp.revalidate();
			  menuplayers.revalidate();
			  menubar.revalidate();
		  }
	  }
  }
  public void log(String str){
	  Window.logMessage(str);
  }
  private WindowHandler(Plugin plugin) {
	  plugin.getServer().getPluginManager().registerEvents(this, plugin);
    if (window == null){
      window = new LogWindow("Console", 800, 600, plugin);
   
      //Server
      //Gamemode
      final JMenu gamemodeMenu = new JMenu("Default gamemode");
      for(final GameMode gm : GameMode.values()){
    	  String name = gm.name().toLowerCase();
    	  JMenuItem item = new JMenuItem(Character.toUpperCase(name.charAt(0)) + name.substring(1));
          item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			    Bukkit.getServer().setDefaultGameMode(gm);	
			}});
          gamemodeMenu.add(item);
      }
      menuserver.add(gamemodeMenu);
      //Gamemode end
       //Whitelist
       final JCheckBoxMenuItem whitelist = new JCheckBoxMenuItem("Whitelist");
       whitelist.addItemListener(new ItemListener(){
		@Override
		public void itemStateChanged(ItemEvent e) {
		    if(e.getStateChange() == 1){
		    	Bukkit.setWhitelist(true);
		    }else{
		    	Bukkit.setWhitelist(false);
		    }
		}
    	   
       });
       menuserver.add(whitelist);
       //End whitelist
       menuserver.addSeparator();
       //Shutdown
    final JMenuItem shutdown = new JMenuItem("Shutdown");
    shutdown.addActionListener(new ActionListener(){@SuppressWarnings("deprecation")
	@Override
				public void actionPerformed(ActionEvent e) {
   	            for(World w : Bukkit.getWorlds()){
   	            	w.save();
   	            	shutdown.disable();
   	            }
					Bukkit.getServer().shutdown();
				}});
    menuserver.add(shutdown);
      //End Shutdown
    menuserver.addSeparator();
    menubar.add(menuserver);
     //End server
     //Players
    menuplayers = new JMenu("Players ");
     for(Player p : Bukkit.getOnlinePlayers()){
    	 String pname = p.getName();
    	 JMenu item = new JMenu(pname);
    	 item.setName(pname);
    	 addPlayerBindings(item, p);
    	 menuplayers.add(item);
     }
     menubar.add(menuplayers);
     menubar.setVisible(true);
     //End Players
      window.setJMenuBar(menubar);
      window.setVisible(true);
      //
      
    }
  }

  public void bindCommand(JMenuItem item, final String command){
	  item.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			
		}
		  
	  });
  }
  public JMenuItem addPlayerBindings(JMenuItem item, Player p){
 	 JMenu gamemodep = new JMenu("Gamemode");
 	 for(GameMode gm : GameMode.values()){
 		 String name = gm.name().toLowerCase();
          JMenuItem gmi = new JMenuItem(Character.toUpperCase(name.charAt(0)) + name.substring(1));
          bindCommand(gmi, "gamemode " + gm.name() + " " + p.getName());
          gamemodep.add(gmi);
 	 }
 	 item.add(gamemodep);
  	 JMenuItem kick = new JMenuItem("Kick");
  	 bindCommand(kick, "kick " + p.getName());
  	 item.add(kick);
 	 JMenuItem ban = new JMenuItem("Ban");
 	 bindCommand(ban, "ban " + p.getName());
 	 item.add(ban);
 	 JMenuItem mute = new JMenuItem("Mute");
 	 bindCommand(mute, "mute " + p.getName());
 	 item.add(mute);
 	 JMenuItem unmute = new JMenuItem("Unmute");
 	 bindCommand(unmute, "unmute " + p.getName());
 	 item.add(unmute);
 	 JMenuItem freeze = new JMenuItem("Freeze");
 	 bindCommand(freeze, "freeze " + p.getName());
 	 item.add(freeze);
 	 JMenuItem unfreeze = new JMenuItem("Unfreeze");
 	 bindCommand(unfreeze, "unfreeze " + p.getName());
 	 item.add(unfreeze);
 	 JMenuItem kill = new JMenuItem("Kill");
 	 bindCommand(kill, "kill " + p.getName());
 	 item.add(kill);
 	 JMenuItem heal = new JMenuItem("Heal");
 	 bindCommand(heal, "heal " + p.getName());
 	 item.add(heal);
 	 JMenuItem feed = new JMenuItem("Feed");
 	 bindCommand(feed, "feed " + p.getName());
 	 item.add(feed);
 	 JMenuItem ci = new JMenuItem("Clear Inventory");
 	 bindCommand(ci, "ci " + p.getName());
 	 item.add(ci);
 	 JMenuItem fly = new JMenuItem("Toggle Fly");
 	 bindCommand(fly, "fly " + p.getName());
 	 item.add(fly);
 	 return item;
  }
  /**
   * The getInstance method returns the singleton instance of the
   * WindowHandler object It is synchronized to prevent two threads trying to
   * create an instance simultaneously. @ return WindowHandler object
   */

  public static synchronized WindowHandler getInstance(Plugin plugin) {

    if (handler == null) {
      handler = new WindowHandler(plugin);
    }
    return handler;
  }

  /**
   * This is the overridden publish method of the abstract super class
   * Handler. This method writes the logging information to the associated
   * Java window. This method is synchronized to make it thread-safe. In case
   * there is a problem, it reports the problem with the ErrorManager, only
   * once and silently ignores the others.
   * 
   * @record the LogRecord object
   *  
   */
  public synchronized void publish(LogRecord record) {
    String message = null;
    //check if the record is loggable
    if (!isLoggable(record))
      return;
    try {
      message = getFormatter().format(record);
    } catch (Exception e) {
      reportError(null, e, ErrorManager.FORMAT_FAILURE);
    }

    try {
      window.showInfo(message);
    } catch (Exception ex) {
      reportError(null, ex, ErrorManager.WRITE_FAILURE);
    }

  }

  public void close() {
	  window.dispose();
  }

  public void flush() {
  }
}

public class Window {
  static private WindowHandler handler = null;

  public Window(Plugin plugin) {
    handler = WindowHandler.getInstance(plugin);
    //obtaining a logger instance and setting the handler
    ConsoleListener.register(plugin);
  }

  /**
   * This method publishes the log message
   */
  public static void logMessage(String str) {
    //logger.log(Level.INFO, str);
    //handler.publish(new LogRecord(Level.SEVERE, "Jo"));
	  
	handler.window.showInfo(str + "\n");
	
  }
  public void close(){
	  handler.close();
  }
  /*public static void main(String[] args) {
    //logging with the help of a logger
    Window demo = new Window();
    demo.logMessage();
    //using the handler.publish() to log
    WindowHandler h = WindowHandler.getInstance();
    LogRecord r = new LogRecord(Level.WARNING,
        "The Handler publish method...");
    h.publish(r);
  }*/
}
class ConsoleListener implements Listener{
	static Handler handler;
	static String lastmessage = "";
	public static void register(Plugin plugin){
		
		handler = new Handler(){
			@Override
			public void close() throws SecurityException {
			}
			@Override
			public void flush() {
			}
			@Override
			public void publish(LogRecord e) {
				String message = ChatColor.stripColor(e.getMessage());
				message = message.replaceAll("\\[0;[0-9]+;[0-9]+m|\\[m","").replaceAll("", "");
				if(e.getThrown() instanceof Exception){
					Exception ex = (Exception) e.getThrown();
				
					Integer hours = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.HOUR_OF_DAY);
					Integer minutes = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MINUTE);
					Integer seconds = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.SECOND);
					if(!lastmessage.equalsIgnoreCase(message + hours + minutes + seconds)){
						lastmessage = message + hours + minutes  + seconds;
					Window.logMessage("[" + hours + ":" + minutes + ":" + seconds + "]: " + ex.getMessage() + "\n" + ExceptionUtils.getStackTrace(ex));
					}
				}else{
					Integer hours = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.HOUR_OF_DAY);
					Integer minutes = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MINUTE);
					Integer seconds = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.SECOND);
					if(!lastmessage.equalsIgnoreCase(message + hours + minutes + seconds)){
						lastmessage = message + hours + minutes  + seconds;
					Window.logMessage("[" + hours + ":" + minutes + ":" + seconds + "]: " + message);
					}
				}
			}
		};
		Bukkit.getLogger().addHandler(handler);
		Logger.getGlobal().addHandler(handler);
		Logger.getAnonymousLogger().addHandler(handler);
		Logger.getLogger("Minecraft").addHandler(handler);
		org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger)LogManager.getRootLogger();
		coreLogger.addAppender(new Appender(){ 
			@Override public boolean isStarted() { return true; }
			@Override public void start() { }
			@Override public void stop() { }
			@Override public void append(LogEvent e) { 
				String message = e.getMessage().getFormattedMessage() + "";
				message = message.replaceAll("\\[0;[0-9]+;[0-9]+m|\\[m","").replaceAll("", "");
				if(e.getThrown() instanceof Exception){
					Integer hours = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.HOUR_OF_DAY);
					Integer minutes = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MINUTE);
					Integer seconds = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.SECOND);
					if(!lastmessage.equalsIgnoreCase(message + hours + minutes + seconds)){
						lastmessage = message + hours + minutes  + seconds;
					Window.logMessage("[" + hours + ":" + minutes + ":" + seconds + "]: " + AnsiColor.ANSI_RED + message);
					}
				}else{
					Integer hours = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.HOUR_OF_DAY);
					Integer minutes = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MINUTE);
					Integer seconds = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.SECOND);
					if(!lastmessage.equalsIgnoreCase(message + hours + minutes + seconds)){
						lastmessage = message + hours + minutes  + seconds;
					Window.logMessage("[" + hours + ":" + minutes + ":" + seconds + "]: " + message);
					}
				}
			}
			@Override public ErrorHandler getHandler() { return null; }
			@Override public Layout<? extends Serializable> getLayout() { return null; }
			@Override public String getName() { return "UClogs"; }
			@Override public boolean ignoreExceptions() { return false; }
			@Override public void setHandler(ErrorHandler arg0) {}
		});
		for(Plugin plug : Bukkit.getServer().getPluginManager().getPlugins()){
			if(plug.isEnabled()){
				plug.getLogger().addHandler(handler);
			}
		}
		//
		
	}
	@EventHandler
	public void pe(PluginEnableEvent e){
		e.getPlugin().getLogger().addHandler(handler);
	}
}