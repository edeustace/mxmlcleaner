package com.ps.ant.mxmlcleaner

import java.io.File

class MxmlCleanerImpl
{
	var rulesPath : String = null
 
 	def run( sourcePath : String, rulesPath : String  ) = 
	{
	  	this.rulesPath = rulesPath
	  	val sourceFile = new File( sourcePath )
	  	println("running..")
	  	processFiles( sourceFile, mxmlFile, process )
	}
    
    private def processFiles(f :File, typeCheckFn : File => Boolean, processFn : File => Unit ) : Unit = 
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
    
    private def process( file : File ) = 
    {	
    	val cleaner : MxmlCleaner = new MxmlCleaner
    	var message : String = ""
     
    	if( cleaner.needsCleaning( file ) )
    	{
    		cleaner.clean( file  )
    		message = file.getName + " cleaned,"
    	}
    	
    	val importOrganiser : ImportOrganiser = new ImportOrganiser( new File( file.getPath ) )
    	if( importOrganiser.needsOrganising )
    	{
    		importOrganiser.organise
    		message += " imports "
    	}
    	var rules : File = null;
    	if( rulesPath != null )
    	{
    		//println("using custom rules file")
    		rules = new File( rulesPath )
    	}
   		val attributeOrganiser : AttributeOrganiser = new AttributeOrganiser( rules )
    	if( attributeOrganiser.needsOrganising(new File( file.getPath )) )
    	{
    		attributeOrganiser.organise(new File( file.getPath ))
    		message +=  "attributes"
    	}
    	
    	if( message.length > 1 )
    	{
    		println( message )
    	}
    }
    
	private def mxmlFile(f :File) = f.getName.endsWith(".mxml")
 
}
