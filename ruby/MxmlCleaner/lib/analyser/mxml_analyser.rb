# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
require 'analyser/mxml_base_analyser'

class MxmlAnalyser < MxmlBaseAnalyser
  
  ROOT_NODE_REGEX = /<[^?].*?:.*?>/m
  UTF_DECL = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
   
  def initialize
    @namespaces = Array.new
    @unused_namespaces = Array.new
    @imports
    @unused_imports
	 @file_string
  end

   def unused_namespaces( file_path )
      @unused_namespaces = Array.new
      @file_string = get_file_as_string(file_path)
      
      @file_string.gsub!( UTF_DECL, "" )
      root_string = @file_string.slice( ROOT_NODE_REGEX )
      get_all_namespaces( root_string )
      store_unused_namespaces
      @unused_namespaces
   end
   
   def unused_imports( file_path )
      @unused_imports = Array.new
      file_string = get_file_as_string(file_path)
      script_node_match = file_string.match( /<mx:Script>(.*)<\/mx:Script>/m )
      
      if( script_node_match == nil )
         return Array.new
      end
      
      import_matches = script_node_match[0].scan(/import (.*\..*?);/)
      
      @imports = get_array_from_matches( import_matches )
      
      get_all_unused_imports( file_string )
      @unused_imports
   end
   
   private 
   
   def get_array_from_matches( matches_array )
      return_array = Array.new
      matches_array.each do |match|
        return_array << match[0]
      end
      return_array
   end
   
   def get_all_unused_imports( string_to_search )
      @imports.each do |fully_qualified_import|
         class_name = get_class_name( fully_qualified_import )
         matches = string_to_search.scan( /[^\w]#{class_name}[^\w]/ )
         
         if( matches.length <= 1 )
            @unused_imports << fully_qualified_import
         end
      end
      
   end

	def get_class_name( fully_qualified_import )
	  last_dot_index = fully_qualified_import.rindex(".")
	  returnval =  fully_qualified_import.slice(last_dot_index + 1, fully_qualified_import.length)
	  ##puts "class_name: #{returnval}"
	  returnval
	end
   
   def get_all_namespaces( root_string )
      name_space_matches = root_string.scan(/xmlns:(.*?)=/m)
      
      name_space_matches.each do |match|
         @namespaces << match[0]
      end
   end
   
   def store_unused_namespaces
      @unused_namespaces = Array.new
      @namespaces.each do |namespace|
        if( namespace != "mx")
           
           file_matched_close_tag = @file_string.scan(/<\/#{namespace}:/)
           file_matched_open_tag = @file_string.scan(/<#{namespace}:/)

			if( file_matched_close_tag.length == 0 && file_matched_open_tag.length == 0 )
              @unused_namespaces << namespace
           end
         end
        
      end
   end
   
   
  

   
   def has_unused_imports( file_path )
      true
   end
end
