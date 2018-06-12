Feature: Process Files

Scenario: parse a file and identify all macros in the file
Given a file path of "/testdata/testFile.txt"
When the macro prefix is "!---="
And the macro postfix is "=---!"
Then the output of processing the macro is:
"""
        this.macroText = foundMacro;
        this.foundOnLineNumber = lineCount;
        this.parsed=false;
        this.macroCommandText="";

"""

Scenario:  parse a file and process all macros in the file extracting the text identified by the macro to a given directory
Given an input directory path of "/testdata/input"
And an output file path of "/testdata/output/"
And a file matching regular expression of "^.*\.txt$"
When the macro prefix is "!---="
And the macro postfix is "=---!"
Then the output directory will contain the following files:
   |filename|
   |constructorAndMacroCodeMethod.txt|
   |sourceMacroConstructorBody.txt|
   |sourceMacroConstructorBody.txt|
   |macroCodeMethod.txt|


Scenario:  parse a file and process all extra files
   Given an input directory path of "/testdata/input/import/subfolder"
   And an output file path of "/testdata/output2/"
   And a file matching regular expression of "^.*\.java$"
   And an extra file of "/testdata/input/datfiles/input.dat"
   When the macro prefix is "!---="
   And the macro postfix is "=---!"
   Then the output directory will contain the following files:
      |filename|
      |textFromDatFile.txt|

#Scenario: use a property file to control the application
#Given a property file like:
#"""
#input_directory=/testdata/input
#output_directory=/testdata/output
#matching_files=^.*\.txt$
#macro_prefix=!---=
#macro_postfix=---!
#"""
#Then the output directory will contain the following files:
#    |filename|
#    |constructorAndMacroCodeMethod.txt|
#    |sourceMacroConstructorBody.txt|
#    |sourceMacroConstructorBody.txt|
#    |macroCodeMethod.txt|

#Scenario: parse a directory and copy all contents of directory to a new directory removing all macros from files with specified prefixes (e.g. .java)
#Scenario: report is generated of changed macro files
