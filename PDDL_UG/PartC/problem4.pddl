(define (problem windfarm-mission-1)
    (:domain windfarm-1)

    (:objects
        uuv1 uuv2 - uuv
        ship1 ship2 - ship
        w1 w2 w3 w4 w5 w6 - waypoint
        img1 img2 sonar1 sonar2 - data
        sample1 sample2 - sample
        engineer1 engineer2 - engineer
        bay1 bay2 - bay
        control1 control2 - control
    )

    (:init
         ;Connections between waypoints
        (connected w1 w2)
        (connected w2 w1)
        (connected w2 w3)
        (connected w2 w4)
        (connected w3 w5)
        (connected w4 w2)
        (connected w5 w3)
        (connected w5 w6)
        (connected w6 w4)
        
        ;First UUV 
        ;(notdeployed uuv1) ;uuv1 has not been deployed
        (at uuv1 w2)
        ;(at-ship uuv1 ship1) ;uuv1 is at ship1
        (ship-located ship1 w2) ; to indicate the first ship is at waypoint2
        (ship-empty ship1) ;Ship1 is empty 
        (memory-available uuv1)
        (hand-empty uuv1)
        
        ;Second UUV starts from second ship 
        (notdeployed uuv2) ;uuv2 has not been deployed 
        (at-ship uuv2 ship2) ;uuv2 is at ship2
        (ship-empty ship2) ;ship2 is empty
        (memory-available uuv2)
        (hand-empty uuv2)
        
        ;Image is located 
        (target-at img1 w3)
        (target-at img2 w2)
        
        ;Sonar scan
        (target-at sonar1 w4)
        (target-at sonar2 w6)
        
        ;Collect sample
        (target-at sample1 w5)
        (target-at sample2 w1)
        
        ;Each engineer starts at the bay of each ship
        (eng-at engineer1 bay1 ship1)
        (eng-at engineer2 bay2 ship2)
        
        (uuv-belongs-to uuv1 ship1 bay1 control1)
        (uuv-belongs-to uuv2 ship2 bay2 control2)

    )

    (:goal
        (and
        ; Ensure samples are collected first
        (sample-collected sample1)
        (sample-collected sample2)

        ; Transmit data
        (data-transmitted img1)
        (data-transmitted img2)
        (data-transmitted sonar1)
        (data-transmitted sonar2)
        )
    )
)
