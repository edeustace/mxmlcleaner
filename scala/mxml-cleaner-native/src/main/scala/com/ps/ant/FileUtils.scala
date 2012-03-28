package com.ps.ant

import java.io.File
import java.io.FileWriter 
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import scalax.data.Implicits._
import scalax.io.Implicits._
import scalax.control.ManagedSequence

import scala.io.Source

object FileUtils
{
	def filePathToString( path : String ) : String = 
	{
		fileToString( new File(path))
	}
 
	def fileToString( file : File ) : String = 
    {
		val lines : ManagedSequence[String] = file.lines
		lines.mkString("\r")
    }
 
	def fileLines(file: java.io.File) = 
	{
		var result : String = ""
		val src = Source.fromFile(file,"UTF-8")
		val lines = src.getLines
    	src.reset
    	lines
	}
 
 
	def writeToFile( file : File, fileString : String ) = 
	{
		val out : OutputStreamWriter = new OutputStreamWriter(new FileOutputStream( file.getPath ),"utf-8");
  
		out.write( fileString )
    	out.close()
	}
 
	def copyFolder( srcPath : String, destPath  : String ) : String =
	{
		println("copyFolder...")
		val src : File = new File( srcPath )
		
		if( !src.exists )
		{
			println( "src doesn't exist: " + src.getPath )
			throw new RuntimeException("the source path doesn't exist: " + srcPath )
		}
		
		if( src.isDirectory )
		{
			val dest : File = new File( destPath)
			if( dest.exists )
			{
				dest.delete
			}
			val success = dest.mkdir
			
			if( !success )
			{
				println("couldn't create dest directory: " + dest.getPath )
			}
			
			for( child <- src.listFiles ; if( isCopyable( child ) ))
			{
				println("child: " + child.getPath )
				copyFolder( child.getPath, destPath + "/" + child.getName )
			}
		}
		else
		{
			copyFile( srcPath, destPath )
		}
		destPath
	}
	
	def copyFile( srcPath : String, destPath : String ) =
	{
		println( "copying: " + srcPath + " to destPath: " + destPath )
		val destFile : File = new File( destPath )
		destFile.createNewFile
		val contents = FileUtils.fileToString( new File( srcPath ) )
		FileUtils.writeToFile( destFile, contents )
	}
 
	def isCopyable( file : File ) : Boolean =
	{
		if( file.isDirectory )
		{
			return !file.getPath.endsWith(".svn")
		}
		return file.getPath.endsWith(".mxml") || file.getPath.endsWith(".txt")
	}
 
 
	def processFiles(f :File, typeCheckFn : File => Boolean, processFn : File => Unit ) : Unit = 
    {
		if(f.isDirectory)
		{
			for(x <- f.listFiles if( typeCheckFn(x) || x.isDirectory )) 
			{
				processFiles(x, typeCheckFn, processFn)
			}
		}
		else processFn(f)
    }
}
