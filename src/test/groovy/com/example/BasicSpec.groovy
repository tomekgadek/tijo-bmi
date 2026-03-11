package com.example

import spock.lang.Specification

class BasicSpec extends Specification {

    def "should pass a basic assertion"() {
        expect:
        1 == 1
    }
}
