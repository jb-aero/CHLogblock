package com.zeoldcraft.chlb.functions;

import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.blocks.MCSign;
import com.laytonsmith.abstraction.bukkit.BukkitMCLocation;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;
import com.zeoldcraft.chlb.CHLB;

import de.diddiz.LogBlock.Consumer;

public class LBLogging {
	
	public static String docs() {
		return "Functions for manually logging block changes in LogBlock.";
	}
	
	private static Consumer getConsumer(Target t){
		Consumer cons = CHLB.getConsumer();
		if (cons != null) {
			return cons;
		} else {
			throw new ConfigRuntimeException("LogBlock is not properly loaded!", ExceptionType.InvalidPluginException, t);
		}
	}

	@api
	public static class lb_log_break extends AbstractFunction {

		public ExceptionType[] thrown() {
			return new  ExceptionType[]{ExceptionType.InvalidPluginException, ExceptionType.PlayerOfflineException,
					ExceptionType.CastException, ExceptionType.FormatException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment,
				Construct... args) throws ConfigRuntimeException {
			Static.checkPlugin("LogBlock", t);
			String p = args[0].val();
			MCLocation loc = ObjectGenerator.GetGenerator().location(args[1], null, t);
			int typeBefore = loc.getBlock().getTypeId();
			byte dataBefore = loc.getBlock().getData();
			if (args.length >= 3) {
				typeBefore = Static.getInt32(args[2], t);
			}
			if (args.length == 4) {
				dataBefore = Static.getInt8(args[3], t);
			}
			getConsumer(t).queueBlockBreak(p, ((BukkitMCLocation) loc).asLocation(), typeBefore, dataBefore);
			return new CVoid(t);
		}

		public String getName() {
			return "lb_log_break";
		}

		public Integer[] numArgs() {
			return new  Integer[]{2, 3, 4};
		}

		public String docs() {
			return "void {player, locationArray, [typeBreaking], [dataBreaking]} Manually logs a block breaking"
					+ " at a location. You can choose to specify the type and data of the block being broken.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	@api
	public static class lb_log_break_sign extends AbstractFunction {
		
		public ExceptionType[] thrown() {
			return new  ExceptionType[]{ExceptionType.InvalidPluginException, ExceptionType.PlayerOfflineException,
					ExceptionType.CastException, ExceptionType.FormatException, ExceptionType.RangeException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment,
				Construct... args) throws ConfigRuntimeException {
			Static.checkPlugin("LogBlock", t);
			String p = args[0].val();
			MCLocation loc = ObjectGenerator.GetGenerator().location(args[1], null, t);
			int type = loc.getBlock().getTypeId();
			byte data = loc.getBlock().getData();
			String[] lines = new String[]{"", "", "", ""};
			if (loc.getBlock().isSign()) {
				MCSign s =  loc.getBlock().getSign();
				for (int i=0; i<4; i++) {
					lines[i] = s.getLine(i);
				}
			}
			if (args.length >= 3) {
				if (args[2] instanceof CArray) {
					for (int i=0;i<4;i++) {
						lines[i] = ((CArray) args[2]).get(i).val();
					}
				} else {
					throw new Exceptions.FormatException("Expected an array of lines on the sign", t);
				}
			}
			if (args.length >= 4) {
				type = Static.getInt32(args[3], t);
			}
			if (type != 63 && type != 68) {
				throw new Exceptions.RangeException("No sign specified.", t);
			}
			if (args.length == 5) {
				data = Static.getInt8(args[4], t);
			}
			getConsumer(t).queueSignBreak(p, ((BukkitMCLocation) loc).asLocation(), type, data, lines);
			return new CVoid(t);
		}

		public String getName() {
			return "lb_log_break_sign";
		}

		public Integer[] numArgs() {
			return new  Integer[]{2, 3, 4, 5};
		}

		public String docs() {
			return "void {player, locationArray, [array], [typePlacing], [dataPlacing]} Manually logs a sign being broken"
					+ " at a location. The third param is an array of the lines of the sign, defaulting to 4 blank strings."
					+ " The 4th and 5th determine the type and rotation of the sign.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
	}
	
	@api
	public static class lb_log_place extends AbstractFunction {

		public ExceptionType[] thrown() {
			return new  ExceptionType[]{ExceptionType.InvalidPluginException, ExceptionType.PlayerOfflineException,
					ExceptionType.CastException, ExceptionType.FormatException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment,
				Construct... args) throws ConfigRuntimeException {
			Static.checkPlugin("LogBlock", t);
			String p = args[0].val();
			MCLocation loc = ObjectGenerator.GetGenerator().location(args[1], null, t);
			int type = loc.getBlock().getTypeId();
			byte data = loc.getBlock().getData();
			if (args.length >= 3) {
				type = Static.getInt32(args[2], t);
			}
			if (args.length == 4) {
				data = Static.getInt8(args[3], t);
			}
			getConsumer(t).queueBlockPlace(p, ((BukkitMCLocation) loc).asLocation(), type, data);
			return new CVoid(t);
		}

		public String getName() {
			return "lb_log_place";
		}

		public Integer[] numArgs() {
			return new  Integer[]{2, 3, 4};
		}

		public String docs() {
			return "void {player, locationArray, [typePlacing], [dataPlacing]} Manually logs a block being placed"
					+ " at a location. You can choose to specify the type and data of the block being placed.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	@api
	public static class lb_log_place_sign extends AbstractFunction {
		
		public ExceptionType[] thrown() {
			return new  ExceptionType[]{ExceptionType.InvalidPluginException, ExceptionType.PlayerOfflineException,
					ExceptionType.CastException, ExceptionType.FormatException, ExceptionType.RangeException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment,
				Construct... args) throws ConfigRuntimeException {
			Static.checkPlugin("LogBlock", t);
			String p = args[0].val();
			MCLocation loc = ObjectGenerator.GetGenerator().location(args[1], null, t);
			int type = loc.getBlock().getTypeId();
			byte data = loc.getBlock().getData();
			String[] lines = new String[]{"", "", "", ""};
			if (loc.getBlock().isSign()) {
				MCSign s =  loc.getBlock().getSign();
				for (int i=0; i<4; i++) {
					lines[i] = s.getLine(i);
				}
			}
			if (args.length >= 3) {
				if (args[2] instanceof CArray) {
					for (int i=0;i<4;i++) {
						lines[i] = ((CArray) args[2]).get(i).val();
					}
				} else {
					throw new Exceptions.FormatException("Expected an array of lines on the sign", t);
				}
			}
			if (args.length >= 4) {
				type = Static.getInt32(args[3], t);
			}
			if (type != 63 && type != 68) {
				throw new Exceptions.RangeException("No sign specified.", t);
			}
			if (args.length == 5) {
				data = Static.getInt8(args[4], t);
			}
			getConsumer(t).queueSignPlace(p, ((BukkitMCLocation) loc).asLocation(), type, data, lines);
			return new CVoid(t);
		}

		public String getName() {
			return "lb_log_place_sign";
		}

		public Integer[] numArgs() {
			return new  Integer[]{2, 3, 4, 5};
		}

		public String docs() {
			return "void {player, locationArray, [array], [typePlacing], [dataPlacing]} Manually logs a sign being placed"
					+ " at a location. The third param is an array of lines on the sign, defaulting to 4 blank strings."
					+ " The fourth and fifth determine the placement of the sign.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
	}
}
