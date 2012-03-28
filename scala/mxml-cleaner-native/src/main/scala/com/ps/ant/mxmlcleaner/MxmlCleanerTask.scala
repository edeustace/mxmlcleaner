package com.ps.ant.mxmlcleaner

import org.apache.tools.ant.Task

class MxmlCleanerTask extends Task
{
	var sourcePath : String = ""
 
	override def execute =
	{
		System.out.println(sourcePath)
	}
}
