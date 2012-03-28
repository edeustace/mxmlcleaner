class Attribute
  attr_reader :name, :value
  attr_writer :name, :value

  BEFORE = -1
  SAME = 0
  AFTER = 1

 
  def initialize( name, value, rules_in )
    @name = name
    @value = value
    @rules = rules_in
  end

  def <=>( incoming )
      self_index = get_index( self.name )
      incoming_index = get_index( incoming.name )

      if( self_index == incoming_index )
          return normal_compare incoming
      end

      return self_index <=> incoming_index
  end

  def get_index( name )
      @rules.each_with_index{ |rule_item, index |

          if( rule_item.class == Regexp  )

              if( name =~ rule_item )
                  ##puts "#{rule_item} found in #{name}"
                  return index
              end
          elsif( rule_item.include? name )
                return index
          end
         

      }

      return @rules.length
  end

  def normal_compare(incoming)
    return @name <=> incoming.name
  end

  def to_s
    "#{@name}=\"#{@value}\""
  end
end
