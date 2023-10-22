Feature: Douglas Test

  Background: Open URL
    Given Open the https://www.douglas.de/de/ URL
    Then I see common page
    And I wait dialog element 10 seconds at index 1
    And I click dialog button element
    Then I see home page

  @Test
  Scenario Outline: Parfum Filtering
    When I wait search input element 5 seconds at index 1
    And I click element: parfum tab at index 1

    Then I see parfum page
    And I click "Alle Herrendüfte" labelled tab option
    And I click "close survey" if exists

    Then I wait geschenk fur element 5 seconds at index 1
    And I scroll to the "geschenk fur" element
    And I click geschenk fur element
    Then I click "<geschenkfur>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Then I wait fur wen element 5 seconds at index 1
    And I scroll to the "fur wen" element
    And I click fur wen element
    Then I click "<furwen>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Then I wait marke element 5 seconds at index 1
    And I scroll to the "marke" element
    And I click marke element
    Then I click "<marke>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Then I wait produktart element 5 seconds at index 1
    And I scroll to the "produktart" element
    And I click produktart element
    Then I click "<produktart>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Examples:
    | produktart       | marke        | geschenkfur    | furwen      |
    | Eau de Cologne   | 4711         | Weihnachten    | Unisex      |
    | Eau de Toilette  | Diptyque     | Geburtstag     | Weiblich    |
    | Duftset          | Aigner       | Weihnachten    | Männlich    |


  @Test
  Scenario Outline: Parfum Filtering
    When I wait search input element 5 seconds at index 1
    And I click element: parfum tab at index 1

    Then I see parfum page
    And I click "Alle Herrendüfte" labelled tab option
    And I click "close survey" if exists

    Then I wait fur wen element 5 seconds at index 1
    And I scroll to the "fur wen" element
    And I click fur wen element
    Then I click "<furwen>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Then I wait marke element 5 seconds at index 1
    And I scroll to the "marke" element
    And I click marke element
    Then I click "<marke>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Then I wait produktart element 5 seconds at index 1
    And I scroll to the "produktart" element
    And I click produktart element
    Then I click "<produktart>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Examples:
      | produktart       | marke        | furwen        |
      | Parfum           | Armani       | Weiblich      |
      | Eau de Cologne   | 4711         | Unisex        |
      | After Shave      | Acca Kappa   | Männlich      |

  @Test
  Scenario Outline: Parfum Filtering
    When I wait search input element 5 seconds at index 1
    And I click element: parfum tab at index 1

    Then I see parfum page
    And I click "Alle Herrendüfte" labelled tab option
    And I click "close survey" if exists

    Then I wait fur wen element 5 seconds at index 1
    And I scroll to the "fur wen" element
    And I click fur wen element
    Then I click "<furwen>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Then I wait produktart element 5 seconds at index 1
    And I scroll to the "produktart" element
    And I click produktart element
    Then I click "<produktart>" labelled option
    And I wait schliessen element 5 seconds at index 1
    And I click schliessen element

    Examples:
      | produktart       | furwen      |
      | Eau de Parfum    | Unisex      |
      | Eau de Cologne   | Unisex      |
      | Eau de Toilette  | Unisex      |
