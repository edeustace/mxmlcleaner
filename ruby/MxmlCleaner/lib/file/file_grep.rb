# To change this template, choose Tools | Templates
# and open the template in the editor.

class FileGrep
  def initialize
    
  end

  def grep( file_path, pattern )
    f = File.open( file_path )
    ##puts "looking for: #{pattern}"
	  result = f.grep( /#{pattern}/ )
    f.grep( pattern ) do |found|
      ##puts "#{found}"
    end
    f.close
    result
  end

  def contains?(file_path, pattern)
    return grep( file_path, pattern).length >= 1
  end
end
