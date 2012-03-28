package com.ps.ant.mxmlcleaner

import org.junit._
import Assert._
import java.io.File
import java.io.FileWriter
import com.ps.ant.mxmlcleaner._

@Test
class MxmlCleanerTest extends AnyRef with CleanerTest 
{
	val cleaner : MxmlCleaner = new MxmlCleaner()
	
	

	@Test 
	def sparkNamespacesArePreserved = 
	{

		val in = """<?xml version="1.0" encoding="utf-8"?>
<s:Group xmlns:fx="http://ns.adobe.com/mxml/2009" 
    xmlns:s="library://ns.adobe.com/flex/spark" 
    xmlns:mx="library://ns.adobe.com/flex/mx" 
    width="100%" height="100%">
    <s:Rect width="100%" height="100%"/>
</s:Group>"""

		val out = cleaner.clean(in)

		val expected = """<?xml version="1.0" encoding="utf-8"?>
<s:Group xmlns:fx="http://ns.adobe.com/mxml/2009" 
    xmlns:s="library://ns.adobe.com/flex/spark" 
    xmlns:mx="library://ns.adobe.com/flex/mx" 
    width="100%" height="100%">
    <s:Rect width="100%" height="100%"/>
</s:Group>"""

    assertEquals( expected, out )

	}
 	
	@Test
	def cleanWorks = 
	{
		assertCleanFile( "MxmlViewWithUnusedNamespaces.mxml" )
		assertCleanFile( "UnusedNamespaces_two.mxml" )
		assertCleanFile( "CampaignsTest.mxml" )
		assertCleanFile( "UnusedNamespaces.mxml" )
		assertCleanFile( "ImageLinkView.mxml" )
		assertCleanFile( "TwoPartCPRNumberInput.mxml" )
		assertCleanFile( "SparkComponentWithSparkNamepsaces.mxml")
	}
 	
 	@Test
 	def commentedImportsWork = 
    {
		assertCleanFile( "CommentedImport.mxml" )
	}
	
  	@Test
	def loginFormWorks = 
	{
		assertCleanFile( "LoginForm.mxml" )
	}
 
 	
 	@Test
 	def multipleScriptNodecleanWorks = 
    {
		assertCleanFile( "MultipleScriptNodesUnusedImports.mxml" )
	}

  	@Test
	def wildCardDoesntThrowException = 
	{
		assertCleanFile( "TickerView.mxml" )
	}
 	
	@Test
	def fileWithDodgyEncoding =
	{
		println("testing dodgy file...")
		assertCleanFile( "DsHelpPopup.mxml" )
	} 
 
 	@Test
 	def cprInput =
    {
      assertCleanFile( "TwoPartCPRNumberInput.mxml" )
      
    }
 
	private def assertCleanFile( filePath : String ) =
   	{
   	  	System.out.println("\nasserting: " + filePath );
		val fileToClean = copyFile( filePath, ".mc.copy.mxml" )
		assertTrue( "file: " + fileToClean.getPath + " should be cleaned", needsCleaning( fileToClean ) )
		cleaner.clean( fileToClean )
		assertFalse( "file: " + fileToClean.getPath + " shouldnt be cleaned", needsCleaning( fileToClean ) )
    }
	
	private def needsCleaning( file : File ) : Boolean = 
	{
	  	println("running needsCleaning")
		val analyser : MxmlAnalyser = new MxmlAnalyser( FileUtils.fileToString(file) )
		if( analyser.hasUnusedImports  )
		{
			val unused : List[String] = analyser.getUnusedImports 
			println( "unused imports are...." ) 
			println( unused )
			println( "unused imports are...." )
			return true
		}
		if( analyser.hasUnusedNamespaces )
		{
			println( "unused namespaces: " + analyser.getUnusedNamespaces )
			return true
		}
		return false
	}
	
	
}
