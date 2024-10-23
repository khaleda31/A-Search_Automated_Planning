(define (domain windfarm)
    (:requirements :strips :typing)

    ; -------------------------------
    ; Types
    ; -------------------------------

    (:types
        uuv
        waypoint
        data sample - target
        ship
    )

    ; -------------------------------
    ; Predicates
    ; -------------------------------

    (:predicates
         ; UUV is at the ship
        (at-ship ?u - uuv ?h - ship)
        
        ; Where the ship is located
        (ship-located ?h - ship ?w - waypoint)
        
         ; UUV is at a specific waypoint
        (at ?u - uuv ?w - waypoint)
        
         ; So that UUV has been deployed once
        (notdeployed ?u - uuv)
        
        ; There is a connection between two waypoints
        (connected ?w1 - waypoint ?w2 - waypoint)
        
        ; Image/ Sonar/ Sample at a specific location
        (target-at ?d - target ?w - waypoint)
        
         ; UUV has memory available (only one item can be stored at a time)
        (memory-available ?u - uuv)

        ; UUV holding the data (sample/image/sonar)
        (hold-data ?u - uuv ?d - data)
        
        ; UUV collected sample
        (sample-collected ?p - sample)
        
        ; Ship can store one sample
        (ship-empty ?h - ship)
        
        ; UUV has transmitted data back to the ship
        (data-transmitted ?d - data)
        
       ; UUV has no sample or data
        (hand-empty ?u - uuv)
        
        ; UUV holding sample
        (hold-sample ?u - uuv)

    )

    ; -------------------------------
    ; Actions
    ; -------------------------------

    ; Deploy the UUV from the ship (only once)
    (:action deploy-uuv
        :parameters (?u - uuv ?h - ship ?w - waypoint)
        :precondition (and
            (notdeployed ?u)
            (at-ship ?u ?h)
        )
        :effect (and
            (at ?u ?w)
            (ship-located ?h ?w)
            (not(notdeployed ?u))
            (not (at-ship ?u ?h))
        )
    )

    ; Move UUV from one waypoint to another or from the ship to a waypoint
    (:action move
        :parameters (?u - uuv ?w1 - waypoint ?w2 - waypoint)
        :precondition (and
            (at ?u ?w1)
            (connected ?w1 ?w2)
        )
        :effect (and
            (not (at ?u ?w1))
            (at ?u ?w2)
        )
    )


    ; Take a picture at a specific underwater site
    (:action take-data
        :parameters (?u - uuv ?d - data ?w - waypoint)
        :precondition (and
            (memory-available ?u)
            (at ?u ?w)
            (target-at ?d ?w)
        )
        :effect (and
            (hold-data ?u ?d)
            (not (memory-available ?u)) ; UUV memory is now full
            (not (target-at ?d ?w))
        )
    )
    
    ; Transmit collected data (images or sonar scans) to the ship
    (:action transmit-data
        :parameters (?u - uuv ?w - waypoint ?d - data)
        :precondition (and
            (hold-data ?u ?d)
            (at ?u ?w)
        )
        :effect (and
            (not (hold-data ?u ?d))
            (data-transmitted ?d)
            (memory-available ?u) ; Memory space is free after data transmission
        )
    )
    
    ; UUV collect sample
    (:action collect-sample
        :parameters (?u - uuv ?w - waypoint ?p - sample)
        :precondition (and 
            (at ?u ?w)
            (hand-empty ?u)
            (target-at ?p ?w)
        )
        :effect (and 
            ;(sample-collected ?p)
            (hold-sample ?u)
            (not (hand-empty ?u))
            (not (target-at ?p ?w))
        )
    )
    
    ; UUV returning sample to ship
    (:action return-sample
        :parameters (?u - uuv ?h - ship ?w - waypoint ?p - sample)
        :precondition (and 
            (ship-located ?h ?w)
            (at ?u ?w)
            (hold-sample ?u)
            (ship-empty ?h)
        )
        :effect (and 
            (hand-empty ?u)
            (not (hold-sample ?u))
            (sample-collected ?p)
            (not (ship-empty ?h))
        )
        
    )
)
