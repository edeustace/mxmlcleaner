# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

$:.unshift File.join(File.dirname(__FILE__),'..','lib')

require 'test/unit'
require 'cleaner/mxml_cleaner'
require 'file/file_mover'

class MxmlCleanerTest < Test::Unit::TestCase
   
   @cleaner

  def setup
      @cleaner =  MxmlCleaner.new
  end

  def test_clean
    file_to_clean = "test/mock/cleaner/mxml_cleaner/UnusedNamespaces.mxml"
	 expected_imports = ["com.test.common.model.content.menu.MenuItem;",
								"mx.core.UIComponent;"]

	 test_file_imports(file_to_clean, expected_imports)
  end
  
  

  def test_clean_image_link_view
		file_to_clean = "test/mock/cleaner/mxml_cleaner/ImageLinkView.mxml"
		expected_imports = [
			 "com.test.common.events.GetImageLinkEvent;",
			 "com.test.common.events.NavigateEvent;",
			 "com.test.common.model.content.ImageLink;",
			 "mx.controls.Alert;"]

	 test_file_imports( file_to_clean, expected_imports )
  end

  private
  def test_file_imports( source_file, expected_imports )
	 original_file = source_file
    copied_file = source_file.chomp(".mxml") + "_cleaned.mxml"
    mover = FileMover.new
    mover.copy_file_m( original_file, copied_file);

    @cleaner.clean_file( copied_file )

	 found_imports = get_imports_from_file( copied_file )

	 assert_equal expected_imports.length, found_imports.length

	 expected_imports.each do |import|
		assert array_element_contains_string( found_imports, "import " + import ), "should have found: #{import} in #{found_imports}"
	 end
  end

  def array_element_contains_string( array, string )
	 array.each do |element|
		#puts "searching for: #{string} in #{element}"
		if( element.include?( string ) )
		  return true
		end
	end
	false
  end

  def get_imports_from_file( file )

	 imports = Array.new
	 file = File.open( file, "r")

	 file.each_line do |line|

		if( line.include?("import "))
		  imports << line
		end
	 end
	 
	 imports
  end

end
