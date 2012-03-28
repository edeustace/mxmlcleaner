class MxmlImportOrganiser

  SCRIPT_NODE_REGEX = /(<mx:Script\s*?>.*?<\/mx:Script\s*?>)/m
  OPEN_SCRIPT_REGEX = /<mx:Script\s*?>/

  OPEN_CDATA_TAG = "<![CDATA["
  OPEN_SCRIPT_TAG = /<mx:Script\s*?>/


  
  def needs_organising?( mxml_file )
		lines = read_file(mxml_file)

        if( !lines.include? "import ")
            return false
		end

        script_node = lines.scan( SCRIPT_NODE_REGEX )[0][0]
        imports = get_imports( script_node )
		sorted_imports = sort_string_array( imports )

		if( imports == sorted_imports )
            return false
		end

        true
  end


  def tidy_imports( mxml_file )
      if( !needs_organising?(mxml_file))
          return
      end

	 store_lines(mxml_file)

	 if( !@stored_lines.include? "import ")
		return
	 end

	 tidy
	 write_file(mxml_file)

  end

  def tidy_imports_in_script_node( script_node )

	 #puts "input:  \n#{script_node}\n---\n"
	 imports = get_imports( script_node )

	 sorted = sort_string_array( imports )

	 sorted_imports_string = build_string( sorted, get_indent( script_node) )
	 node_with_no_imports = remove_imports( script_node )
	 #puts "no imports:  \n#{node_with_no_imports}\n---\n"
	 index = get_insertion_index( node_with_no_imports )

	 result = node_with_no_imports.insert( index, sorted_imports_string )
	 #puts result
	 result
  end
  

  private

  def get_insertion_index( script_node )
	  if( script_node.include?( OPEN_CDATA_TAG ) )
	 	 return script_node.index( OPEN_CDATA_TAG ) + OPEN_CDATA_TAG.length
	 else
		index = script_node.index( OPEN_SCRIPT_REGEX )
		open_tag = script_node.scan( OPEN_SCRIPT_REGEX )[0]
		return  index + open_tag.length
	 end

  end
  def tidy
	 node = @stored_lines.scan( SCRIPT_NODE_REGEX )[0][0]
	 cleaned = tidy_imports_in_script_node( node )
	 @stored_lines.gsub!( SCRIPT_NODE_REGEX, cleaned )
  end

  def read_file( file )
	 result = ""
      File.open(file, "r").each do |line|
        result <<  line
      end
		result
  end
  def store_lines( file_to_store )
      @stored_lines = ""
      File.open(file_to_store, "r").each do |line|
        @stored_lines <<  line
      end
   end

   def write_file( file_to_write )
      new_file = File.new( file_to_write, "w" )
      @stored_lines.each do |line|
		  new_file.puts line
		end
		new_file.close
   end

  def get_indent( script_node )
	 result = script_node.scan( /(\t*?)import/)
	 #puts result
	 first_item = result[0]
	 #puts first_item
	 ##uts result
	 first_item[0]
  end
  
  def sort_string_array( array )
	 array.sort { |a, b| a.downcase <=> b.downcase }
  end
  
  def get_imports( script_node )
	 scan_results = script_node.scan(/(import .*?)(\r|\n)/)

	 imports = Array.new

	 scan_results.each do |scan|
		imports << scan[0]
	 end

	 imports
  end

  def remove_imports( script_node )
	 stripped = script_node.gsub( /import .*?(\r|\n)/, "" )
	 #puts "stripped: \n#{stripped}\n---\n"
	 #stripped.gsub( /__empty__./m, "")
	 stripped
  end


  def build_string( sorted_import_array, indent )

	 #indent = "***"
	 output_string = ""
	 current_package = ""

	 sorted_import_array .each do |import|

		incoming_package = get_root_package(import)

		if( current_package != incoming_package )
		  output_string << "\n"
		  current_package = incoming_package
		end
		
		output_string << indent + import + "\n"

	 end
	 
	 output_string.chomp!
  end


  def get_root_package( import )
	 import.scan( /import (.*?)\./)[0]
  end
end
