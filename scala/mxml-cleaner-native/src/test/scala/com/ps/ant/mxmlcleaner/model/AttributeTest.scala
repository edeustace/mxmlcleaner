package com.ps.ant.mxmlcleaner.model

import org.junit._
import org.junit.Assert._

@Test
class AttributeTest 
{
	@Test
	def testConstructor = 
	{
		assertAttribute(	"xmlns:mx=\"http://www.macromedia.com\"",
                  			"xmlns:mx", 
                  			"http://www.macromedia.com" )
		assertAttribute(	"creationComplete=\"moveArrowTo = pm.selectedBrandMenu\"", 
                   			"creationComplete", 
                   			"moveArrowTo = pm.selectedBrandMenu")
		assertAttribute(	"xmlns:com=\"blah.test\"",
                   			"xmlns:com", 
                   			"blah.test" )
		assertAttribute(	"xmlns:containers=\"flexlib.containers.*\"", 
                   			"xmlns:containers", 
                   			"flexlib.containers.*" )
        assertAttribute(	" 	xmlns:containers=\"flexlib.containers.*\"", 
                   			"xmlns:containers", 
                   			"flexlib.containers.*" )
	}
 
	private def assertAttribute( raw : String, name : String, value : String ) =
	{
		val attr : Attribute = new Attribute( raw )
		assertEquals( name, attr.name )
		assertEquals( value, attr.value )
	}
}
