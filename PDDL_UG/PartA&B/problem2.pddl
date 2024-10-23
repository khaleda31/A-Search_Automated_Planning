(define (problem windfarm-mission-2)
    (:domain windfarm)

    (:objects
        uuv1 - uuv
        ship1 - ship
        w1 w2 w3 w4 w5 - waypoint
        img1 sonar1 - data
        sample1 - sample
    )

    (:init
        ; UUV has not been deployed
        (notdeployed uuv1)
        
        ; UUV starts at the ship
        (at-ship uuv1 ship1)
        
        ; UUV has available memory to collect data
        (memory-available uuv1)
        
        ;initialise that the UUV's hands are empty to collect the sample
        (hand-empty uuv1)
        
        ; Initialise that the ship is empty
        (ship-empty ship1)
        
        ; Connections between waypoints
        (connected w1 w2)
        (connected w1 w4)
        (connected w2 w3)
        (connected w3 w5)
        (connected w4 w3)
        (connected w5 w1)
        
        ;Image is at waypoint 5
        (target-at img1 w5)
        
        ;Sonar scan at waypoint 3
        (target-at sonar1 w3)
        
        ;Sample is at waypoint 1
        (target-at sample1 w1)
    )

    (:goal
        (and
            ; UUV has collected an image at waypoint 3
            (data-transmitted img1)

            ; UUV has performed a sonar scan at waypoint 4
            (data-transmitted sonar1)
            
            ;UUV has collected a sample at waypoint 1 
            (sample-collected sample1)
        )
    )
)
