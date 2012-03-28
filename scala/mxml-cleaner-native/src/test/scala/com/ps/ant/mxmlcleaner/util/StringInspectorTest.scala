package com.ps.ant.mxmlcleaner.util

import org.junit._
import org.junit.Assert._

@Test
class StringInspectorTest 
{
	
	@Test
	def emptyLineWorks = 
	{
			assertTrue( StringInspector.emptyLine("") )
			assertTrue( StringInspector.emptyLine(" ") )
			assertTrue( StringInspector.emptyLine("  ") )
			assertTrue( StringInspector.emptyLine("       ") )
			assertTrue( StringInspector.emptyLine("	    ") )
			assertTrue( StringInspector.emptyLine("	    \r") )
			assertTrue( StringInspector.emptyLine("\t\t	    \r") )
			assertTrue( StringInspector.emptyLine("\t\t	    \n\r") )
   
			assertFalse( "ee isn't allowed", StringInspector.emptyLine("w") )
			assertFalse( StringInspector.emptyLine("\t\t	    .\n\r") )
	}
}
