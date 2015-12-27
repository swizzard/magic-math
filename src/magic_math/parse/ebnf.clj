(ns magic-math.parse.ebnf
  (:require [instaparse.core :as insta]))

(def ebnf {:anything "<anything> = #'\\w+'"
           :permanent "permanent = anything* permanent-type
                      <permanent-type> = 'Land' | 'Creature' | 'Enchantment'
                                         'Artifact' | 'Planeswalker' | 'Sorcery'
                                         'Instant'"
           :colors "blue = #'[Bb]lue'
                    black = #'[Bb]lack'
                    green = #'[Gg]reen'
                    red = #'[Rr]ed'
                    white = #'[Ww]hite'
                    artifact = #'artifacts?'"
           :land "land = basic-land | nonbasic-land | 'Forest' | 'Island' |
                        'Mountain' | 'Plains' | 'Swamp'
                  basic-land = #'[Bb]asic land'
                  nonbasic-land = #'[Nn]onbasic land'
                  lands = basic-lands | nonbasic-lands | 'Forests' | 'Islands' |
                          'Mountains' | 'Plains' | 'Swamps'
                  basic-lands = #'[Bb]asic lands'
                  nonbasic-lands = #'[Nn]onbasic lands'"
           :mana-cost "mana-cost = symbol+
                       <symbol> = <'{'> mana-type <'}'>
                       <mana-type> = (colorless | color | split)
                       colorless = #'\\d+' | 'X'
                       <color> = 'B' | 'U' | 'R' | 'G' | 'W' | special
                       special = 'P' | 'S'
                       split = mana-type '/' mana-type"
           :keywords "keyword = kwd gloss?
                      gloss = #'\\((.+\\.)\\)'
                      kwd = atomic | for-cost | targeted | for-n | special
                      <for-cost> = for-cost-kwd mana-cost
                      <targeted> = targeted-kwd target
                      <target> = anything
                      <for-n> = for-n-kwd #'\\d+'
                      atomic = 'Deathtouch' | 'Defender' | 'First Strike' |
                               'Double Strike' | 'Flash' | 'Flying' | 'Haste' |
                               'Hexproof' | 'Indestructible' | 'Intimidate' |
                               'Lifelink' | 'Reach' | 'Shroud' | 'Trample' |
                               'Vigilance' | 'Banding' | 'Battle cry' | 'Cascade' |
                               'Changeling' | 
                      for-cost-kwd = 'Equip' | 'Aura swap' | 'Bestow' | 'Buyback' |
                      targeted-kwd = 'Enchant' |
                      for-n-kwd = 'Scry' | 'Absorb' | 'Amplify' | 'Bolster' |
                                  'Bloodthirst' | 'Bushido' | 
                      special = affinity | bands-with | champion
                      affinity = 'Affinity for' ('artifacts' | lands)
                      bands-with = 'Bands with' target
                      champion = #'Champion an?' target"})

(def parser (insta/parser (reduce str (interpose "\n" (vals ebnf)))))
