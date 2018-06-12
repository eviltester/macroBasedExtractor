Feature: Process Macros

  Scenario: Can process text containing a Macro
  Given an input file which contains the following text
    """
    This is the first line
    // !---=getNextLines(3, 3lineBlock)=---!
    This is the third line
    This is the fourth line
    This is the fifth line
    """
  When the macro prefix is "!---="
  And the macro postfix is "=---!"
  Then the output of processing the macro is:
    """
    This is the third line
    This is the fourth line
    This is the fifth line
    """
  And the macro output name is "3lineBlock"


  Scenario: Can process text containing a Macro as the first line
    Given an input file which contains the following text
    """
    // !---=getNextLines(2, 2lineBlock)=---!
    This is the second line
    This is the third line
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is the second line
    This is the third line
    """
    And the macro output name is "2lineBlock"

  Scenario: Can process text containing a Start and end Macro
    Given an input file which contains the following text
    """
    This is before the macro
    // !---=start(this-blocking-section)=---!
    This is in the macro block
    This is also in the macro block
    // !---=end(this-blocking-section)=---!
    This is not contained
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is in the macro block
    This is also in the macro block

    """
    And the macro output name is "this-blocking-section"

  Scenario: Start and End macros Ignore nested Macro blocks
    Given an input file which contains the following text
    """
    This is before the macro
    // !---=start(this-blocking-section)=---!
    This is in the macro block
    // !---=getNextLines(2)=---!
    This is also in the macro block
    // !---=end(this-blocking-section)=---!
    This is not contained
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is in the macro block
    This is also in the macro block

    """
    And the macro output name is "this-blocking-section"

  Scenario: getNextLines macro Ignores nested Macro blocks but they contribute to the line count
    Given an input file which contains the following text
    """
    This is before the macro
    // !---=getNextLines(3, 2LineBlock)=---!
    This is in the macro block
    // !---=start(this-blocking-section)=---!
    This is also in the macro block
    // !---=end(this-blocking-section)=---!
    This is not in the macro block last line
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is in the macro block
    This is also in the macro block

    """

  Scenario: getNextLines macro Ignores nested Macro blocks but they contribute to the line count
    Given an input file which contains the following text
    """
    This is before the macro
    // !---=getNextLines(5, 5LineBlock)=---!
    This is in the macro block 1st line
    // !---=start(this-blocking-section)=---!
    This is also in the macro block 2nd line
    // !---=end(this-blocking-section)=---!
    This is the last thing in the macro block last line
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is in the macro block 1st line
    This is also in the macro block 2nd line
    This is the last thing in the macro block last line
    """


  Scenario: we can ignore lines inside other macros
    Given an input file which contains the following text
    """
    This is before the macro
    This is before the macro block 1st line
    // !---=start(this-blocking-section)=---!
    This is in the macro block as 1st line
    // !---=startignorefor(this-blocking-section)=---!
    I am in the main macro but ignored
    I am ignored also
    I too am ignored
    // !---=endignorefor(this-blocking-section)=---!
    I am not ignored and am in the macro
    // !---=end(this-blocking-section)=---!
    This is the last thing in the macro block last line
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is in the macro block as 1st line
    I am not ignored and am in the macro

    """


  Scenario: we can ignore lines inside other macros with nested ignores
    Given an input file which contains the following text
    """
    This is before the macro
    This is before the macro block 1st line
    // !---=start(this-blocking-section)=---!
    This is in the macro block as 1st line
    // !---=startignorefor(this-blocking-section)=---!
    I am in the main macro but ignored
    // !---=startignorefor(a-different-block)=---!
    I am ignored also
    // !---=endignorefor(a-different-block)=---!
    I too am ignored
    // !---=endignorefor(this-blocking-section)=---!
    I am not ignored and am in the macro
    // !---=end(this-blocking-section)=---!
    This is the last thing in the macro block last line
    """
    When the macro prefix is "!---="
    And the macro postfix is "=---!"
    Then the output of processing the macro is:
    """
    This is in the macro block as 1st line
    I am not ignored and am in the macro

    """

  #TODO: Scenario: warnings are given for duplicate names
  #TODO: Scenario: warnings are given for missing start
  #TODO: Scenario: warnings are given for missing end
  #TODO: Scenario: an importHere("/filepath") macro - imports and creates a startImportHere and endImportHere macro

#  Scenario: we can import files using the importHere macro
#    Given an input file which contains the following text
#    """
#    This is before the macro
#    // !---=importHere(%%user.dir%%/src/test/resources/testdata/input)=---!
#    This is after the macro
#    """
#    When the macro prefix is "!---="
#    And the macro postfix is "=---!"
#    Then the output file to write is:
#    """
#    This is before the macro
#This is an import file
#
#* with a list
#* of things
#    This is after the macro
#    """


