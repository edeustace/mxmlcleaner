package com.ps.ant.mxmlcleaner.model

import scala.util.matching._

class Attribute( attributeString : String )
{
	val name : String = getName( attributeString )
	val value : String = getValue( attributeString )
 
	private def getName( attribute : String ) : String =
	{
		val AttributeRegex : Regex = new Regex("""[\s|\t]*(.*?)="(.*)""")
		val AttributeRegex(name,value) = attribute
		name
	}
	
	private def getValue( attribute : String ) : String = 
	{
		val AttributeRegex : Regex = new Regex("""[\s|\t]*(.*?)="(.*)""")
		val AttributeRegex(name,value) = attribute
		stripQuotes( value )
	}
 
	private def stripQuotes( value : String ) : String = 
	{
		value.replaceAll("\"", "")
	}
 
	override def toString = 
	{
		name + "=\"" + value + "\""
	}
 
}
