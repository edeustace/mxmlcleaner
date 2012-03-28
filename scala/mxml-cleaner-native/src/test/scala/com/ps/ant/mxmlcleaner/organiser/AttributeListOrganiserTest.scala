package com.ps.ant.mxmlcleaner.organiser

import org.junit._
import org.junit.Assert._

import com.ps.ant.mxmlcleaner.model.Attribute

@Test
class AttributeListOrganiserTest 
{
	@Test
	def sort = 
	{
	  val unsorted = getAttrList( "xmlns", "xmlns:mx" )
	  
	  assertSort( List("xmlns:mx", "xmlns", "/xmlns:.*?/"), getAttrList( "xmlns:mx", "xmlns" ), unsorted )
	  assertSort( List("xmlns", "xmlns:mx", "/xmlns:.*?/"), getAttrList("xmlns", "xmlns:mx"), unsorted )
	}
 
	@Test
	def sortComplex = 
	{
		val defaultRules = List("xmlns:mx", "xmlns", "/xmlns:.*?/","width,height", "creationComplete,preInitialize")
		val expected = getAttrList("xmlns:mx","xmlns","xmlns:test", "height", "width","creationComplete", "preInitialize")
		assertSort( defaultRules, expected, shuffle(expected) )
  
		val crazyRules = List("width,height","xmlns:mx", "creationComplete,preInitialize", "xmlns", "/xmlns:.*?/")
		val crazyExpected = getAttrList("height", "width","xmlns:mx", "creationComplete", "preInitialize", "xmlns","xmlns:apple", "xmlns:banana", "xmlns:carrot", "xmlns:test")
		assertSort( crazyRules, crazyExpected, shuffle(crazyExpected) )
	}
	
	@Test
	def customNamespacesOnly = 
	{
		val rules = List("xmlns:mx", "xmlns", "/xmlns:.*?/")
		val expected = getAttrList("xmlns:apple","xmlns:banana", "xmlns:carrot", "xmlns:test")
		assertSort( rules, expected, shuffle(expected) )
	}
	
	private def shuffle(xs: List[Attribute]): List[Attribute] = xs match 
	{
		case List() => List()
		case xs => { 
			val i = new scala.util.Random().nextInt(xs.size);
			xs(i) :: shuffle(xs.take(i) ++ xs.drop(i+1))
		}
	}
 
	private def tag( name : String ) : String = 
	{
			name + "=\"dummyTag\""
	}

 
	private def assertSort( rules : List[String], expected : List[Attribute], unsorted : List[Attribute] ) =
	{
		val organiser : AttributeListOrganiser = new AttributeListOrganiser
		val result : List[Attribute] = organiser.organise( unsorted, rules )
		assertEquals( expected.toString, result.toString )
	}

	private def getAttrList( args : String* ) : List[Attribute] = 
	{
		var result : List[Attribute] = List()
		for( arg <- args)
		{
		  result = new Attribute( tag(arg) ) :: result
		}
		result.reverse
	}
}
