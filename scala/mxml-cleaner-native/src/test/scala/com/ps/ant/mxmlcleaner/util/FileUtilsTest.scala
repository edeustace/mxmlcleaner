package com.ps.ant.mxmlcleaner.util

import org.junit._
import org.junit.Assert._
import java.io.File
import java.net.URL

@Test
class FileUtilsTest extends AnyRef with CleanerTest
{
	@Test
	def copyFolderWorks =
	{
		val url : URL = this.getClass.getResource(".")
		val rootPath : String = url.getPath;
		
		val testFolder : File = new File( rootPath + "/testFolder" )
		println( "testFolder: " + testFolder.getAbsolutePath );
		
		val destPath : String = FileUtils.copyFolder( rootPath + "testFolder", rootPath + "copiedTestFolder" )
		
		val copied : File = new File( destPath )
		val child1 : File = new File( destPath + "/contents.txt" )
		val child2 : File = new File( destPath + "/contents2.txt" )
		
		assertTrue( copied.exists )
		assertTrue( child1.exists )
		assertTrue( child2.exists )
		
		copied.delete
		child1.delete
		child2.delete
		println("")
		
	}
 
	private def createFolder( path : String ) : File = createItem( path, mkDir )
	private def createFile( path : String ) : File = createItem( path, mkFile )
	private def mkDir( file : File ) = file.mkdir
	private def mkFile( file : File ) = file.createNewFile
 
	private def createItem( path : String, fn : File => Boolean ) : File =
	{
		val file : File = new File( path )
		if( file.exists )
		{
			println( "deleting: " + file.getAbsolutePath )
			file.delete
		}
		
		val success : Boolean = fn( file )
		if( !success )
		{
			throw new RuntimeException("couldn't create file: " + file )
		}
		file
	}
}
