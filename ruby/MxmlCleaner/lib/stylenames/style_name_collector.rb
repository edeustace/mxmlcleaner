# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'util/string_util'

class StyleNameCollector

	 MXML_REGEX = /styleName="(.*?)"/
	 #CSS_REGEX = /[^a-z][^0-9]\.(.*?)[{|,|\n]/
	 CSS_REGEX = /^\.(.*?)(\{|$|,)/
	 DECLARED_IN_CSS_REGEX = /style-name: "\.(.*?)";/
  
  def get_stylenames( path, regex, index )
	 result = Array.new
	# #puts "----"
	# #puts "regex: #{regex}"

	 file = StringUtil.get_file_as_string( path )

	 stylename_matches = file.scan( regex )

	 stylename_matches.each { |item|
		cleaned = item[index].strip
		
		if( result.index( cleaned ) == nil )
			 ##puts "stylename: #{cleaned}"
			 result << cleaned

		end
		
	 }
	 ##puts "----"
   result
	
	end

	def get_regex_for_file( path )

	  if( path.index( ".mxml" ) != nil )
		  return MXML_REGEX
	  end

	  if( path.index( ".css" ) != nil )
		  return CSS_REGEX
	  end
	end

end

class StyleUtils

	MXML_REGEX = /styleName="(.*?)"/i
	CSS_REGEX = /^\.(.*?)(\{|$|,)/
	#DECLARED_IN_CSS_REGEX = /[style-name|StyleName]\s*?:.*?"(.*?)".*?;/
	DECLARED_IN_CSS_REGEX = /.*?(style-name|StyleName).*?:.*?"(.*?)".*?;/

  @finder

  def initialize
	 @finder = StyleNameCollector.new
  end

  def find_unused_style_names( src_path )

	 mxmlDeclarations = Array.new
	 add_declarations( src_path, mxmlDeclarations, ".mxml", MXML_REGEX, 0)

	 #puts "mxml declarations"
	 print_array mxmlDeclarations
	 #puts "---------------"
	 internalCssLinks = Array.new
	 add_declarations( src_path, internalCssLinks, ".css", DECLARED_IN_CSS_REGEX, 1 )
	 #puts "css links"
	 print_array internalCssLinks
	 #puts "---------------"

	 mxmlDeclarations << internalCssLinks
	 mxmlDeclarations.flatten!

	 cssDeclarations = Array.new
	 add_declarations( src_path, cssDeclarations, ".css", CSS_REGEX, 0 )
	 #puts "css.."
	 print_array cssDeclarations
	 #puts "---------------"

	 unused = Array.new

	 cssDeclarations.each do |decl|
		##puts "decl: #{decl}"
		if( mxmlDeclarations.index( decl ) == nil )
			unused << decl
		end
	 end

	 unused
	 
  end

  def print_array( array )
	 array.each do |item|
		#puts item
	 end
  end
  private
  def add_declarations( src_path, array, suffix, regex, index )
	 ##puts "src_path: #{src_path}"
	 declarations = Array.new
	 Dir.chdir(src_path)
	 Dir[ '**/*' + suffix ].each do |path|
		##puts "path: #{path}"
		declarations = @finder.get_stylenames(path, regex, index)
		declarations.each do |declaration|
		  if( array.index( declaration ) == nil )
			 array << declaration
		  end
		end
	 end
	end

end
