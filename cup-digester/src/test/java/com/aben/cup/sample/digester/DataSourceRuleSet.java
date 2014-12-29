package com.aben.cup.sample.digester;

import com.aben.cup.digester.Digester;
import com.aben.cup.digester.ObjectCreateRule;
import com.aben.cup.digester.RuleSet;

public class DataSourceRuleSet implements RuleSet {

    /**
     * The matching pattern prefix to use for recognizing our elements.
     */
    protected String prefix = null;
    
	 public DataSourceRuleSet(String prefix) {
	        super();
	        this.prefix = prefix;

	    }
	@Override
	public String getNamespaceURI() {
		// myOpinion Auto-generated method stub
		return null;
	}

	@Override
	public void addRuleInstances(Digester digester) {
		// myOpinion Auto-generated method stub
		 digester.addRule(prefix+"/data-source",new ObjectCreateRule("com.aben.cup.sample.digester.bean.DataSource", "className"));
		 digester.addSetProperties(prefix+"/data-source");
		
	}

}
