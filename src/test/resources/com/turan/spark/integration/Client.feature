Feature: Client endpoint
  User must be able to edit and list clients in the Person table with client service

  Scenario:
    Given Person table is empty
    When I insert client with name Pamela and with last name Anderson to the clients
    Then I should list 1 client with the name Pamela and last name Anderson when I list
    When I update the last name of the client number 1 with the name Pamela and last name Anderson to the new last name Ericsson
    Then I should list 1 client with the name Pamela and last name Ericsson when I list
    When I insert another client with name Jennifer and with last name Lopez to the clients
    Then I should list 2 clients when I list with first one being Pamela Ericsson and the second one being Jennifer Lopez
    When I delete the client with the id 2
    Then This time I should list only 1 client with the name Pamela and last name Anderson
    When I clear the Person table
    Then I should list no one when I list

