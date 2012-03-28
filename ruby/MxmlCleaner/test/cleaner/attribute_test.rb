# To change this template, choose Tools | Templates
# and open the template in the editor.

$:.unshift File.join(File.dirname(__FILE__),'..','lib')

require 'test/unit'
require 'cleaner/attribute'
require 'cleaner/rules_reader'

class AttributeTest < Test::Unit::TestCase

    #a custom set of sorting rules
    CUSTOM_RULES_PATH = "test/cleaner/custom_sort_rules.txt"

    
  def test_basic_sort
      assert_sort(["height","width"] )
  end

  def test_namespace_sort
       #assert_sort("xmlns:mx","xmlns","xmlns:com")
       assert_sort( ["xmlns:mx","xmlns","xmlns:com","xmlns:dev","xmlns:ed"], CUSTOM_RULES_PATH  )
  end
  
  def test_sort_2
       assert_sort( ["xmlns:mx",
                    "xmlns",
                    "xmlns:com",
                    "implements",
                    "height",
                    "width",
                    "change",
                    "creationComplete",
                    "verticalScrollPolicy",
                    "includeInLayout"],
                    CUSTOM_RULES_PATH
                    
                )
  end
  def test_sort_3
       assert_sort( ["xmlns:mx",
                    "xmlns",
                    "xmlns:a",
                    "xmlns:com",
                    "xmlns:mxml",
                    "implements",
                    "height",
                    "width",
                    "change",
                    "creationComplete",
                    "verticalScrollPolicy",
                    "includeInLayout"],
                    CUSTOM_RULES_PATH

                )
  end

   def test_sort_4
       assert_sort( ["xmlns:mx",
                    "xmlns",
                    "xmlns:a",
                    "xmlns:com",
                    "xmlns:mxml",
                    "implements",
                    "height",
                    "width",
                    "change",
                    "creationComplete",
                    "horizontalAlign",
                    "verticalAlign",
                    "verticalScrollPolicy",
                    "includeInLayout"],
                CUSTOM_RULES_PATH

                )
  end
  private

  def assert_sort(  namespaces, custom_rules_path = nil  )
      expected = Array.new
      namespaces.each{ |name|
          expected << attribute(name, custom_rules_path)
      }
      #puts "_"
      #puts "expe: #{expected}"
      shuffled = expected.shuffle
      ##puts "shuffled: #{shuffled}"
      sorted = shuffled.sort
      #puts "sort: #{sorted}"
      #puts "_"

      assert_array(expected, sorted)
  end


  def attribute( name, custom_rules_path )
      if( custom_rules_path == nil )
          reader = RulesReader.new
      else
          reader = RulesReader.new custom_rules_path
      end
      Attribute.new( name, " ", reader.rules_array )
  end

  def create_array( *args )
      result = Array.new
      args.each{ |name|
          result << attribute(name)
         
      }
      result
  end

  def assert_array( expected, actual )
      counter = 0
      expected.each{ |item|
          e = expected[ counter ]
          a = actual[ counter ]

          assert_equal e.to_s, a.to_s
          counter += 1
      }
  end
end

class Array
  def shuffle
    sort_by { rand }
  end

  def shuffle!
    self.replace shuffle
  end
end

