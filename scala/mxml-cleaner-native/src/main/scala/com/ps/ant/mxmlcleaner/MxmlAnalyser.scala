package com.ps.ant.mxmlcleaner

import java.io._
import java.net.URL
import scala.util.matching._
import java.util.regex.Pattern
import com.ps.ant.mxmlcleaner.parser.MxmlParser

class MxmlAnalyser( val fileString : String ) 
	extends AnyRef 
	with MxmlParser
{
	val RESERVED_NAMESPACES = List("fx","s","mx","fb")

 	val ImportStatement = new Regex(""".*import (.*?)[;|\n]""")
	
	def hasUnusedImports() : Boolean = { getUnusedImports().length >= 1 }

	def getUnusedImports() : List[String] = 
	{
		val script = getActionScript( fileString )
		//println("imports: " + script )
		
		if( script == null )
		{
			return List[String]()
		}
		var unused : List[String] =  List[String]()

		for( ImportStatement(v) <- ImportStatement findAllIn script  )
		{
			val className : String = getClassName( v )
   
			if( !importInUse( fileString, className ) && !unused.contains(v) )
			{
				unused = v :: unused
			}
		}
		unused
	}
	
	def hasUnusedNamespaces() : Boolean = { getUnusedNamespaces.length >= 1 }
 
 	/**
 	 * Return the unused namespaces in a list eg: List('blah', 'blahblah')
 	 * Note: some key namespaces are ignored eg: fx, s, fb
 	 */
	def getUnusedNamespaces() : List[String] = 
	{
		var unused : List[String] =  List[String]()
    val rootNode = getRootNode( fileString )
	   	
		for( v <-  getNamespaces( rootNode ) )
		{
			if( !RESERVED_NAMESPACES.contains(v) )
			{
				val count : Int = findNumberOfOccurrences( fileString, "<" + v + ":" )
				
				if( count == 0 )
				{
					unused = v :: unused
				}
			}
		}	
	  unused
	}
	
	private def getClassName( fullName : String ) : String = { fullName.substring( fullName.lastIndexOf(".") + 1, fullName.length ) }
	private def fileToString( file : File ) : String = { FileUtils.fileToString( file ) }
	private def hasScriptNode( value : String ) : Boolean = getScriptNode( value ) != null 
}