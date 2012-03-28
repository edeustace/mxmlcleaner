package com.ps.ant.mxmlcleaner

import java.io.File

trait Organiser 
{
	def needsOrganising() : Boolean
	def organise()
}
