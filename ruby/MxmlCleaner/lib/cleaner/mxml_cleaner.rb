require 'analyser/mxml_analyser'

class MxmlCleaner
   
   DELETE_KEY = "!*delete*!"
  
   @analyser
   @stored_lines
   
   def initialize
      @analyser = MxmlAnalyser.new
      @unused_namespaces
   end
   
   def clean_file( file_path )
      clean( file_path, file_path )
   end
   
   private
   
   def clean( file_to_analyse, file_to_write )
     
      init_unused_arrays
      
      @unused_namespaces = @analyser.unused_namespaces( file_to_analyse )
      @unused_imports = @analyser.unused_imports( file_to_analyse )

	  #analyser should be returning empty arrays
		if( @unused_namespaces == nil )
		  @unused_namespaces = Array.new
		end

	  if( @unused_imports == nil )
		  @unused_imports = Array.new
		end

	  if( @unused_namespaces.empty? && @unused_imports.empty? )
         return
      end
      store_lines( file_to_analyse )
      strip
      write_file(file_to_write)
   end
   
   def init_unused_arrays
      @unused_namespaces = Array.new
      @unused_imports = Array.new
   end
   
   def store_lines( file_to_store )
      @stored_lines = Array.new
      File.open(file_to_store, "r").each do |line|
        @stored_lines <<  line 
      end
   end
   
   def strip
       @stored_lines.each do |line|
          strip_namespaces( line )
          strip_imports( line )
      end
   end
   
   def write_file( file_to_write )
      new_file = File.new( file_to_write, "w" )
      @stored_lines.each do |line|
        #puts "adding: #{line}"
        stripped = line.strip
        stripped.rstrip!
        
        if( !deleted_item_only?( line ) )
           remove_deleted_key( line )
           new_file.puts line
        end
      end
      new_file.close
   end
   
   def deleted_item_only?( line )
       stripped = line.strip
       stripped.rstrip!
       stripped == DELETE_KEY 
   end
   
   def remove_deleted_key( line )
      line.gsub!( DELETE_KEY, "" )
   end

   def strip_namespaces( line )
      @unused_namespaces.each do |namespace|  
         line.gsub!(/xmlns:#{namespace}=".*?"/, DELETE_KEY)
        
      end
   end
   
   def strip_imports( line )
      if( @unused_imports == nil )
        return
      end
      
      @unused_imports.each do |import|
         line.gsub!(/import .*?#{import};/, DELETE_KEY)
      end
   end
end
