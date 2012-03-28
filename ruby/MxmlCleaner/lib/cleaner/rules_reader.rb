class String
    def starts_with?(s)
        self[0...(s.size)] == s
    end
end


class RulesReader
   attr_reader :rules_array
   
   def initialize( rules_file = "lib/cleaner/rules/namespace_rules.txt" )
      @rules_array = Array.new

      file = File.new( rules_file )

      file.each_line { |line|
            if( line.starts_with?("#"))
                next
            end
            clean = line.gsub("/","")
            clean.gsub!("\n", "")
            clean.gsub!("\r", "")

            if( line.starts_with? "/" )
                @rules_array << Regexp.new( clean )
            else
                if( clean.include? ",")
                    @rules_array << clean.split(",")
                else
                    @rules_array << clean
              end

            end
      }
       ##puts "rules are: #{ @rules_array }"
  end
end
