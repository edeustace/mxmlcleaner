# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

class MxmlBaseAnalyser
  
  def get_file_as_string(filename)
     data = ''
     f = File.open(filename, "r") 
     f.each_line do |line|
       data += line
     end
     f.close
     return data
   end
end
