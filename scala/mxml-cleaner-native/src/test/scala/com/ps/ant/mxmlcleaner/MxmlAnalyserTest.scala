package com.ps.ant.mxmlcleaner

import org.junit._
import Assert._
import java.io._
import java.net._

@Test
class MxmlAnalyserTest 
{
   
   var fileName = "UnusedImports.mxml"
   var url = this.getClass.getResource( fileName )
   	
    @Test
	def hasUnusedImports = 
	{
	   val string = FileUtils.fileToString(new File(url.getPath))
	   assertTrue( new MxmlAnalyser( string ).hasUnusedImports )
	}
    
    @Test
    def testUnusedImportsSingle =
    { 
	   assertUnusedImports( "DsHelpPopup.mxml", 1 )
    }
   
    @Test
   	def getUnusedImports = 
   	{
		assertUnusedImports( "BaseRegionView.mxml", 0 )
		assertUnusedImports( "TestComboBoxYellow.mxml", 0 )
        assertUnusedImports( "UnusedImports.mxml", 1 )
		assertUnusedImports( "CampaignsTest.mxml", 2 )
		assertUnusedImports( "UnusedNamespaces_two.mxml", 2 )
		assertUnusedImports( "MxmlViewWithUnusedNamespaces.mxml", 6 )
   	  	assertUnusedImports( "ImageLinkView.mxml", 1 )
   	}
    
   @Test
   def getUnusedImportsFromWildCard = 
   {
	   assertUnusedImports( "TickerView.mxml", 1 )
   }
	   
   @Test
   def getUnusedImportsFromCommentedImports = 
   {
	   assertUnusedImports( "CommentedImport.mxml", 1 )
   }
    
    @Test
    def getUnusedImportsRepeatedMultipleTimes = 
    {
	   assertUnusedImports( "FileWithTheSameUnusedImportRepeated.mxml", 1 )
    }
    
    private def assertUnusedImports( path : String, expectedLength : Int ) = 
    {
	   	val message : String = " didn't work: imports"
	   	var url = this.getClass.getResource( path )
       	val unused : List[String] = new MxmlAnalyser( FileUtils.filePathToString(url.getPath) ).getUnusedImports
	   	assertNotNull( unused )
	   	assertEquals( path + message, expectedLength, unused.length)
   	}
    
    
    @Test 
    def hasUnusedNamespaces =
    {
      var url = this.getClass.getResource( "MxmlViewWithUnusedNamespaces.mxml" )
      assertTrue( new MxmlAnalyser( FileUtils.filePathToString(url.getPath) ).hasUnusedNamespaces)
    }
   
    
    @Test
    def getUnusedNamespaces = 
    {
      assertUnusedNamespaces( "MxmlViewWithUnusedNamespaces.mxml", 1, "shouldn't have 1 unused namespaces" )
      assertUnusedNamespaces( "TestComboBoxYellow.mxml", 0, "combobox didn't work" )
      assertUnusedNamespaces( "TestComboBoxYellow_withunused.mxml", 2, "combobox didn't work" )
      assertUnusedNamespaces( "BaseRegionView.mxml", 0, "combobox didn't work" )
      assertUnusedNamespaces( "UnusedNamespaces_two.mxml", 3, "combobox didn't work" )
      assertUnusedNamespaces( "ImageLinkView.mxml", 0, "combobox didn't work" )
      assertUnusedNamespaces( "FoundIssues.mxml", 0, "combobox didn't work" )
      assertUnusedNamespaces( "CampaignsTest.mxml", 1, "combobox didn't work" )
    }
    
    @Test 
    def getUnusedImportsFromCodeWithMultipleScriptNodes =
    {
      assertUnusedImports("MxmlWithMultipleScriptBlocks.mxml", 3 )
    }
    
     @Test 
    def getUnusedImportsFromCodeWithMultipleScriptNodesAndAsComments =
    {
      
      assertUnusedImports("MultipleScriptBlocksAndAsComments.mxml", 2 )
    }
    
    private def assertUnusedNamespaces( path : String, expectedLength : Int, message : String ) =
    {
	   	var url = this.getClass.getResource( path )
       	val unused : List[String] = new MxmlAnalyser( FileUtils.filePathToString(url.getPath) ).getUnusedNamespaces
        assertNotNull( unused )
	   	assertEquals( unused.toString + " " + message, expectedLength, unused.length)
    }
}
