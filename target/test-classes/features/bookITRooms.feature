
  Feature: Rooms
    Background:
      Given user logs in using "sbirdbj@fc2.com" "asenorval"

    @bookITRooms
    Scenario Outline: Rooms info
      Given user is on the "<Rooms>" page
      Then user sees "<Rooms>" header
      Then User go to API "<Rooms>" page
      And API and UI rooms are the same name

    Examples:
      |Rooms|
      |amazon|
      |tesla|