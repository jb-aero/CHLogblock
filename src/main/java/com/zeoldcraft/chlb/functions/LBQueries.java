package com.zeoldcraft.chlb.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;
import com.zeoldcraft.chlb.CHLB;
import com.zeoldcraft.chlb.LBOG;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;

public class LBQueries {

	public static String docs() {
		return "Functions for getting queries from LogBlock.";
	}
	
	public static Consumer getConsumer(Target t){
		Consumer cons = CHLB.getConsumer();
		if (cons != null) {
			return cons;
		} else {
			throw new ConfigRuntimeException("LogBlock is not properly loaded!", ExceptionType.InvalidPluginException, t);
		}
	}
	
	public static LogBlock getLB(Target t) {
		LogBlock lb = CHLB.getLB();
		if (lb != null) {
			return lb;
		} else {
			throw new ConfigRuntimeException("LogBlock is not properly loaded!", ExceptionType.InvalidPluginException, t);
		}
	}
	
	@api
	public static class lb_get_changes extends AbstractFunction {

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.IOException, ExceptionType.PlayerOfflineException,
					ExceptionType.FormatException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment,
				Construct... args) throws ConfigRuntimeException {
			LogBlock lb = getLB(t);
			QueryParams qp = LBOG.GetGenerator().query(args[0], lb, t);
			java.util.List<BlockChange> bcl;
			CArray ret = new CArray(t);
			try {
				bcl = lb.getBlockChanges(qp);
			} catch (java.sql.SQLException e) {
				throw new ConfigRuntimeException("Could not read database!", ExceptionType.IOException, t);
			}
			for (BlockChange bc : bcl) {
				ret.push(new CString(bc.toString(), t));
			}
			return ret;
		}

		public String getName() {
			return "lb_get_changes";
		}

		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		public String docs() {
			return "array {array} Takes an associative array of parameters to query the database with,"
					+ " and returns an array of the matching changes. Block changes are in the form of"
					+ " strings pre-formatted by LogBlock.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
}
