package com.ps.ant.mxmlcleaner

import com.ps.ant.FileUtils
import com.ps.ant.mxmlcleaner.util.StringInspector

import java.io.File
import java.io.FileWriter
import scala.util.matching._

class MxmlCleaner 
{
	var analyser : MxmlAnalyser = null
	
	def needsCleaning( value : String ) : Boolean =
 	{
    	if( analyser == null )
	  	{
	  		analyser = new MxmlAnalyser( value )
	  	}
		analyser.hasUnusedImports || analyser.hasUnusedNamespaces 
	
 	}
	
  	def needsCleaning( file : File ) : Boolean = 
	{	
	  	needsCleaning( FileUtils.fileToString( file ) )
	}
	
   	def clean( fileToClean : File ) : Unit =
	{
	  	val fileString : String  = FileUtils.fileToString(fileToClean)
		
		val cleaned : String = clean( fileString )
     	val path : String = fileToClean.getPath
     	FileUtils.writeToFile( new File( path ), cleaned )
   	}
   
   	def clean( fileString : String ) : String = 
    {
		analyser = new MxmlAnalyser( fileString )
     	
		if( !needsCleaning( fileString ) )
		{
			return fileString
		}
		cleanNamespace( cleanImports( fileString ) )
    }
    
    def cleanNamespace( fileString : String ) : String = 
    {
    	analyser = new MxmlAnalyser( fileString )
    	var cleaned = fileString
    	strip( 	fileString, 
    			analyser.getUnusedNamespaces, 
    			(w) => "xmlns:" + w + "=\".*?\"" )
  	}
    
    def cleanImports( fileString : String ) : String =
    {
      	analyser = new MxmlAnalyser( fileString )
    	var cleaned = fileString
    	strip( 	fileString, 
    			analyser.getUnusedImports, 
    			(w) => "import .*?" + w + ";" )
    }
    
    private def strip( fileString : String, unusedItems : List[String], regexify : String => String ) : String = 
    {
    	var cleaned = fileString
  		for( item <- unusedItems )
  		{
  			cleaned = cleaned.replaceAll( regexify( item ), "" )
  		}
    	cleaned
    }
    
    private def empty( line : String ) : Boolean = { StringInspector.emptyLine( line ) }
}
