Feature: Payment Initiation Service

    ####################################################################################################################
    #                                                                                                                  #
    # Single Payment                                                                                                   #
    #                                                                                                                  #
    ####################################################################################################################
    Scenario Outline: Successful payment initiation request for single payments
        Given PSU is logged in
        And <sca-approach> approach is used
        And PSU wants to initiate a single payment <single-payment> using the payment product <payment-product>
        When PSU sends the single payment initiating request
        Then a successful response code and the appropriate single payment response data is delivered to the PSU
        Examples:
            | sca-approach | payment-product       | single-payment                |
            | redirect     | sepa-credit-transfers | singlePayInit-successful.json |

    Scenario Outline: Failed payment initiation request for single payments
        Given PSU is logged in
        And <sca-approach> approach is used
        And PSU wants to initiate a single payment <single-payment> using the payment product <payment-product>
        When PSU sends the single payment initiating request
        Then an error response code is displayed the appropriate error response is delivered to the PSU
        Examples:
            | sca-approach | payment-product      | single-payment                                 |
            | redirect     | sepa-credit-trans    | singlePayInit-incorrect-payment-product.json   |
            | redirect     | sepa-credit-transfer | singlePayInit-incorrect-syntax.json            |
            | redirect     | sepa-credit-transfer | singlePayInit-no-transaction-id.json           |
            | redirect     | sepa-credit-transfer | singlePayInit-no-request-id.json               |
            | redirect     | sepa-credit-transfer | singlePayInit-no-ip-address.json               |
            | redirect     | sepa-credit-transfer | singlePayInit-wrong-format-transaction-id.json |
            | redirect     | sepa-credit-transfer | singlePayInit-wrong-format-request-id.json     |
            | redirect     | sepa-credit-transfer | singlePayInit-wrong-format-psu-ip-address.json |
            | redirect     | sepa-credit-transfer | singlePayInit-exceeding-amount.json            |
            | redirect     | sepa-credit-transfer | singlePayInit-expired-exec-time.json           |

    # TODO Single payment with not existing tpp-transaction-id -> 400  (are there not existant id's / not in the system?)
    # TODO Single payment with not existing tpp-request-id -> 400      (are there not existant id's / not in the system?)
    # TODO Single payment with not existing psu-ip-address -> 400      (are there not existant id's / not in the system?)


    ####################################################################################################################
    #                                                                                                                  #
    # Bulk Payment                                                                                                     #
    #                                                                                                                  #
    ####################################################################################################################
    Scenario Outline: Payment initiation request for bulk payments
        Given PSU is logged in
        And <sca-approach> approach is used
        And PSU wants to initiate multiple payments <bulk-payment> using the payment product <payment-product>
        When PSU sends the bulk payment initiating request
        Then a successful response code and the appropriate bulk payment response data is delivered to the PSU
        Examples:
            | sca-approach | payment-product       | bulk-payment                |
            | redirect     | sepa-credit-transfers | bulkPayInit-successful.json |


    ####################################################################################################################
    #                                                                                                                  #
    # Recurring Payments                                                                                               #
    #                                                                                                                  #
    ####################################################################################################################
    Scenario Outline: Payment initiation request for standing orders
        Given PSU is logged in
        And <sca-approach> approach is used
        And PSU wants to initiate a recurring payment <recurring-payment> using the payment product <payment-product>
        When PSU sends the recurring payment initiating request
        Then a successful response code and the appropriate recurring payment response data is delivered to the PSU
        Examples:
            | sca-approach | payment-product       | recurring-payment          |
            | redirect     | sepa-credit-transfers | recPayInit-successful.json |

    # TODO Recurring payment initiation with not defined frequency -> 400
    # TODO Recurring payment initiation with start date in the past -> 400


    ####################################################################################################################
    #                                                                                                                  #
    # Payment Status                                                                                                   #
    #                                                                                                                  #
    ####################################################################################################################
    Scenario Outline: Successful Payment Status Request
        Given PSU is logged in
        And initiated a single payment with the payment-id <payment-id>
        And created a payment status request with of that payment
        When PSU requests the status of the payment
        Then an appropriate response code and the status <payment-status> is delivered to the PSU
        Examples:
            | payment-id                           | payment-status                |
            | 529e0507-7539-4a65-9b74-bdf87061e99b | paymentStatus-successful.json |

    Scenario Outline: Payment Status Request with not existing Payment-ID
        Given PSU is logged in
        And created a payment status request with of a not existing payment-id <payment-id>
        When PSU requests the status of the payment
        Then an appropriate response code and the status <payment-status> is delivered to the PSU
        Examples:
            | payment-id                           | payment-status                     |
            | 529e0507-7539-4a65-9b74-bdf87061e99b | paymentStatus-not-existing-id.json |
