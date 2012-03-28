package com.ps.ant.mxmlcleaner.util

object StringInspector 
{

	def emptyLine( value : String ) : Boolean = 
    {
			if( value == null )
			{
				return true
			}	
			if( value.length == 0 )
			{
				return true
			}
   
			value.matches("^[\\t\\s\\r]*$")
    }
}
