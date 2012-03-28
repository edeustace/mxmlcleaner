require 'fileutils'
include FileUtils

class FileMover
  def initialize
    
  end

  def move_file( fromPath, toPath )
    folderToPathIndex = toPath.rindex( "/" )

    folderToPath = toPath.slice( 0, folderToPathIndex )
    FileUtils.mkdir_p folderToPath
    cp( fromPath, toPath )
    #puts "fromPath: " + fromPath
    File.delete( fromPath )
    #rm( fromPath )
end

  def copy_file_m( fromPath, toPath )
    folderToPathIndex = toPath.rindex( "/" )

    folderToPath = toPath.slice( 0, folderToPathIndex )
    FileUtils.mkdir_p folderToPath
    cp( fromPath, toPath )
end
end
