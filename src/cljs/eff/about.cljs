(ns eff.about
  (:require [re-frame.core :as re-frame]
            [eff.ui :as ui])
  )

(defn about []
  [:div
   (ui/hero "is-light" "Welcome to IOTApassphra.se" "Why would you use this website, how to use it and how to create it.")
   [:div.content.section.container


    [:h1 " What is this site, and why would I use it?"]


    [:p "This site is both a " [:strong "IOTA seed generator and passphrase generator"] " and a " [:strong "learning and teaching project about IOTA and functional programing using Clojurescript."]] 
    [:p "I made this site as my first Clojurescript project. Clojurescript is a functional language. If you heard of Abra (which will be a trinary programing language used for IOTA Qubic) and are interested in it and never tried functional programing, the Tutorial section can, I hope, help you grasp the concepts of functional programing so you have an easier time using Abra when it comes out."]
    [:p "This site is also a way to dabble in IOTA programing using the API. It uses IOTA for the comments section, allowing us to store comments on the Tangle, and also uses IOTA when requesting a transaction to try a seed (see more in the how to page)."]
    [:p "If you are reading this and asking yourself 'WTF is IOTA?', go read this."]



    [:h1 "What is a seed?"]


    [:p "With IOTA, your seed is the equivalent of your username and password combined. Anyone having access to your seed has access to your IOTAs.  A seed is a combination of 81 letters from A to Z and 9s." ]
    [:p "The most secure way to create a seed is to use an offline linux and type in the terminal :" ]
    [:pre.has-text-centered "cat /dev/urandom |tr -dc A-Z9|head -c$ {1:-81} " ] 
    [:p "Which gives you something like : " ]
    [:pre.has-text-centered "9TP9GQJQQKY9NAYMIFSJVUBYUEHCYYVW9FVAVUBLEXEQWJACUVGAPMWUOYPZMVNOXRXQODZMIKEDMHMWG" ]
    [:p "This 81-letters string is your seed to access your funds. Anyone having access to this seed can steal your funds."]
    [:p "Another solution is to use the Android and iOS Trinity wallet to generate a seed, but you still have to write it down (or use a password manager)"]


    [:h1 " So why should I use this site?"]


    [:p "If you have generated a seed as above, maybe you will think like me and find it nearly impossible to use without copy-pasting it (if you don't use a password manager). With the trinity wallet, you can use a password to access the seed (or seeds, you can have multiple seeds behind one password), but you need to have a copy of the seed in case you loose your phone or you need to reinstall Trinity. If you have a string of 81 letters or 9s, good luck inputing it on Trinity :)" ]
    [:p "In the future, let's say you are travelling in another country and you need to use IOTA. Can you easily use your seed by heart? Can you easily type it in a wallet? Of course you can use Trinity and use a passphrase to have your seed stored on your smartphone, but lets say you lost it or it was stolen. Wouldnt it be easier to have a seed consisting of a sequence of words. something like :"]
    [:pre.has-text-centered "broadcast aspire dangle escalate poster dollop stiffness january phobia recluse lilly buffed" [:br] "BROADCASTASPIREDANGLEESCALATEPOSTERDOLLOPSTIFFNESSJANUARYPHOBIARECLUSELILLYBUFFED"]
    [:p "You then need to only remember 10 to 15 words, randomly taken from a known wordlist, to have instant access to your funds anytime, anywhere. It is also a lot easier to store (on paper or engraving on metal), and easy to type in a wallet."]


    [:h1 "But what about security? Is it safe?"]


    [:p "Maybe your asking yourself OK but is a seed created like that secure enough? If everyone knows the wordlist, is it possible to do a dictionary attack to find your seed?"]
    [:p "Maybe you've heard of passphrases before, or the DICEWARE method of creating passphrases by rolling dices?"]
    [:p "This method consists of randomly choosing words from a dictionary file of 7776 words, each assigned to a 5 digit number. By rolling 5 dices, you generate a sequence corresponding to a word. For example, the word associated with 21131 is 'clocke'. We can use this to generate a sequence of words with a total length of 81, acting as a IOTA seed."]
    [:p  "I decided to use the EFF wordlist, which is a modified Diceware list. Read this if you want to know the changes and improvements made from the Diceware list. The site generates a sequence of words with a combined length of 81 characters (you can choose to add 9s in the seed for extra security, see more details in the How To page). Of course a passphrase with 10 words taken from a list is less secure than 81 random characters taken from A-Z and 9. But I think it is secure enough. You have to make your own opinion."]
    [:p  "In the dictionary file, there is 7776 words known by everyone. Each word is assigned ~12.92 bits of entropy (Math.log2(7776)). The total entropy of your passphrase or seed is the" [:strong " sum of the entropy in each word." ] ]
    [:p "For example, a 10 words seed has an entropy of 129 bits (your seed will be at least 10 words but the more the better, choose a 14 words seed if you can). 10 words means a total possible phrases of 7776^10. If an attacker knows that your seed is created with this wordlist AND the number of words of your seed AND there is no 9 ( just adding even a 9 increases security) it would take ~12,815,215,586,389,586,504 years to crack it. FOR ME, it is secure enough."]


    [:h1 "But is is an online seed generator..."]


    [:article.message.is-dark
     [:div.message-body "One of the first thing you will be told or read is NEVER USE AN ONLINE SEED GENERATOR. This is good advice because a lot of scam generators stole a lot of funds by storing generated seeds for a long period of time, and then stealing all of the funds fronm these seeds." [:br] "If you have any doubt about this site or the security of this type of seed after reading this page or you don't understand what's happening in the code and dont trust it, DON'T USE THIS SITE TO GENERATE A SEED THAT YOU WILL USE FOR GOOD. You can however USE IT TO TRY OUT IOTA, testing how it works, requesting a transaction or trying out Trinity wallet."] ]

    [:p "If you are asking yourself : Can I trust this site? Will it steal my seed and my funds?"]
    [:p  "This site is client-side only, which means after the first load, it never connects to a server and thus cannot store the seed anywhere where I can retrieve it. You can load this site and go completely offline to generate seeds. As soon as you click on generate again, it will be impossible to retrieve the last seed created, it is never stored anywhere. The only connection to a server (you can check this by going to the console and chek the network tab, more precision in the tutorial section) is when using the IOTA API to retrieve or post comments, or while requesting a transaction. "]
    [:p  "If youre still not convinced or want to dig deeper and understand how this site works, you can check the tutorial on how to create this site (nothing is left out) and check the source code. You can even clone the repo from GHithub and compile it on an offline computer, and run it on your machine offline."]

    [:p "You can also decide to use a traditional seed for storing most of your IOTAs, and using a passphrase seed for your day-to-day wallet. Or if you like this method but don't trust this site, you can use the EFF wordlist by rolling dices and make a passphrase completely offline."]


    [:h1 "OK you got me. I'm in. What do I do?"]


    [:p "Head over to How to and follow the directions to create a seed."]
    ] ]
  )
