(ns eff.how
  (:require [re-frame.core :as re-frame]
            [eff.ui :as ui])
  )

(defn how []
    [:div
     (ui/hero "is-success " "How to use?" "How to create a seed and what to do with it!")
     [:div.section.container
      [:div.content


       [:h1 "How to use this site? " ]


       [:p "On the generator page, you can choose between:" ]
       [:ul
        [:li "Generate a IOTA seed" ]
        [:li "Create a passphrase" ]
        ]



       [:h3 "Generate a IOTA seed" ]

       [:p "If you want to generate a IOTA seed, you have two choices:"]
       [:ul
        [:li "With no 9s : in this case a sequence of words is generated from the wordlist. If the sequence length goes over 81, a new sequence is generated until the length is equals to 81. This sequence of words is your new seed." ]
        ]
       [:pre.has-text-centered "UNHITCHED - NEGATIVE - SCRUNCH - PRIMAL - TWICE - WISH - SUBSECTOR - HATCHLING - COLLAPSE - CORNFIELD - SEMINAR" [:br] "UNHITCHEDNEGATIVESCRUNCHPRIMALTWICEWISHSUBSECTORHATCHLINGCOLLAPSECORNFIELDSEMINAR" ]
       [:ul
        [:li "With 9s : if you want extra security, you can choose to add 9s in the seed. Let's call the number you want n. You have two choices:" ]
        [:ul
         [:li "Between words:  In this case, a seed of length (81 - n) is created, then the number 9 is randomly inserted n times between words."]
         ]

        [:pre.has-text-centered "9 - RIFT - CATNIP - SETTING - CLARIFY - 9 - UNABASHED - CULINARY - CURSOR - PORTABLE - TACTICS - 9 - 9 - 9 - GLANCE - FLATWARE" [:br] "9RIFTCATNIPSETTINGCLARIFY9UNABASHEDCULINARYCURSORPORTABLETACTICS999GLANCEFLATWARE" ]
        [:ul
         [:li "Replace letters: in this case words: in this case, a seed of length 81 is created, then n letters are randomly replaced by a 9"]
         ]

        [:pre.has-text-centered "SINISTER - NIBBLE - SPEAK - EXPIRING - AMBER - PAROLE - EXTRADITE - EXCITABLE - UNFLAWED - CRABBING - SHORTNESS" [:br] "SINIST99 - NIBBLE - SPEAK - EXPIRING - AMBER - PAROLE - 9XTRADITE - EXCITABLE - UNFLA9ED - CRABBING - SHORTN9SS" [:br] "SINIST99NIBBLESPEAKEXPIRINGAMBERPAROLE9XTRADITEEXCITABLEUNFLA9EDCRABBINGSHORTN9SS" ]
        ]
     

       [:h3 "Generate a passphrase" ]

    [:p "If you want to generate a passphrase, you can choose the number of words, the length or both."]
    [:ul
     [:li "If you choose a number of words, a sequence of this number of words will be generated, whatever is the length." ]
     [:li "If you choose a length, it will generate words until the length equals or is superior to the wanted length. If it is superior, it generates a new sequence until one is found with the desired length."]
     [:li "If you choose a number of words and a length, a sequence of the chosen number of words is generated, and the length is checked. If is it too low or to high, a new sequence is generated until the length is the given length."]
     ]


    [:h1 "OK I have a seed, what do I do with it?"]


    [:p "You can download the official Trinity wallet for iOS or Android, and for now use the iota desktop wallet while waiting for Trinity Desktop."]
    [:p "The seed you generate is the key to your funds. From this seed, private keys can be created and from each private key an address can be created. Think of an address as a piggy bank. You can safely put money in your piggy bank, but when you want to send money from the bank you need to break it, send money and put the remainder you didn't use in a new one. You can't reuse the broken one. This means that you can safely send several transactions to an address, but as soon as you send funds from this address, you have to use a ne one. Trinity does that behind the scenes for you. An address can be safely shared with everyone, because it's nearly impossible to calculate the private key associated from the address, but it is easy to generate a private key and an address if you have the seed."]
    [:article.message.is-dark
     [:div.message-body "The seed generated has the first addresss generated and displayed. I suggest you don't copy the seed but WRITE IT DOWN. You can safely copy the address and paste it for a payment."] ]
    [:p "If you want to try the seed, you can add it to Trinity. You can either type the words, or if you are just testing and know you won't use the seed for your real funds, you can generate a QR code to scan it in Trinity. This way you don't have to type the seed. You can also use the QR code for the comment seed to check the comments. In trinity, the address you get in the receive tab should be the same as the one displayed. If you click on Request a payment, a 0 value IOTA trnsaction is sent to this address. You should receive the transaction in trinity and see the message."]
    [:p "You can also use the passphrase generator to create a Trinity password to keep your seed(s) safe. It is easier to remember and more secure than classic passwords."]
    ]]] 
    )
