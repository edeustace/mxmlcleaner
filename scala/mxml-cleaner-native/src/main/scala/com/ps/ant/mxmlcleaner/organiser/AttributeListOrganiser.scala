package com.ps.ant.mxmlcleaner.organiser

import com.ps.ant.mxmlcleaner.model.Attribute

class AttributeListOrganiser 
{
	def organise( list : List[Attribute], rules : List[String] ) : List[Attribute] =
	{
		list.sort( (e1, e2) => ( compareAttributes( e1.name , e2.name, rules ) )  )
	}
 
	private def compareAttributes( itemOne : String, itemTwo : String, rules : List[String] ) : Boolean =
    {
		val itemOneIndex : Int = getItemIndex( itemOne, rules )		
		val itemTwoIndex : Int = getItemIndex( itemTwo, rules )	
		if( itemOneIndex == itemTwoIndex )
		{
			return ( itemOne compareTo itemTwo ) < 0
		}
		
		if( itemOneIndex == -1 )
		{
			return false
		}
		
		if( itemTwoIndex == -1 )
		{
			return true
		}
  
		itemOneIndex < itemTwoIndex
    }
	
	private def getItemIndex( name : String, rules : List[String] ) : Int = 
	{
		//println("name to find: " + name )
		var simpleStringIndex : Int = getSimpleStringIndex( name, rules )
		
		if( simpleStringIndex != -1 )
		{
			return simpleStringIndex
		}
  
		var regexIndex : Int = getRegexIndex( name, rules )
		if( regexIndex != -1 )
		{
			return regexIndex
		}
		
		-1
	}
 
	private def getSimpleStringIndex( name : String, rules : List[String]) : Int =
	{
		def hasItem( name : String, arr : Array[String] ) : Boolean = arr.indexOf(name) != -1;
  
		for( rule <- rules )
		{
			val items : Array[String] = rule.split(",")
			
			if( hasItem(name, items) && !regexRule(rule))
			{
				//println("found: " + name + " @ " + rules.indexOf(rule) )
				return rules.indexOf(rule)
			}
		}
		-1
	}
	
	private def regexRule( rule : String ) : Boolean = 
	{
		rule.startsWith("/") && rule.endsWith("/")
	}
 
	private def getRegexIndex( name : String, rules : List[String] ) : Int =
	{
		for( rule : String <- rules )
		{
			if( regexRule(rule) )
			{
				var expr : String = rule.substring(1)
				expr = expr.substring( 0, expr.length - 1)
			
				if( name.matches( expr ) )
				{
					return rules.indexOf( rule )
				}
			}
		}
		-1
	}
}
