package com.ps.ant.mxmlcleaner


import org.junit._
import org.junit.Assert._
import java.io.File
import java.net.URL

@Test
class MxmlCleanerImplTest 
{
	@Test
    def tryAllFiles =
    {
    	var url = this.getClass.getResource( "." )
    	
    	val projectPath : String = copyFolder( url.getPath + "/DummyProject1", url.getPath + "/_test_DummyProject1")
    	val cleaner : MxmlCleanerImpl = new MxmlCleanerImpl
    	cleaner.run( projectPath, null )
    	assertFilesAreClean( projectPath )
    }
    
    @Test
    def trySingleFile = 
    {
      var url = this.getClass.getResource(".")
      
      val projectPath : String = copyFolder( url.getPath + "/TroublesomeFiles", url.getPath + "/_test_TroublesomeFiles")
      val cleaner : MxmlCleanerImpl = new MxmlCleanerImpl
      cleaner.run( projectPath, null )
      assertFilesAreClean( projectPath )
    }
    
    private def copyFolder( path : String, destPath : String ) : String = 
    {
    	FileUtils.copyFolder( path, destPath )
    }
    
    private def assertFilesAreClean( path : String ) =
    {
    	FileUtils.processFiles( new File(path), mxmlFile, assertFileIsClean)
    }
    
    private def mxmlFile( file : File ) : Boolean =
    {
      file.getPath.endsWith(".mxml")
    }
    
    private def assertFileIsClean( file : File ) =
    {
		val analyser : MxmlAnalyser = new MxmlAnalyser( FileUtils.fileToString(file) )
		println( "> unused imports: " + analyser.getUnusedImports )
    	println( "> unused import s length: " + analyser.getUnusedImports.length )
    	assertFalse( file.getName + " shouldn't have any unused imports", analyser.hasUnusedImports )
    	println( "> unused namespaces: " + analyser.getUnusedNamespaces )
    	println( "> unused namespaces length: " + analyser.getUnusedNamespaces.length )
    	assertFalse( file.getName + " shouldn't have any unused namespaces", analyser.hasUnusedNamespaces )
    	assertFalse( file.getName + " shouldn't have need imports organised", new ImportOrganiser(file).needsOrganising )
    	assertFalse( file.getName + " shouldn't have need attributes organised", new AttributeOrganiser(null).needsOrganising(file) )
    }

}
