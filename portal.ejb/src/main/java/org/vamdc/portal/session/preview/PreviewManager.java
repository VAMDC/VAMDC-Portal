package org.vamdc.portal.session.preview;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.vamdc.dictionary.Restrictable;
import org.vamdc.portal.RedirectPage;
import org.vamdc.portal.Settings;
import org.vamdc.portal.entity.query.HttpHeadResponse;
import org.vamdc.portal.registry.RegistryFacade;
import org.vamdc.portal.session.queryBuilder.QueryData;

@Name("preview")
@Scope(ScopeType.PAGE)
public class PreviewManager implements Serializable{

	//TODO: create meaningful test to check the serializability
	private static final long serialVersionUID = 2029452631857959114L;

	@Logger
	Log log;

	@In QueryData queryData;

	@In(create=true) RegistryFacade registryFacade;
	
	private Collection<Future<HttpHeadResponse>> nodeFutureResponses = new ArrayList<Future<HttpHeadResponse>>();
	private long startTime;
	
	public void initiate(){
		if (nodeFutureResponses.size()>0)
			return;

		Collection<String> activeNodes = getActiveNodes();

		if (activeNodes.size()==0)
			return;
		
		ExecutorService executor = Executors.newFixedThreadPool(activeNodes.size());

		for (String ivoaID:activeNodes){
			try{
				nodeFutureResponses.add(executor.submit(new PreviewThread(ivoaID,getQuery(ivoaID))));
			}catch (IllegalArgumentException e){
			}
		}
		
		executor.shutdown();
		startTime = new Date().getTime();
	}

	public Collection<String> getActiveNodes(){
		Collection<String> result = new ArrayList<String>();

		Collection<Restrictable> keywords = queryData.getKeywords();
		for (String ivoaID:registryFacade.getTapIvoaIDs()){
			if (keywords.size()>0 && registryFacade.getRestrictables(ivoaID).containsAll(keywords))
				result.add(ivoaID);
		}
		
		return result;
	}

	private URL getQuery(String ivoaID) {
		String query = queryData.getQueryString();
		URL baseURL;
		URL queryURL=null;
		try {
			baseURL = registryFacade.getVamdcTapURL(ivoaID);
			queryURL = new URL(baseURL+"sync?LANG=VSS2&REQUEST=doQuery&FORMAT=XSAMS&QUERY="+URLEncoder.encode(query,"UTF-8"));
		} catch (MalformedURLException e) {
		} catch (UnsupportedEncodingException e) {
		}

		return queryURL;
	}


	public Collection<HttpHeadResponse> getNodes(){
		TreeSet<HttpHeadResponse> nodes = new TreeSet<HttpHeadResponse>(new HttpHeadResponseComparator());
		for (Future<HttpHeadResponse> task:nodeFutureResponses){
			if (task.isDone()&& !task.isCancelled()){
				try {
					HttpHeadResponse response = task.get();
					nodes.add(response);
				} catch (InterruptedException e) {
					log.info("interruptedException");
					e.printStackTrace();
				} catch (ExecutionException e) {
					log.info("ExecutionException");
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<HttpHeadResponse>(nodes);
	}

	private class HttpHeadResponseComparator implements Comparator<HttpHeadResponse>{

		@Override
		public int compare(HttpHeadResponse o1, HttpHeadResponse o2) {
			if (o1==null || o2==null)
				return 0;
			Integer value1 = Integer.valueOf(o1.getStatus().ordinal());
			Integer value2 = Integer.valueOf(o2.getStatus().ordinal());
			int compare = value1.compareTo(value2);
				if (compare!=0)
						return compare;
				else if(o1.getProcesses()!=o2.getProcesses())
					return o2.getProcesses()-o1.getProcesses();
				else
					return o1.getIvoaID().compareTo(o2.getIvoaID());
				
		}
		

		
	}
	

	public boolean isDone(){
		for (Future<HttpHeadResponse> task:nodeFutureResponses){
			if (!task.isDone())
				return false;
		}
		return true;
	}
	
	public Long getPercentsDone(){
		Long result=0L;
		if (isDone())
			result=101L;
		else{
			Long now = new Date().getTime();
			result = (100L*(now-startTime)/Settings.HTTP_HEAD_TIMEOUT.getInt());
		}
		return result;
	}

	public String getStringStatus(){
		if (isDone())
			return "Done";
		return ""+getNodes().size()+" nodes of "+nodeFutureResponses.size()+" responded";
	}
	
	public void cancel(){
		for (Future<HttpHeadResponse> task:nodeFutureResponses){
			if (!task.isDone())
				task.cancel(true);
		}
	}
	
	public void clear(){
		cancel();
		nodeFutureResponses=new ArrayList<Future<HttpHeadResponse>>();
	}
	
	public String refine(){
		clear();
		return RedirectPage.QUERY;
	}

}
