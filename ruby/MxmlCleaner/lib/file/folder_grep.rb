# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'file/file_grep'

class FolderGrep
  def initialize
    @file_grep = FileGrep.new
  end

  def contains?( folder_path, pattern, type_array )

    Dir.chdir(folder_path)
    ##puts "looking for: #{pattern}"
    Dir.glob( type_array ).each do |file_path|
      if( @file_grep.contains?( file_path, pattern ))
          #puts "found reference to #{pattern} in: #{file_path}"
          return true
      end
    end
    false
  end

end
