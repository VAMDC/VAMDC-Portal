package org.vamdc.portal.session.queryBuilder.forms;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.vamdc.dictionary.Restrictable;
import org.vamdc.portal.session.queryBuilder.QueryData;
import org.vamdc.portal.session.queryBuilder.fields.AbstractField;

public abstract class AbstractForm implements QueryForm{
	
	protected List<AbstractField> fields;
	private transient QueryData queryData;
	
	public AbstractForm(QueryData queryData){ 
		this.queryData = queryData;
	}
	
	public void setQueryData(QueryData queryData){ 
		this.queryData = queryData;
	}
	
	public String getQueryPart() {
		String query="";
		for (AbstractField field:fields){
			if (field.hasValue()){
				if (query.length()>0){
					query+=" AND "+field.getQuery();
				}
				else query=field.getQuery();
			}
		}
		return query;
	}
	
	public Collection<AbstractField> getFields() { return fields; }
	
	public Collection<Restrictable> getKeywords() {
		EnumSet<Restrictable> keywords = EnumSet.noneOf(Restrictable.class);
		for (AbstractField field:fields){
			if (field.hasValue()){
				keywords.add(field.getKeyword());
			}
		}
		return keywords;
	}
	
	public void clear(){
		for (AbstractField field:fields){
			field.clear();
		}
	}

	public void delete(){
		queryData.deleteForm(this);
	}
	
}
