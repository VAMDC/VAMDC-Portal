package org.vamdc.portal.session.queryBuilder;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;

/**
 * Main class of a query page
 * @author doronin
 *
 */
@Name("query")
@Scope(ScopeType.CONVERSATION)
public class Query {
	
	@Logger
	Log log;
	
	@In
	Conversation conversation;
	
	private Integer counter=0;
	
	public int getCount(){
		return counter;
	}
	
	@End
	public String saveQuery(){
		conversation.endAndRedirect();
		return "queryLog";
	}
	
	
	public String preview(){
		log.info("preview action");
		if (this.isValid())
			return "preview";
		else 
			return "query";
	}
	
	public String refine(){
		log.info("refine query");
		return "query";
	}
	
	public void action(){
		log.info(conversation.isLongRunning());
		log.info(counter++);
	}
	
	public boolean isValid(){
		return true;
	}
	
}
