(define (problem windfarm-mission-1)
    (:domain windfarm)

    (:objects
        uuv1 - uuv
        ship1 - ship
        w1 w2 w3 w4 - waypoint
        img1 sonar1 - data
    )

    (:init
        ; UUV has not been deployed
        (notdeployed uuv1)
        
        ; UUV starts at the ship
        (at-ship uuv1 ship1)
        
        ; UUV has available memory to collect data
        (memory-available uuv1)
        
        ; Connections between waypoints
        ;(connected ship1 w1)
        (connected w1 w2)
        (connected w2 w1)
        (connected w2 w3)
        (connected w3 w4)
        (connected w4 w3)
        (connected w4 w1)
        
        ;Image is at waypoint 3
        (target-at img1 w3)
        
        ;Sonar scan at waypoint 4
        (target-at sonar1 w4)
    )

    (:goal
        (and
            ; UUV has collected an image at waypoint 3
            (data-transmitted img1)

            ; UUV has performed a sonar scan at waypoint 4
            (data-transmitted sonar1)
            
        )
    )
)
