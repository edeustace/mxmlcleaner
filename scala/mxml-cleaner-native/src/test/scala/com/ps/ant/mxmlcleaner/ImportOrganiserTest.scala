package com.ps.ant.mxmlcleaner

import org.junit._
import org.junit.Assert._
import java.io.File

@Test
class ImportOrganiserTest extends AnyRef with CleanerTest
{
	@Before
	def setUp = 
	{
   
	}
 
	@Test
	def needsOrganising =
	{
	  	assertTrue( fileNeedsOrganising( "MxmlViewWithUnusedNamespaces.mxml" ) )
	  	assertTrue( fileNeedsOrganising( "TestBackgroundView.mxml" ) )

	  	assertFalse( "test teaser view doesn't need organising", 
                 fileNeedsOrganising( "TestTeaserView2.mxml" ) )
	  	
        assertFalse( fileNeedsOrganising( "PromotionThumbnailTileView.mxml" ) )
	  	assertFalse( fileNeedsOrganising( "MxmlViewWithUnusedNamespacesAndOrganisedImports.mxml") )
	  	assertFalse( fileNeedsOrganising( "DoesntNeedOrganising.mxml" ) )
	  	/*
	  	*/
	}
 	
 	private def fileNeedsOrganising( filePath : String ) : Boolean = 
    {
      	//System.out.println("checking: " + filePath )
      	val file : File = copyFile( filePath, ".io.copy.mxml" )
		new ImportOrganiser(file).needsOrganising
		
    }
	
	@Test
	def organise = 
	{
	  	assertOrganisingWorks( "MxmlViewWithUnusedNamespaces.mxml" )
    	assertOrganisingWorks( "TestBackgroundView.mxml" ) 
  	}
 
 	private def assertOrganisingWorks( filePath : String ) =
	{
		val file : File = copyFile( filePath, ".io.org2.copy.mxml" )
		val organiser : ImportOrganiser = new ImportOrganiser(file)
		assertTrue( organiser.needsOrganising )
		organiser.organise
  
		assertFalse( new ImportOrganiser(file).needsOrganising )
	}
   
}
