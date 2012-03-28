require 'util/string_util'
require 'test/unit'
require 'cleaner/attribute'
require 'rexml/document'
require 'cleaner/rules_reader'



class NamespaceOrganiser

  UTF_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
  TAG_DECLARATION = /(<[^!].*?>)/m
  
  def initialize( rules_file = "lib/cleaner/rules/namespace_rules.txt" )
      reader = RulesReader.new( rules_file )
      @rules_array = reader.rules_array
  end

    def needs_organising?( file )
        file_string = get_file_as_string( file )
        root_opening_tag = get_root_tag( file_string )

        if( is_only_one_closed_tag?( root_opening_tag ) )
            #puts "its only got one closed tag, nothing to clean"
            return false
        end
        
        root_name = get_root_name( root_opening_tag )
        attributes_string = get_root_attributes( root_opening_tag, root_name )
       
        raw_attributes = get_attribute_array(attributes_string)
        sorted_attributes = raw_attributes.sort

        counter = 0

        raw_attributes.each{ |item|
            raw_item = raw_attributes[ counter ].to_s
            sorted_item = sorted_attributes[ counter ].to_s

            if( raw_item != sorted_item  )
                #puts "raw [#{raw_item} != sorted[#{sorted_item}]]"

                #puts "__"
                #puts "#{raw_attributes}"
                #puts "#{sorted_attributes}"
                #puts "__"
                return true
            end

            counter += 1

        }

        if( raw_attributes == sorted_attributes )
            #puts "no need to organise"
            return false
        end

        true
  end
  def prune( string )
      string.chomp!
      string.strip!
  end
  def sort_root_namespaces( file )
	 #puts "!!! sorting: #{file}"

      if( !needs_organising?(file))
          return
      end
    file_string = get_file_as_string( file )
    root_opening_tag = get_root_tag( file_string )
    ##puts "root_opening_tag: #{root_opening_tag}"

	 if( is_only_one_closed_tag?( root_opening_tag ) )
		#puts "its only got one closed tag, nothing to clean"
		return
	 end
    root_name = get_root_name( root_opening_tag )
    #puts "root_name: #{root_name}"
    raw_attributes = get_root_attributes( root_opening_tag, root_name )

    sorted_attributes = sort( raw_attributes )
    remainder = file_string.gsub( root_opening_tag, "")
    remainder.gsub!( UTF_DECLARATION.chomp, "")
    new_file = File.new( file, "w" )
    out_string = UTF_DECLARATION + "\n"
    out_string +="<#{root_name} \n"
    out_string += sorted_attributes
    out_string += ">"
    new_file.puts( out_string + remainder )
    new_file.close
  end

  def is_only_one_closed_tag?( tag_string )
	 tag_string.include?( "/>" )
  end

  def get_root_tag( file_string )
	 if( file_string.include?(UTF_DECLARATION))
		 return _scan( file_string, /<\?xml version="1\.0" encoding="utf\-8"\?>.*?(<[^!].*?>)/m )
	 else
	    return _scan( file_string, TAG_DECLARATION )
	 end
  end

  def get_root_name( search_string )
    _scan( search_string , /<(.*?)[\s|\t|\r]/ )
  end

  def get_root_attributes( search_string, root_name )
    result = search_string.scan( /<#{root_name}[\s|\t|\r](.*?)>/m )
    result[0][0]
  end

  def get_file_as_string(filename)
	 StringUtil.get_file_as_string(filename)
   end

  def sort( unsorted_namespaces )

    #matches = unsorted_namespaces.scan(/(.*?)="(.*?)"/)
    #namespace_list = Array.new
    #matches.each do |match|
      
    #clean_name = clean( match[0])
    #clean_value = clean( match[1])

    #namespace_list << Attribute.new( clean_name, clean_value )
      
    #end

    sorted = get_attribute_array(unsorted_namespaces).sort
    return_string = ""
    sorted.each do |attribute|
      return_string << "\t" + attribute.to_s + "\n"
    end
    return_string.chomp!
  end

  def get_attribute_array( namespace_string )
    matches = namespace_string.scan(/(.*?)="(.*?)"/)
    list = Array.new
    matches.each do |match|

        clean_name = clean( match[0])
        clean_value = clean( match[1])

          list << Attribute.new( clean_name, clean_value, @rules_array )

    end

    list
  end

  def clean( str )
    str.lstrip
    str.strip
  end

  private
  def _scan( search_string, regex )
    scan_result = search_string.scan( regex )

	 if( scan_result.empty? )
		return nil
	 end
	 
    scan_result[0][0]
  end
end


