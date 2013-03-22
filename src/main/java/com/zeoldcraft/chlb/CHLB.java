package com.zeoldcraft.chlb;

import org.bukkit.plugin.Plugin;

import com.laytonsmith.annotations.startup;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class CHLB {
	
	private static LogBlock lb;
	private static Consumer cons;
	
	public static String docs() {
		return "LogBlock functions here.";
	}
	
	@startup
	public static void setup() {
		CommandHelperPlugin chp = CommandHelperPlugin.self;
		try {
			Static.checkPlugin("LogBlock", Target.UNKNOWN);
			Plugin pl = chp.getServer().getPluginManager().getPlugin("LogBlock");
			if (pl instanceof LogBlock) {
				lb = (LogBlock) pl;
				cons = lb.getConsumer();
			} else {
				throw new ConfigRuntimeException("", ExceptionType.InvalidPluginException, Target.UNKNOWN);
			}
		} catch (ConfigRuntimeException cre) {
			Static.getLogger().warning("LogBlock not found, CHLogblock cannot function!");
		}
	}
	
	public static Consumer getConsumer() {
		return cons;
	}
	
	public static LogBlock getLB() {
		return lb;
	}
}
