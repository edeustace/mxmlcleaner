package com.ps.ant.mxmlcleaner

import com.ps.ant.mxmlcleaner.model._
import scala.util.matching._
import java.io._
import com.ps.ant.mxmlcleaner.parser.MxmlParser
import com.ps.ant.mxmlcleaner.organiser.AttributeListOrganiser


class AttributeOrganiser( rulesFile : File  ) 
	extends AnyRef 
	with MxmlParser
{

	val listOrganiser : AttributeListOrganiser = new AttributeListOrganiser
	val AttributeRegex : Regex = new Regex("""(.*?=".*?")""")
 	val NodeRegex : Regex = new Regex("""(?s)<.*?\s(.*)>""")
	val NodeNameRegex : Regex = new Regex("""(?s)<(.*?)\s.*>""")
 	val Utf8 : String =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
 	val rules : List[String] = initRules( rulesFile )
 	
 	private def initRules( rulesFile : File ) : List[String] =
    {
		if( rulesFile == null || !rulesFile.exists )
		{
			return List("xmlns:mx", "xmlns", "/xmlns:.*?/" )
		}
		var customList :List[String] = List[String]()
		for( line <- FileUtils.fileLines( rulesFile ) )
		{
			val cleaned : String = line.replaceAll("\n","")
			customList =  cleaned :: customList
		}
		customList.reverse 
    }
  
 	
 	def organise( file : File ) = 
    {
		if( needsOrganising(file) )
		{
			println("....organising...")
			val organised = organiseClass( FileUtils.fileToString(file) )
			FileUtils.writeToFile( file, organised )
		}
    }
	
 	def organiseClass( mxml : String ) : String = 
    {
		val rootNode : String = getRootNode( mxml )
		val raw : List[Attribute] = getAttributeList( rootNode )
		val sorted : List[Attribute] = listOrganiser.organise( raw, rules )
		val sortedNode : String = createSortedNode( getRootNodeName( rootNode ), sorted )
		val builder : StringBuilder = new StringBuilder
		builder.append( Utf8 )
  
		builder.append( sortedNode )
		builder.append( getRestOfClass( mxml ) )
		builder.toString
    }
	
 	private def getRootNodeName( rootNode : String ) : String = rootNode match
    {
    	case NodeNameRegex(v) => v
    	case other @_ => null
    }
  
	private def createSortedNode( rootNodeName : String, sorted : List[Attribute]) : String =
	{
		"<" + rootNodeName + "\r\t" + (sorted.init :\ sorted.last + ">" )( _ + "\r\t" + _)
	}
	
	def needsOrganising( file : File ) : Boolean =
	{
		needsOrganisingString( FileUtils.fileToString(file) )
	}
	
	def needsOrganisingString( value : String ) : Boolean = 
	{
		val rootNode : String = getRootNode( value )
		val rawAttributes : List[Attribute] = getAttributeList( rootNode )
		println( "raw: " + getListString(rawAttributes) )
		val sortedAttributes : List[Attribute] = listOrganiser.organise( rawAttributes, rules )
		println( "sorted: " + getListString(sortedAttributes) )
		rawAttributes != sortedAttributes
	}
	
	def getListString( list : List[Attribute]) : String = 
	{	
		( ""  /: list )( _ + "\n" + _ )
	}
 
	def getAttributeList( value : String ) : List[Attribute] = 
	{
		val attributesOnly : String = stripNode( value )
		//println("attrs only: " + attributesOnly )
	  	var list : List[Attribute] =  List[Attribute]()
		for( AttributeRegex(v) <- AttributeRegex findAllIn attributesOnly  )	
		{
			list = new Attribute(v) :: list
		}
  		list.reverse
	}
	
	private def stripNode( value : String ) : String = value match
	{
		case NodeRegex(v) => v;
		case other @_ => other;
	}
 
 	
}
