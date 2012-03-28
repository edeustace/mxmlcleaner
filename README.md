#Mxml Cleaner

##Introduction
Some utilities for cleaning up mxml files.

Originally hosted on [google code](http://code.google.com/p/mxmlcleaner/).

It can:

* Remove unused imports
* Organize the remaining imports in alphabetical order
* Remove unused namespaces
* Organize the namespaces (and remaining attributes)

From:

    <mx:Canvas 
      xmlns:mx="http://www.adobe.com/2006/mxml" 
      creationComplete="init()"
      xmlns:view="com.test.common.view.*"
      xmlns:unused="com.test.unused.*"
      >
      <mx:Script>
        <![CDATA[
        import com.test.common.model.content.Menus;
        import com.test.common.model.content.Campaign;
        import com.test.common.Unused;
        
        [Bindable]
        public var campaigns : Campaigns;
        
        [Bindable]
        public var menus : Menus;
        ]]>
      </mx:Script>
      <view:MenuView/>
    </mx:Canvas>
To:

    <mx:Canvas 
      xmlns:mx="http://www.adobe.com/2006/mxml" 
      xmlns:view="com.test.common.view.*"
      creationComplete="init()">
      <mx:Script>
        <![CDATA[
        import com.test.common.model.content.Campaign;
        import com.test.common.model.content.Menus;
        
        [Bindable]
        public var campaigns : Campaigns;
        
        [Bindable]
        public var menus : Menus;
        ]]>
      </mx:Script>
      <view:MenuView/>
    
    </mx:Canvas>

##Ant Task
This Task can be included in your automated build to keep your mxml clean

###Source 
is here: ```scala/mxmlcleaner-task```

###Usage

    <taskdef
        name="mxmlcleaner" 
        classname="com.adobe.ps.ant.mxmlcleaner.MxmlCleanerTask"
        classpath="build/libs/mxmlcleaner-task-1.0-with-dependencies.jar">
    </taskdef>

    <target name="run.clean.task">
        <!-- the rules attribute is optional -->
        <mxmlcleaner
            sourcePath="${SRC_DIR}/"/>
    </target>

##Ruby script
###Source
is here: ```ruby/MxmlCleaner```

###Usage
To run the cleaner do the following:
Requirements: Ruby 1.8.5 or higher

    git clone git://github.com/edeustace/mxmlcleaner.git
    cd mxmlcleaner/ruby/MxmlCleaner
    ruby main.rb ${the_path_to_a_folder_that_contains_mxml_files}
    #ruby main.rb C:\dev\my-project\src\main\flex



##Eclipse plugin (experimental)
This only organises namesapces (flash builder 4 will organise your imports for you).
uby to your Ant classpath to get it working.
Be sure to have a back up of your files before you execute the clean.

#Flash Builder
Currently Flex Builder or Flash Builder doesn't provide this functionality. If they do eventually provide it, it is unknown if it will allow you to execute it in batch mode.

Here are the related issues from bugs.adobe.com - be sure to vote!

Organise imports in FB: http://bugs.adobe.com/jira/browse/FB-5562

Organise namespaces in FB: todo - ed to add

