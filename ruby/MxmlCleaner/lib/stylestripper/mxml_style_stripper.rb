# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
require 'analyser/mxml_style_analyser'

class MxmlStyleStripper
  
  
   def initialize
     @style_analyser = MxmlStyleAnalyser.new
  end
  
   def clean( file_path, css_file_to_write_to )
      if( !@style_analyser.has_style_tag( file_path ) )
         return
      end
      file_string = @style_analyser.get_file_as_string( file_path )
      extracted_style = file_string.match( /<mx:Style>(.*?)<\/mx:Style>/m)
      
      style_string_to_write = "/*from: #{file_path}*/\n"
      style_string_to_write << extracted_style[1]
      append_file( css_file_to_write_to, style_string_to_write )
      cleaned_string = file_string.gsub( /<mx:Style>.*?<\/mx:Style>/m, "" )
      write_file( file_path, cleaned_string )
   end
   
   def write_file( file_to_write, string_to_write )
      new_file = File.new( file_to_write, "w" )
      new_file.#puts string_to_write
      new_file.close
   end
   
   def append_file( file_to_write, string_to_write )
      new_file = File.new( file_to_write, "a" )
      new_file.#puts string_to_write
      new_file.close
   end
   
end
