package com.adobe.ps.ant.mxmlcleaner;

import org.apache.tools.ant.Task;

import com.ps.ant.mxmlcleaner.MxmlCleanerImpl;

public class MxmlCleanerTask extends Task 
{
	private String rules;
	
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	private String sourcePath;

	public String getSourcePath() 
	{
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) 
	{
		this.sourcePath = sourcePath;
	}

	@Override
	public void execute() 
	{
		MxmlCleanerImpl cleanerImpl = new MxmlCleanerImpl();
		
		if( sourcePath == null )
		{
			throw new RuntimeException("you must specify a source path");
		}

		log("sourcePath is: " + sourcePath);
		cleanerImpl.run(sourcePath, rules );

	}
}
