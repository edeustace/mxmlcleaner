# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

$:.unshift File.join(File.dirname(__FILE__),'..','lib')

require 'test/unit'
require 'analyser/mxml_analyser'

class MxmlAnalyserTest < Test::Unit::TestCase
  
   def setup
      @analyser = MxmlAnalyser.new
   end
   
   def test_has_unused_namespaces
     unused_namespaces = @analyser.unused_namespaces("test/mock/analyser/MxmlViewWithUnusedNamespaces.mxml")
     assert_equal(1, unused_namespaces.length, "should have found one unused namespace")
  end
  
  def test_has_unused_namespaces_no_unused_namespaces
     unused_namespaces = @analyser.unused_namespaces("test/mock/analyser/BaseRegionView.mxml")
     assert_equal(0, unused_namespaces.length, "should have found one unused namespace")
  end
  
  def test_has_unused_namespaces_two
     unused_namespaces = @analyser.unused_namespaces("test/mock/analyser/UnusedNamespaces_two.mxml")
     assert_equal(3, unused_namespaces.length, "should have found one unused namespace")
  end

  def test_has_unused_namespaces_single_node
     unused_namespaces = @analyser.unused_namespaces("test/mock/analyser/TestComboBoxYellow.mxml")
     assert_equal(0, unused_namespaces.length, "should have found no unused namespace")
  end

  def test_has_unused_namespaces_single_node_with_unused
     unused_namespaces = @analyser.unused_namespaces("test/mock/analyser/TestComboBoxYellow_withunused.mxml")
     assert_equal(2, unused_namespaces.length, "should have found 2 unused namespace")
  end

  def test_has_unused_imports
     unused_imports = @analyser.unused_imports( "test/mock/analyser/MxmlViewWithUnusedNamespaces.mxml" )
     assert_equal(6, unused_imports.length )
  end
  
  def test_has_unused_imports_with_same_name_classes
     unused_imports = @analyser.unused_imports( "test/mock/CampaignsTest.mxml" )
     assert_equal(2, unused_imports.length )
  end
  
  def test_has_unused_imports_no_unused_imports
     unused_imports = @analyser.unused_imports( "test/mock/analyser/FoundIssues.mxml" )
     assert_equal(0, unused_imports.length )
  end

  def test_has_unused_imports_image_link_view
	 unused_imports = @analyser.unused_imports( "test/mock/analyser/ImageLinkView.mxml" )
    assert_equal(1, unused_imports.length, "only the 'Link' is unused")
	 assert unused_imports[0].include?( "Link" ), "should only be a Link import that is unused"
  end

  def test_unused_imports_returns_fully_qualified_name
	 unused_imports = @analyser.unused_imports( "test/mock/analyser/ImageLinkView.mxml" )
	 assert_equal(1, unused_imports.length, "only the 'Link' is unused")
	 assert unused_imports[0].include?( "com.test.common.model.content.menu.Link" ), "should contain the fully qualified name for Link "
  end

end
