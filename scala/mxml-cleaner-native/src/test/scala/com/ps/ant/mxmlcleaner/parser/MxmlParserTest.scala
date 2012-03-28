package com.ps.ant.mxmlcleaner.parser

import org.junit._
import org.junit.Assert._

@Test
class MxmlParserTest
{
    @Ignore
    @Test
	def getRootNode() =
	{
		val testString : String = """<?xml version="1.0" encoding="utf-8"?>
<!--
	Comment
-->
<RootNode xmlns="com.betware.ds.games.view.components.popups.*">
	<mx:Metadata>
	</mx:Metadata>
	<mx:Style>
	</mx:Style>
	<mx:Script>
		<![CDATA[
		
		]]>
	</mx:Script>
</RootNode>
"""
		assertRootNode( testString, """<RootNode xmlns="com.betware.ds.games.view.components.popups.*">""" )
  
  	val testString2 : String = """<!--
	Comment
-->
<RootNode xmlns="com.betware.ds.games.view.components.popups.*">
	<mx:Metadata>
	</mx:Metadata>
	<mx:Style>
	</mx:Style>
	<mx:Script>
		<![CDATA[
		
		]]>
	</mx:Script>
</RootNode>
"""
		assertRootNode( testString2, """<RootNode xmlns="com.betware.ds.games.view.components.popups.*">""" )
  	val testString3 : String = """<RootNode xmlns="com.betware.ds.games.view.components.popups.*">
	<mx:Metadata>
	</mx:Metadata>
	<mx:Style>
	</mx:Style>
	<mx:Script>
		<![CDATA[
		
		]]>
	</mx:Script>
</RootNode>
"""
		assertRootNode( testString3, """<RootNode xmlns="com.betware.ds.games.view.components.popups.*">""" )

	}
 
	private def assertRootNode( testString : String , expected : String ) = 
	{
		val mockParser : MockParser = new MockParser
		val result : String = mockParser.getRootNode( testString )
		assertEquals( expected, result )
	}
 
	 	
    
    @Test
    def getNamespaces =
     {
       val testString : String = """<DsPopup
        xmlns:mx="http://www.adobe.com/2006/mxml"
        xmlns="com.betware.ds.games.view.components.popups.*"
        creationComplete="onCreationComplete()"
        dragBarHeight="{POPUP_TITLEBAR_HEIGHT}"
        draggable="true"
        initialize="onInitialize()"
        printable="true"
        statusBarHeight="{popupStatusBarHeight}"
        styleName="dsArticlePopupStyle"
        topButtonsBarHeight="{POPUP_TITLEBAR_HEIGHT}"
        topButtonsPaddingRight="17"
        width="550"
        >"""
        assertNumberOfNamepaces( testString, 1 )
       
        val testString2 = """<mx:Canvas 
        xmlns:mx="http://www.adobe.com/2006/mxml" 

        tabEnabled="true"
        focusIn="onFocusIn()"
        focusOut="onFocusOut()"
        toolTipShown="setToolTipColor(event)"
        creationComplete="init()"
        initialize="createTooltip()" 
        xmlns:controls="view.controls.*" xmlns:textinputs="ds.hindu.common.view.controls.text.textinputs.*">"""
        
       assertNumberOfNamepaces( testString2, 3 )
    }
    
    private def assertNumberOfNamepaces( testString : String, expected : Int ) =
    {
        val parser : MockParser = new MockParser()
        var result : List[String] = parser.getNamespaces( testString )
        assertEquals( expected, result.length )
    }
    
    
    @Test
    def getActionscript = 
    {
		val parser : MockParser = new MockParser()
		val testString = """<?xml version="1.0" encoding="utf-8"?>
<mx:Canvas 
		xmlns:mx="http://www.adobe.com/2006/mxml">
			>
	<mx:Script>
		<![CDATA[
		import mx.binding.utils.BindingUtils;
		]]>
	</mx:Script>
	<mx:Script>
		<![CDATA[
		import mx.binding.utils.BindingUtils;
		]]>
	</mx:Script>
	<view:Test/>
	<mx:Script>
		<![CDATA[
		import mx.binding.utils.BindingUtils;
		]]>
	</mx:Script>
		</mx:Canvas>"""
  
		val result = parser.getActionScript( testString )
		assertEquals( 3, countOccurrences("import mx.binding.utils.BindingUtils", result) )
      
    }
     
    
    @Test
    def getActionscriptIgnoreCommented = 
    {
		val parser : MockParser = new MockParser()
		val testString = """<?xml version="1.0" encoding="utf-8"?>
<mx:Canvas 
		xmlns:mx="http://www.adobe.com/2006/mxml">
			>
	<!--
	<mx:Script>
		<![CDATA[
		import mx.binding.utils.BindingUtils;
		]]>
	</mx:Script>
	-->
	<mx:Script>
		<![CDATA[
		import mx.binding.utils.BindingUtils;
		]]>
	</mx:Script>
	<view:Test/>
	<mx:Script>
		<![CDATA[
		import mx.binding.utils.BindingUtils;
		]]>
	</mx:Script>
		</mx:Canvas>"""
  
		val result = parser.getActionScript( testString )
		assertEquals( 2, countOccurrences("import mx.binding.utils.BindingUtils", result) )
      
    }
    
      
      @Test
    def getActionscriptIgnoreAsMultilineComments = 
    {
		val parser : MockParser = new MockParser()
		val testString = """<?xml version="1.0" encoding="utf-8"?>
<mx:Canvas 
		xmlns:mx="http://www.adobe.com/2006/mxml">
			>
	<mx:Script>
		<![CDATA[
		/*
		import mx.binding.utils.BindingUtils;
		*/
		]]>
	</mx:Script>
	<view:Test/>
		</mx:Canvas>"""
  
		val result = parser.getActionScript( testString )
		println( "found: " + result )
		assertEquals( 0, countOccurrences("import mx.binding.utils.BindingUtils", result) )
      
    }
    
    
    
	@Test
    def getActionscriptIgnoreAsMultilineComments_MultipleComments = 
    {
		val parser : MockParser = new MockParser()
		val testString = """<?xml version="1.0" encoding="utf-8"?>
<mx:Canvas 
		xmlns:mx="http://www.adobe.com/2006/mxml">
			>
	<mx:Script>
		<![CDATA[
		/*
		import mx.binding.utils.BindingUtils;
		*/
		import mx.binding.utils.BindingUtils;
		/*
		import mx.binding.utils.BindingUtils;
		*/
		
		]]>
	</mx:Script>
	<view:Test/>
		</mx:Canvas>"""
  
		val result = parser.getActionScript( testString )
		println( "found: " + result )
		assertEquals( 1, countOccurrences("import mx.binding.utils.BindingUtils", result) )
      
    }
    
    
    @Test
	def getActionscriptIgnoreAsSinglelineComments = 
	{
		val parser : MockParser = new MockParser()
	val testString = """<?xml version="1.0" encoding="utf-8"?>
	<mx:Canvas 
	xmlns:mx="http://www.adobe.com/2006/mxml">
	>
	<mx:Script>
	<![CDATA[
	         
	         //import mx.binding.utils.BindingUtils;
	         
	         ]]>
	</mx:Script>
	<view:Test/>
	</mx:Canvas>"""
	
	val result = parser.getActionScript( testString )
	println( "found: " + result )
	assertEquals( 0, countOccurrences("import mx.binding.utils.BindingUtils", result) )
	
	}
    
    
	def countOccurrences( search : String, source : String  ) : Int =
	{
		var counter = 0
		val searchComplete : String =  search 
		for( matched <- searchComplete.r findAllIn source )
		{
			counter += 1
		}
		counter 
	}
 
	
 	@Test
	def importsInUse = 
	{
		val parser : MockParser = new MockParser
		val fileString : String = """
      			import com.test.Link;
   				Link( cast  );
 				"""
       assertTrue( "Casting should be picked up", parser.importInUse( wrapInXml(fileString), "Link" ) )
       
       val fileString3 : String = """
       import com.test.Link;
       Link.TEST;
       """
       assertTrue( "Statics should be picked up", parser.importInUse( wrapInXml(fileString3), "Link" ) )
       
       val fileString4 : String = """
       import com.test.Link;
       var link : Link = new Link();
       """
       assertTrue( "Variables shold be picked up", parser.importInUse( wrapInXml(fileString4), "Link" ) )
       
       val fileString5 : String = """
       import com.test.Link;
       var link : ImageLink = new ImageLink();
       """
       assertFalse( "Similar names (ImageLink) should NOT be picked up", parser.importInUse( wrapInXml(fileString5), "Link" ) )
       
       val fileString6 : String = """
       import com.test.Link;
       var link : GetLink = new GetLink();
       """
       assertFalse( "Similar names (GetLink) should NOT be picked up", parser.importInUse( wrapInXml(fileString6), "Link" ) )
      
	}
    
    
    @Test
    def repeatedImportInUse = 
    {
    	val repeatedUnusedImport : String = """
    	import com.test.Link;
    	import com.test.Link;
    	import com.test.Link;
    var link : GetLink = new GetLink();
    """
     val parser : MockParser = new MockParser
		
    assertFalse( parser.importInUse( wrapInXml(repeatedUnusedImport), "Link" ) )
   
    val repeatedUsedImport : String = """
    import com.test.Link;
    import com.test.Link;
    import com.test.Link;
    var link : Link;
    """
    
    assertTrue( parser.importInUse( wrapInXml(repeatedUsedImport), "Link" ) )
    
        
    }
 
    private def wrapInXml( script : String ) : String = "<mx:Box><mx:Script>" + script + "</mx:Script></mx:Box>";
    
	@Test
	def classDeclaredInBinding =
	{
		val testXML = wrapInCanvas("""
		<mx:Label text="{ ClassName }"/>
		<mx:Label text="{ ClassName.TEST }"/>""")
  
		assertClassDecaredInBinding( testXML, "ClassName" )
	}	

	@Test
	def classDeclaredInAttribute_Equality =
	{
		val testXML = wrapInCanvas( """<mx:Button 
			selected="{ pm.selectedBrandMenu == StaticDeclaration.ODDSET }"/>""" )
		
		assertClassDecaredInBinding( testXML, "StaticDeclaration")
	}
    
	@Test
	def classDeclaredInAttribute_As =
	{
		val testXML = wrapInCanvas( """<controls:DSButton
			label="{ makeSureItsNotNull((rep.currentItem as AsClassDeclaration).label, '' ) }"/>""" )
		
		assertClassDecaredInBinding( testXML, "AsClassDeclaration")
   }
 
	@Test
	def classDeclaredInAttribute_Cast =
	{
		val testXML = wrapInCanvas( """<controls:DSButton
			label="{ makeSureItsNotNull( AsClassDeclaration(rep.currentItem).label, '' ) }"/>""" )
		
		assertClassDecaredInBinding( testXML, "AsClassDeclaration")
	}
 
	@Test
	def classDeclaredInAttribute_Class =
	{
		val testXML = wrapInCanvas( """<controls:DSButton
			class="{ClassOnlyDeclaration}"/>""" )
		
		assertClassDecaredInBinding( testXML, "ClassOnlyDeclaration")
		
		val testXML2 = wrapInCanvas( """<controls:DSButton
				class="  {  ClassOnlyDeclaration2 }  "/>""" )
				
				assertClassDecaredInBinding( testXML2, "ClassOnlyDeclaration2")
	}
 
	@Test
	def classDeclaredInAttribute_New =
	{
		val testXML2 = wrapInCanvas( """<controls:DSButton 
											click="parent.dispatchEvent( new NewClassDeclaration(CustomClassName2.CLOSE_POPUP,'close' ))" />""")
		assertClassDecaredInBinding( testXML2, "NewClassDeclaration")
	} 
 
 
	private def assertClassDecaredInBinding( xmlString : String, className : String ) =  
	{
		val parser : MockParser = new MockParser()
		assertTrue( className + " should be declared", parser.classDeclaredInBinding( xmlString, className ))
		assertFalse( className + " trimmed should not be declared", parser.classDeclaredInBinding( xmlString, className.substring( 0, className.length -1) ))
		assertFalse( className + " prepended should not be declared", parser.classDeclaredInBinding( xmlString, "O" + className ))
		assertFalse( className + " appended should not be declared", parser.classDeclaredInBinding( xmlString, className + "r" ))
	}
 
	private def wrapInCanvas( xmlString : String ) : String = 
	{
		"""<mx:Canvas xmlns:mx="http://www.adobe.com/2006/mxml">""" + xmlString + """</mx:Canvas>"""
	}
	
	
	
	
	
	@Test
	def classDeclaredInScriptNode =
	{
		val testXML = """<mx:Canvas 
		xmlns:mx="http://www.adobe.com/2006/mxml">
		<mx:Script>
		<![CDATA[
		         import com.ee.test.ClassName;
		      var test : ClassName;
		         ]]>
			</mx:Script>
		
		</mx:Canvas>"""
  
		val parser : MockParser = new MockParser()
		assertTrue("ClassName should be used", parser.classUsedInScript( testXML, "ClassName" ))
		assertFalse( "ClassNam shouldn't be used", parser.classUsedInScript( testXML, "ClassNam" ))
	}	
 
	
	@Test
	def classDeclaredInScriptNodeWithSimilarClassNames =
	{
		val testXML = """<mx:Canvas 
		xmlns:mx="http://www.adobe.com/2006/mxml">
		<mx:Script>
		<![CDATA[
		         import com.ee.test.ClassName;
		         import com.ee.test.ClassNameEvent;
		      //var test : ClassName;
		         /*var test2 : ClassName;
		         	*/
		         ]]>
			</mx:Script>
		
		</mx:Canvas>"""
  
		val parser : MockParser = new MockParser()
		assertFalse( parser.classUsedInScript( testXML, "ClassName" ))
		assertFalse( parser.classUsedInScript( testXML, "ClassNameEvent" ))
	}	
		
	
	@Test
	def wildcardImportDoesntThrowError = 
	{
		val testXML = """<mx:Canvas>
						<mx:Script>
		<![CDATA[

			import flash.net.*;
			import flash.text.*;
		]]>
		</mx:Script>
		</mx:Canvas>"""
		
		val parser : MockParser = new MockParser()
		assertFalse( parser.classUsedInScript( testXML, "ClassName" ))
    
	}
}



class MockParser extends AnyRef with MxmlParser
{
  
}
