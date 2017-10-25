package com.zeoldcraft.chlb;

import java.util.ArrayList;
import java.util.List;

import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.bukkit.BukkitMCLocation;
import com.laytonsmith.abstraction.bukkit.BukkitMCWorld;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;

import com.laytonsmith.core.exceptions.CRE.CRENotFoundException;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import de.diddiz.worldedit.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class LBOG {

	private static LBOG lbog = null;

    public static LBOG GetGenerator() {
        if (lbog == null) {
            lbog = new LBOG();
        }
        return lbog;
    }
    
    public QueryParams query(Construct c, LogBlock lb, Target t) {
    	QueryParams qp = new QueryParams(lb);
    	if (c instanceof CArray && ((CArray) c).inAssociativeMode()) {
    		CArray p = (CArray) c;
    		MCLocation l1, l2;
    		RegionContainer rc;
    		if (p.containsKey("info")) {
    			qp.merge(columns(qp, p.get("info", t), t));
    		} else {
    			throw new CREFormatException("Array was missing info key", t);
    		}
    		if (p.containsKey("changetype")) {
    			try {
    				qp.bct = BlockChangeType.valueOf(p.get("changetype", t).val());
    			} catch (IllegalArgumentException iae) {
    				throw new CREFormatException("Applicable changetypes: " + BlockChangeType.values().toString(), t);
    			}
    		}
    		if (p.containsKey("location")) {
    			l1 = ObjectGenerator.GetGenerator().location(p.get("location", t), null, t);
    			qp.setLocation(((BukkitMCLocation) l1).asLocation());
    			if (p.containsKey("radius")) {
    				qp.radius = Static.getInt32(p.get("radius", t), t);
    			}
    		} else if (p.containsKey("sel")) {
    			Construct sel = p.get("sel", t);
    			if (sel instanceof CArray) {
    				CArray ca = (CArray) sel;
    				if(ca.size() < 2) {
						throw new CREFormatException("Expected 2 location arrays for sel", t);
					}
    				l1 = ObjectGenerator.GetGenerator().location(ca.get(0, t), null, t);
    				l2 = ObjectGenerator.GetGenerator().location(ca.get(1, t), null, t);
    				rc = RegionContainer.fromCorners(((BukkitMCWorld) l1.getWorld()).__World(),
    						((BukkitMCLocation) l1).asLocation(), ((BukkitMCLocation) l2).asLocation());
    				qp.setSelection(rc);
    			} else {
    				MCPlayer player = Static.GetPlayer(p.get("sel", t), t);
					Plugin wep = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
					if(wep == null || !wep.isEnabled()) {
						throw new CRENotFoundException("WorldEdit cannot be found or is disabled.", t);
					}
    				rc = RegionContainer.fromPlayerSelection(((BukkitMCPlayer) player)._Player(), wep);
    				qp.setSelection(rc);
    			}
    		}
    		if (p.containsKey("players")) {
    			List<String> plist = new ArrayList<String>();
    			if (p.get("players", t) instanceof CArray) {
    				for (int i=0; i < ((CArray) p.get("players", t)).size(); i++) {
    					plist.add(((CArray) p.get("players", t)).get(i, t).val());
    				}
    			} else {
    				plist.add(p.get("players", t).val());
    			}
    			qp.players = plist;
    		}
    		if (p.containsKey("world")) {
    			qp.world = ((BukkitMCWorld) Static.getServer().getWorld(p.get("world", t).val())).__World();
    		}
    		if (p.containsKey("since")) {
    			qp.since = Static.getInt32(p.get("since", t), t);
    		}
    		if (p.containsKey("before")) {
    			qp.before = Static.getInt32(p.get("before", t), t);
    		}
    		if (p.containsKey("limit")) {
    			qp.limit = Static.getInt32(p.get("limit", t), t);
    		}
    	} else {
    		throw new CREFormatException("Expected an associative array but received " + c, t);
    	}
    	return qp;
    }
    
    public QueryParams columns(QueryParams qp, Construct col, Target t) {
		if (col instanceof CArray && /* ((CArray) col).inAssociativeMode() && */(((CArray) col).size() >= 0)) {
			CArray ca = (CArray) col;
			for (int i = 0; i < ca.size(); i++) {
				LBColumns lbc;
				try {
					lbc = LBColumns.valueOf(ca.get(i, t).val().toUpperCase());
				} catch (IllegalArgumentException ex) {
					continue;
				}
				switch (lbc) {
					case CHESTACCESS:
						qp.needChestAccess = true;
						break;
					case COORDS:
						qp.needCoords = true;
						break;
					case COUNT:
						qp.needCount = true;
						break;
					case DATA:
						qp.needData = true;
						break;
					case DATE:
						qp.needDate = true;
						break;
					case ID:
						qp.needId = true;
						break;
					case KILLER:
						qp.needKiller = true;
						break;
					case MESSAGE:
						qp.needMessage = true;
						break;
					case PLAYER:
						qp.needPlayer = true;
						break;
					case SIGNTEXT:
						qp.needSignText = true;
						break;
					case TYPE:
						qp.needType = true;
						break;
					case VICTIM:
						qp.needVictim = true;
						break;
					case WEAPON:
						qp.needWeapon = true;
						break;
				}
			}
    	} else {
    		throw new CREFormatException("Needed an array to determine which info is requested", t);
    	}
    	return qp;
    }
}
