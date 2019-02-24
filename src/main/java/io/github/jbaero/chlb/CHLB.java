package io.github.jbaero.chlb;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import org.bukkit.plugin.Plugin;

import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

@MSExtension("CHLogBlock")
public class CHLB extends AbstractExtension {
	private static LogBlock lb;
	private static Consumer cons;

	public Version getVersion()  {
		return new SimpleVersion(0,2,0);
	}

	@Override
	public void onStartup() {
		CommandHelperPlugin chp = CommandHelperPlugin.self;
		try {
			Static.checkPlugin("LogBlock", Target.UNKNOWN);
			Plugin pl = chp.getServer().getPluginManager().getPlugin("LogBlock");
			if (pl instanceof LogBlock) {
				lb = (LogBlock) pl;
				cons = lb.getConsumer();
			} else {
				throw new CREInvalidPluginException("", Target.UNKNOWN);
			}
			System.out.println("CHLogBlock " + getVersion() + " loaded.");
		} catch (ConfigRuntimeException cre) {
			Static.getLogger().warning("LogBlock not found, CHLogblock cannot function!");
		}
	}

	@Override
	public void onShutdown() {
		System.out.println("CHLogBlock " + getVersion() + " unloaded.");
	}
	
	public static Consumer getConsumer() {
		return cons;
	}
	
	public static LogBlock getLB() {
		return lb;
	}
}
