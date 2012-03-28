# To change this template, choose Tools | Templates
# and open the template in the editor.

$:.unshift File.join(File.dirname(__FILE__),'..','lib')

require 'test/unit'
require 'cleaner/namespace_organiser'
require 'file/file_mover'

class NamespaceOrganiserTest < Test::Unit::TestCase

    CUSTOM_SORT_RULES = "test/cleaner/custom_sort_rules.txt"
  def test_already_organised_file_doesnt_need_organising
	 file = "test/mock/cleaner/namespace_organiser/DoesntNeedOrganising.mxml"
	 organiser = NamespaceOrganiser.new CUSTOM_SORT_RULES
	 assert_false organiser.needs_organising? file
  end

  def test_file_needs_organising
	 file = "test/mock/cleaner/namespace_organiser/NamespacesNeedOrganising.mxml"
	 organiser = NamespaceOrganiser.new
	 assert organiser.needs_organising? file
  end

  def test_strings_are_equal
        string1 = "xmlns:mx='http://www.adobe.com/2006/mxml'
	xmlns:mxml='flicc.factory.mxml.*'
	xmlns:background='com.com.testmon.view.background.*'
	xmlns:fliccext='com.adobe.fliccext.*'
	xmlns:ioc='com.test.core.model.ioc.*'
	xmlns:loaders='com.com.testmon.loaders.*'
	xmlns:lotto='com.test.lotto.view.*'
	xmlns:urlkit='urlkit.rules.*'
	addedToStage='initMouseHandler()'
	backgroundAlpha='0'
	backgroundImage='null'
	creationComplete='main()'
	frameRate='25'
	height='100%'
	historyManagementEnabled='false'
	horizontalAlign='center'
	horizontalScrollPolicy='auto'
	implements='com.test.printing.IPrintOutputHolder'
	initialize='init()'
	layout='vertical'
	width='100%'"
      string2 = "xmlns:mx='http://www.adobe.com/2006/mxml'
	xmlns:mxml='flicc.factory.mxml.*'
	xmlns:background='com.com.testmon.view.background.*'
	xmlns:fliccext='com.adobe.fliccext.*'
	xmlns:ioc='com.test.core.model.ioc.*'
	xmlns:loaders='com.com.testmon.loaders.*'
	xmlns:lotto='com.test.lotto.view.*'
	xmlns:urlkit='urlkit.rules.*'
	addedToStage='initMouseHandler()'
	backgroundAlpha='0'
	backgroundImage='null'
	creationComplete='main()'
	frameRate='25'
	height='100%'
	historyManagementEnabled='false'
	horizontalAlign='center'
	horizontalScrollPolicy='auto'
	implements='com.test.printing.IPrintOutputHolder'
	initialize='init()'
	layout='vertical'
	width='100%'"
      assert_equal string1, string2
  end


  def assert_false( boolean )
	  assert( !boolean )
  end

  def test_organise_namespaces
    organiser = NamespaceOrganiser.new
    
    unsorted_namespaces = """xmlns:b=\"com.view.b\"
xmlns:a=\"com.view.a\"
xmlns:mx=\"www.macromedia.com\""""
    sorted = organiser.sort(unsorted_namespaces)
    expected = "\txmlns:mx=\"www.macromedia.com\"\n\txmlns:a=\"com.view.a\"\n\txmlns:b=\"com.view.b\""

    #puts sorted
    assert_equal  expected, sorted, "sorting worked"
  end

  def test_organise_all
    organiser = NamespaceOrganiser.new CUSTOM_SORT_RULES

    unsorted_namespaces = """\txmlns:b=\"com.view.b\"
xmlns:a=\"com.view.a\"
width=\"150\" paddingLeft=\"20\"
xmlns:mx=\"www.macromedia.com\"
label=\"hello\""""
    sorted = organiser.sort(unsorted_namespaces)

    expected = """\txmlns:mx=\"www.macromedia.com\"
\txmlns:a=\"com.view.a\"
\txmlns:b=\"com.view.b\"
\twidth=\"150\"
\tlabel=\"hello\"
\tpaddingLeft=\"20\""""


    #puts sorted
    assert_equal  expected, sorted, "sorting worked"

    unsorted_2 = """xmlns:mx=\"http://www.adobe.com/2006/mxml\"
\txmlns:a=\"abc.*\"
\txmlns:view1=\"com.com.testmon.view.*\"
\txmlns:pm=\"com.test.frontpage.model.pm.*\"
\txmlns:regionmenu=\"com.test.view.components.regionmenu.*\"
\tcreationComplete=\"init()\"
\theight=\"100\"
\tpaddingLeft=\"10\"
\tpaddingRight=\"20\"
\ttitle=\"hello\"
\twidth=\"100\""""
    sorted2= organiser.sort( unsorted_2 )

    expected2 =  """\txmlns:mx=\"http://www.adobe.com/2006/mxml\"
\txmlns:a=\"abc.*\"
\txmlns:pm=\"com.test.frontpage.model.pm.*\"
\txmlns:regionmenu=\"com.test.view.components.regionmenu.*\"
\txmlns:view1=\"com.com.testmon.view.*\"
\theight=\"100\"
\twidth=\"100\"
\tcreationComplete=\"init()\"
\tpaddingLeft=\"10\"
\tpaddingRight=\"20\"
\ttitle=\"hello\""""
      
    assert_equal expected2, sorted2, "the ns sorting didn't work"
  end
  def test_sort_root_namespaces

    original_file = "test/mock/cleaner/namespace_organiser/CampaignsTest.mxml"
    copied_file = "test/mock/cleaner/namespace_organiser/CampaignsTest_cleaned.mxml"
    mover = FileMover.new
    mover.copy_file_m( original_file, copied_file);

    organiser = NamespaceOrganiser.new
    organiser.sort_root_namespaces( copied_file )

  end

	def sort_root_namespaces_background
	  file = "test/mock/cleaner/namespace_organiser/TestBackgroundView.mxml"
	  expected_sorting = ["xmlns:mx=\"http://www.adobe.com/2006/mxml\"",
								 "xmlns:comps=\"*\"",
								  "creationComplete=\"getBackgroundColor()\"",
								  "height=\"100%\"",
								  "initialize=\"init()\"",
								  "width=\"100%\""
								  ]
								  test_organised_namespaces( file, expected_sorting )
	end
=begin

	def test_sort_dspopuplib
	 test_organised_namespaces("test/mock/cleaner/namespace_organiser/DSPopup.mxml")
  end

  def test_sort_only_one_node
	 test_organised_namespaces( "test/mock/cleaner/namespace_organiser/HinduComboboxYellow.mxml" )
  end

=end
  private
  def test_organised_namespaces( source_file, expected_sorting )
	 original_file = source_file
    copied_file = source_file.chomp(".mxml") + "_cleaned.mxml"
    mover = FileMover.new
    mover.copy_file_m( original_file, copied_file);

    organiser = NamespaceOrganiser.new
    organiser.sort_root_namespaces( copied_file )

	 amended_array = get_array_of_root_attributes( copied_file )
	 #list the changes
	 index = 0
	 expected_sorting.each do |attribute|
		assert amended_array[index].include?( attribute ), "should have found #{attribute} in #{amended_array[index]}"
		index += 1
	 end
  end

  def get_array_of_root_attributes( mxml_file )
	 file = File.open( mxml_file, "r")
	 file_string = get_string_from_file( file )
	 
  end
end
