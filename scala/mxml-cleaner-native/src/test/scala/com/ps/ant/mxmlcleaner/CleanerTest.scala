package com.ps.ant.mxmlcleaner

import java.io._

trait CleanerTest 
{
	def copyFile( fileName : String, suffix : String ) : File =
	{
		var url = this.getClass.getResource( fileName )
		val text = FileUtils.fileToString(new File( url.getPath ))
		val copyPath = url.getPath.replace(".mxml", suffix)
		FileUtils.copyFile( url.getPath, copyPath )
		new File( copyPath )
	}
}
