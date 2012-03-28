# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
require 'analyser/mxml_base_analyser'

class MxmlStyleAnalyser < MxmlBaseAnalyser
  
   def has_style_tag( file_path )
      file_string = get_file_as_string( file_path )
      match = file_string.match(/<mx:Style>.*?<\/mx:Style>/m)
      match != nil
   end
end
