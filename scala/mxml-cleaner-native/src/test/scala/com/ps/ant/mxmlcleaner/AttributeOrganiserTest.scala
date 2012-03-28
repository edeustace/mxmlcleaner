package com.ps.ant.mxmlcleaner

import org.junit._
import org.junit.Assert._
import java.io._
import java.net.URL

@Test
class AttributeOrganiserTest extends AnyRef with CleanerTest
{
  	@Test 
	def needsOrganisingNamespaces = 
	{
		val oneListedRule : List[String] = List( "xmlns:mx=\"http://www.macromedia.com/2006\"", "xmlns:com=\"blah.test\"")
		assertList( "one listed rule", oneListedRule )
	}
  	
  	
  	@Test
  	def multipleRulesNeedsOrganising =
    {
		val twoListedRules : List[String] = List( "xmlns:mx=\"http://www.macromedia.com/2006\"", "xmlns=\"blah.test\"")
		assertList( "two listed rule", twoListedRules )
		
		val threeListedRules : List[String] = List( "xmlns:mx=\"http://www.macromedia.com/2006\"", "xmlns=\"blah.test\"", "xmlns:com=\"blah.*\"")
		assertList( "three listed rule", threeListedRules )
		//TODO this is here to prevent the following maven error:
		//java.lang.Exception: Method needsOrganisingEverything should be void
		//need to find out why this happens
		println("")
	}
  	
  	@Test 
	def needsOrganisingEverything = 
	{
		val oneListedRule : List[String] = List( "xmlns:mx=\"http://www.macromedia.com/2006\"", "creationComplete=\"doThis()\"")
		assertList( "one listed rule with normal attribute", oneListedRule )
		
		val withCustomNamespace : List[String] = List( "xmlns:mx=\"http://www.macromedia.com/2006\"", "xmlns:custom=\"custom.blah\"", "creationComplete=\"doThis()\"")
		assertList( "with custom namespace", withCustomNamespace )
		
		val withCustomNamespace2 : List[String] = 
				List( "xmlns:mx=\"http://www.macromedia.com/2006\"", 
						"xmlns=\"custom.blah\"",
						"xmlns:custom=\"custom.blah\"",
						"xmlns:next=\"custom.blah\"",
						"click=\"doClick()\"",
						"creationComplete=\"doThis()\"",
						"height=\"100%\"",
						"verticalAlign=\"middle\"",
						"width=\"500px\"")
		assertList( "with custom namespace", withCustomNamespace2 )
		println("")
	}
 	
 	private def assertList( message : String, sortedList : List[String] ) =
	{
	  	val normal = createTestString( sortedList )
	  	val reversed = createTestString( sortedList.reverse )
	  	println( "normal:" )
	  	println( normal )
	  	println( "reversed:" )
	  	println( reversed )
	  	println( "no need to organise..." )
		assertFalse( message + " shouldn't need organising", new AttributeOrganiser(null).needsOrganisingString( normal ) )
		println( ">> need to organise..." )
		assertTrue(  message + " reverse should need organising", new AttributeOrganiser(null).needsOrganisingString( reversed ) )
	}
 	
  	private def deleteFile( file : File ) =
    {
		val success : Boolean = file.delete
		if( !success )
		{
			throw new RuntimeException("couldn't delete file: " + file.getName )
		}
    }
   
	private def createTestString( list : List[String] ) : String =
	{
		var result : String = ""
		for( v : String <- list )
		{
			result = result + v + "\n"
		}
		result
	}
 
 	private def createTestFile( list : List[String] ) : File = 
    {
      	val file : File = new File( "dummyFile_" + Math.random + ".txt" )
     	FileUtils.writeToFile( file, createTestString( list ) )
     	file
    }
  	
  	@Test
	def organiseWorks = 
	{
		assertOrganiseFile( "ContentPopup.mxml" )
		assertOrganiseFile( "MxmlViewWithUnusedNamespaces.mxml" )
	}

	@Test
	def organiseWorksWithSparkNamespaces = 
	{
		assertOrganiseFile( "SparkComponent.mxml")
	}
 
 	@Test
 	def organiseArticle =
    {
      	assertOrganiseFile( "DsArticlesPopup.mxml" )
    }
	
  	@Test
  	def customRules =
    {
		println(">>")
  		val fileToClean = copyFile( "DsArticlesPopup.mxml", ".rules.copy.mxml" )
  		val thisPath : URL = this.getClass.getResource(".")
  		val rulesPath : String = thisPath.getPath + "/rules/customRules.properties"
  		val organiser : AttributeOrganiser = new AttributeOrganiser(new File( rulesPath ))
  		assertTrue( fileToClean.getName + " needs organising", organiser.needsOrganising(fileToClean) )
  		
  		
  		organiser.organise(fileToClean)
  		
  		val assertOrganiser : AttributeOrganiser = new AttributeOrganiser( new File( rulesPath ) )
  		println("--------------- should be cleaned now" )
  		
    
  		assertFalse(  fileToClean.getName + " doesn't need organising after being cleaned", assertOrganiser.needsOrganising(new File( fileToClean.getPath )) )
  		
  		
  		//try with a different ruleset
   		val differentRulesPath : String = thisPath.getPath + "/rules/customRulesTwo.properties"
  		val differentRulesOrganiser : AttributeOrganiser = new AttributeOrganiser( new File( differentRulesPath ) )
  		println("--------------- open saved file and see if it needs organising" )
  		assertTrue(  fileToClean.getName + " should need organising", differentRulesOrganiser.needsOrganising(new File( fileToClean.getPath )) )
    }
   
 	private def assertOrganiseFile( filePath : String ) = 
    {
      	System.out.println("\nasserting: " + filePath );
		val fileToClean = copyFile( filePath, ".org.copy.mxml" )
		assertTrue( "file: " + fileToClean.getPath + " should be organised", needsOrganising( fileToClean ) )
		new AttributeOrganiser(null).organise(fileToClean)
		assertFalse( "file: " + fileToClean.getPath + " shouldnt need to be organised", needsOrganising( fileToClean ) )
    }
  
  	private def needsOrganising( file : File ) : Boolean = 
    {
      	new AttributeOrganiser(null).needsOrganising(file)
    }
}
