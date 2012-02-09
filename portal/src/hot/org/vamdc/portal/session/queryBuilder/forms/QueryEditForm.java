package org.vamdc.portal.session.queryBuilder.forms;

import java.util.ArrayList;

import org.vamdc.portal.session.queryBuilder.QueryData;
import org.vamdc.portal.session.queryBuilder.fields.AbstractField;

public class QueryEditForm extends AbstractForm implements Form{

	public String getTitle() { return "Query comments"; }
	public Integer getOrder() { return Order.Query; }
	public String getView() { return "/xhtml/query/forms/editorForm.xhtml"; }
	
	private String queryString = "";
	
	public QueryEditForm(){
		queryData.setQueryEditForm(this);
		fields = new ArrayList<AbstractField>();
	}
	
	@Override
	public String getValue(){
		if (queryString.length()>0)
			return queryString;
		return queryData.getQueryString();
	}
	
	public void setValue(String newQueryString){
		if (!newQueryString.equals(queryData.getQueryString()))
				queryString=newQueryString;
	}
	
	@Override
	public void setQueryData(QueryData queryData){
		super.setQueryData(queryData);
		queryData.setQueryEditForm(this);
	}
	
	@Override
	public void delete(){
		queryData.setQueryEditForm(null);
		super.delete();
	}

	@Override
	public void clear(){
		super.clear();
		queryString="";
	}
	
}
