require 'cleaner/mxml_cleaner'
require 'cleaner/namespace_organiser'
require 'cleaner/mxml_import_organiser'

#execute the script from one level up
Dir.chdir ".."

cleaner = MxmlCleaner.new
namespace_organiser = NamespaceOrganiser.new
import_organiser = MxmlImportOrganiser.new

src_path = ARGV[0]

puts src_path

Dir.chdir(src_path) 
Dir[ '**/*.mxml'].each do |path|
   #puts "path: #{path}"
   cleaner.clean_file(path)
	namespace_organiser.sort_root_namespaces(path)
	import_organiser.tidy_imports(path)
end
