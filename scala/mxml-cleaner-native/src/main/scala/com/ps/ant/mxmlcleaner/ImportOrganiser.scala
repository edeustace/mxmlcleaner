package com.ps.ant.mxmlcleaner

import com.ps.ant.mxmlcleaner.parser._
import scala.util.matching._
import java.io.File

class ImportOrganiser( file : File ) 
	extends AnyRef 
	with MxmlParser with Organiser
{
	val fileString : String = FileUtils.fileToString( file )
	val IMPORTS_KEY : String = "!IMPORTS_HERE!"
	val ImportRegex : Regex = new Regex("""import .*?[;|\n]""")
 	val RootPackageRegex : Regex = new Regex(""".*?import (.*?)\..*""")
 
	override def needsOrganising() : Boolean =
	{
		classStringNeedsOrganising( fileString )
	}
	
	override def organise() =
	{
		val organised : String = organiseClass( fileString )
		FileUtils.writeToFile( file, organised )
	}
 	
	def classStringNeedsOrganising( value : String ) : Boolean = 
	{
		val scriptNode : String = getScriptNode( value )
		if( scriptNode == null )
		{
			return false
		}
		val importsList : List[String] = getImportsList( scriptNode )
		val sortedList : List[String] = importsList.sort( (e1, e2) => stringSort(e1,e2) )
		return importsList != sortedList
	}
	
	private def stringSort( itemOne : String, itemTwo : String ) : Boolean =
	{
		( itemOne.toLowerCase compare itemTwo.toLowerCase ) < 0 
	}
	
	private def getImportsList( scriptNode : String ) : List[String] = 
	{
	  	var result : List[String] = List[String]()
    
		for( imp <- ImportRegex findAllIn scriptNode )
		{
		  result = imp :: result
		}
  		result.reverse
	}
	
	
 	private def organiseClass( value : String ) : String = 
    {
		val scriptNode : String = getScriptNode( value )

		if( scriptNode == null )
		{
			return value 
		}
		val importsList : List[String] = getImportsList( scriptNode )
		val sortedList : List[String] = importsList.sort( (e1, e2) => stringSort(e1,e2) )
		
		val importsRemoved : String = value.replaceAll("import .*?[\n|;]", IMPORTS_KEY )
		//TODO: This assumes that the imports are declared contiguously. This may not be the case.
		val index : Int = importsRemoved.indexOf( IMPORTS_KEY )
		val lastIndex : Int = importsRemoved.lastIndexOf( IMPORTS_KEY )

		val firstBit : String = importsRemoved.substring( 0, index )
		val lastBit : String = importsRemoved.substring( lastIndex + IMPORTS_KEY.length )
		
		firstBit + createImportString(sortedList) + lastBit
    }
  	
  	private def createImportString( sortedList : List[String]) : String = 
    {
		var result : String = ""
		var rootPackage : String = ""
		for( importItem <- sortedList )
		{	
			//TODO: Get the correct tab index for the imports
			//TODO: Find out about \r vs \n .. \n causes double lines when mixed with original string
			val currentRoot = getRootPackage(importItem)

			if( rootPackage != currentRoot )
			{
				result += "\t\t\r"
			}
			result += "\t\t" + importItem + "\r"
			
			rootPackage = currentRoot
		}
  		result
      
    }
   
   	private def getRootPackage( importItem : String ) : String = importItem match 
    {
      case RootPackageRegex(v) => v 
      case other @ _ => other
    }
}
