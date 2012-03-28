# To change this template, choose Tools | Templates
# and open the template in the editor.

$:.unshift File.join(File.dirname(__FILE__),'..','lib')

require 'test/unit'
require 'cleaner/mxml_import_organiser'
require 'file/file_mover'

class MxmlImportOrganiserTest < Test::Unit::TestCase

  def test_already_organised_file_doesnt_need_organising
	 file = "test/mock/cleaner/import_organiser/DoesntNeedOrganising.mxml"
	 organiser = MxmlImportOrganiser.new
	 assert_false organiser.needs_organising? file
  end

	def assert_false( boolean )
	  assert( !boolean )
	end
  def test_tidy_imports_1
		file = "test/mock/cleaner/import_organiser/TestBackgroundView.mxml"
		expected = ["com.test.common.model.pm.background.TestBackgroundPM;",
						"com.test.common.nav.NavigationManager;"]
		test_organised_imports(file, expected)
  end

  def test_tidy_imports_2

		test_organised_imports("test/mock/cleaner/import_organiser/MxmlWithNoScriptNode.mxml", Array.new)
  end

  def test_tidy_imports_3
		file_to_organise = "test/mock/cleaner/import_organiser/PromotionThumbnailTileView.mxml"
		expected_imports = ["com.test.frontpage.model.content.promotion.Promotion;",
								  "com.test.frontpage.model.pm.PromotionThumbnailPM;",
								  "",
								  "flash.display.*;"]
		test_organised_imports( file_to_organise, expected_imports )
  end
  
  def test_tidy_imports_4
		file_to_organise = "test/mock/cleaner/import_organiser/TestTeaserView.mxml"
		expected_imports = ["com.test.common.model.content.news.teaser.NewsTeaser;",
								  "",
								  "mx.formatters.DateFormatter;" ]

	 test_organised_imports( file_to_organise, expected_imports )
  end

  private
  def test_organised_imports( source_file, expected_imports )
	 original_file = source_file
    copied_file = source_file.chomp(".mxml") + "_cleaned.mxml"
    mover = FileMover.new
    mover.copy_file_m( original_file, copied_file);

    organiser = MxmlImportOrganiser.new
    organiser.tidy_imports( copied_file )

	 found_imports = get_lines_from_file( copied_file, expected_imports.length )

	 assert_equal expected_imports.length, found_imports.length
	 index = 0
	 expected_imports.each do |import|
		if( !import.empty? )
		  found_import_string = found_imports[index].scan(/import .*?;/)[0]
		  expected = "import " + import
		  #puts "expected: #{expected} / found: #{found_import_string}"
		  assert_equal expected, found_import_string
		end

		index += 1
	 end
  end

  def get_lines_from_file( file, number_of_lines_from_first_import )
	 no_of_lines_added = 0
	 begin_adding_lines = false
	 stop_adding_lines = false
	 lines_found = Array.new

	 file = File.open( file, "r")
	 
	 file.each_line do |line|

		if( line.include?("import "))
			begin_adding_lines = true
		end

		if( no_of_lines_added >= number_of_lines_from_first_import )
		  stop_adding_lines = true
		end

		if( begin_adding_lines && !stop_adding_lines)
		  lines_found << line
		  no_of_lines_added += 1
		end

	 end
	 
	 lines_found
  end

end
