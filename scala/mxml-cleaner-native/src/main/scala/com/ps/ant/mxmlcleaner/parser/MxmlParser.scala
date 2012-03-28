
package com.ps.ant.mxmlcleaner.parser

import scala.util.matching._
import scala.util.parsing.combinator.RegexParsers
import scala.xml.XML
import scala.xml.Node
import scala.xml.Elem
import scala.xml.MetaData
import scala.xml.Group
import scala.util.matching._
import scala.collection.mutable.ArrayBuffer

trait MxmlParser 
{
	val RootNode = new Regex( """(?s)<\?.*?\?>.*?(<[^!].*?>)(.*)""" )
	val RootNodeNoUtf8Declaration = new Regex("""(?s).*?(<[^!].*?>)(.*)""" )
	val ScriptNode = new Regex("""(?s)<mx:Script>.*<!\[CDATA\[(.*)\]\]>.*</mx:Script>""")
	val ScriptNodeNoCData = new Regex("""(?s).*<mx:Script>(.*)</mx:Script>.*""")
	val NameSpaceDeclaration = new Regex(""".*?xmlns:?([a-z0-9A-Z]*?)=""")
 
	def getRootNode( mxml : String ) : String = mxml match
	{
     	case RootNode(node,remainder) => node;
     	case RootNodeNoUtf8Declaration(node,remainder) => node;
     	case other @ _ => other;
	}
 
	def getRestOfClass( mxml : String ) : String = mxml match
	{
     	case RootNode(node,remainder) => remainder;
     	case RootNodeNoUtf8Declaration(node,remainder) => remainder;
     	case other @ _ => other;
	}
 
	def getScriptNode( mxml : String ) : String = mxml match
	{
	  	case ScriptNode(v) => v
	  	case ScriptNodeNoCData(v) => v
	  	case _ => null 
	}
 
	def getActionScript( mxml : String ) : String = 
	{
		var stringOut : String = ""
  
		for( node <- XML.loadString( mxml ) \\ "Script" )
		{
			stringOut = stringOut + node.text
		}
		stripComments(stringOut)
	}
 
	private def stripComments( asCode : String ) : String = 
    {
		val withoutSingleLineComments = replaceAll(asCode, """[\w]*//.*""")
		val withoutMultilineComments = withoutMultiline( withoutSingleLineComments, new Regex("""(?s)(.*)(/\*.*\*/)(.*)""") )
		withoutMultilineComments
    }
 
	private def withoutMultiline( code : String, regex : Regex) : String = code match
	{
	  case regex(before,comment,after) => withoutMultiline(before + after, regex )
	  case other @ _ => other
	}
 
	private def replaceAll( value : String, regex : String ) : String = value.replaceAll( regex, "" )
 
	def getNamespaces( node : String ) : List[String] = 
	{
		var namespaces : List[String] = List[String]()
		
		for( NameSpaceDeclaration(v) <- NameSpaceDeclaration findAllIn node )
		{
			if( v != "" )
			{
				namespaces = v :: namespaces
			}
		}	
		namespaces.reverse
	}
 
	def importInUse( fileString : String, className : String ) : Boolean =
	{
		//check all the attributes
		if( classDeclaredInBinding( fileString, className ))
		{
			return true
		}	
		//check actionscript
		if( classUsedInScript( fileString, className ))
		{
			return true 
		}	
		false
	}
 
	def classUsedInScript( fileString : String, className : String ) : Boolean =
	{
		if( className.equals("*") )
		{
			return true;
		}
   
		val as : String = getActionScript( fileString )
 
		val count : Int = findNumberOfOccurrences( as, "[^a-z|A-Z]" + className + "[^a-z|A-Z]" )
		val importCount : Int = findNumberOfOccurrences( as, "import .*?\\." + className + "[^a-z|A-Z]")
		//println("normalCount:" + count )
		//println("importCount: " + importCount )
		count > importCount
	}
 
	/**
		Finds bound class declarations eg: 
		<mx:Label text="{ ClassName }"/>
		<mx:Label text="{ ClassName.NAME }"/>
	*/
	def classDeclaredInBinding( mxml : String, className : String ) : Boolean =
	{
		if( className.equals("*"))
		{
			return false
		}
		val xml = XML.loadString( mxml )
		
		if( hasStaticDeclaration( xml, className ))
		{
			return true
		}
		
		if( hasNewDeclaration( xml, className ))
		{
			return true
		}
  
		if( hasCastDeclaration( xml, className ))
		{
			return true
		}
		if( hasClassDeclaration( xml, className ))
		{
			return true
		}
		
		val testResults = xml \\ "_" filter attributeValueEquals(""".*\{.*"""+ className +"""[\s|\.|\)|\(].*\}""")
		return testResults.length > 0
	}
	
	private def hasStaticDeclaration( xml : Elem, className : String ) : Boolean = 
	{
		val testResults = xml \\ "_" filter attributeValueEquals( """.*""" + className +"""\..*""")
		testResults.length > 0
	}
 
	private def hasNewDeclaration( xml : Elem, className : String ) : Boolean = 
	{
		val testResults = xml \\ "_" filter attributeValueEquals( """.*new\s*""" + className +"""\s*\(.*""")
		testResults.length > 0
	}
 
	private def hasCastDeclaration( xml : Elem , className : String ) : Boolean = 
	{
		val testResults = xml \\ "_" filter attributeValueEquals( """.*as\s*""" + className + """[^a-z].*\s.*""")
		testResults.length > 0
		
	}
	private def hasClassDeclaration( xml : Elem , className : String ) : Boolean = 
	{
			val testResults = xml \\ "_" filter attributeValueEquals( """[\s|\t]*\{[\s|\t]*""" + className + """[\s|\t]*\}[\s|\t]*""")
			testResults.length > 0
			
	}
	
	private def attributeValueEquals( value : String)(node : Node ) : Boolean = node.attributes.exists(_.value.toString.matches( value ))
	
	private def matches( value : Seq[Node], regex : String ) : Boolean = 
	{
			val result : Boolean = value.toString.matches( regex )
			result 
	}
 
	def findNumberOfOccurrences( source : String, search : String ) : Int =
	{
		var counter = 0
		val searchComplete : String =  search 
		for( matched <- searchComplete.r findAllIn source )
		{
			counter += 1
		}
		counter 
	}
 
}
