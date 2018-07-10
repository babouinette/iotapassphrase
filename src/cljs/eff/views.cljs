(ns eff.views
  (:require
    [re-frame.core :as re-frame]
    [eff.subs :as subs]
    [eff.wordlist :as wordlist]
    [eff.ui :as ui]
    [eff.about :as about]
    [eff.tutorial :as tutorial]
    [cljsjs.kjua :as kjua]
    [eff.iota :as iota-helpers]
    [eff.comments :as comments]
    [eff.how :as how]
    [cljs-iota.api :as iota-api]
    [cljs-iota.utils :as iota-utils]
    [eff.iota :as iota-helpers :refer  [iota-instance]]
    ))

(def payment-seed "EVOLVECANCELTIMINGTASTINGCHILISPEARMINTUNVEILINGBACKSTABFILLERMANNISHACROBATKITTY" )

(defn generator []
  (let [ 
        passphrase @(re-frame/subscribe  [:passphrase])
        seed (clojure.string/upper-case (clojure.string/join  passphrase))
        invalid @(re-frame/subscribe  [:invalid])
        valid-seed?  (iota-helpers/valid-seed? seed) 
        address (if valid-seed? (iota-helpers/get-address seed) "Seed is not valid. Cannot create address." )
        ]


    [:div
     [ui/hero "is-info" "Seed and passphrase generator" "Choose your options and create a seed easily"]

     [ui/generator-tabs]

     [:div.container
      [:div.box.content.has-text-centered
       (if (zero? @ui/active-tab)
         [ui/seed-generator-form]
         [ui/passphrase-generator-form]
         )
       (if (not (clojure.string/blank? @ui/error-message) )
         [:div.section
         [:article.message.is-danger
         [:div.message-body
          [:p @ui/error-message]
          ]] ]
         )
       [:div.section
        [:div.notification.is-light {:style {:word-wrap "break-word"}}

         (if (zero? @ui/active-tab)
           (if (not-empty passphrase)
             [:span
              [:h7 "WORDS (" (count passphrase) "words)"] 
        (if (and ( not-empty invalid) (string? (first invalid)) )
[:div
              [:h9 "With no 9"] 
              [:h4 (clojure.string/upper-case ( clojure.string/join " - " invalid) )] 
              
              [:h9 "With 9"] 
              ]
              )
              [:h4 (clojure.string/upper-case ( clojure.string/join " - " passphrase) )] 
              [:h7 "SEED"] 
              [:h4 seed]
              
              (if (and (not @ui/qr-code-modal?) (not @ui/generate-qr-code?) )
                [:div
              [:a.button.is-outlined.is-text
              {:on-click #(reset! ui/qr-code-modal? true)}
              "Generate a QR code" ]
               ])

              (if (and (not @ui/qr-code-modal?) @ui/generate-qr-code? )
                [:div
[:img  {:src  (str  "https://api.qrserver.com/v1/create-qr-code/?data=" seed  "&size=100x100" )}] ]
)

              [:h7 "ADDRESS"] 
              [:h4 address ]
              [:a.button.is-outlined.is-dark
               {:on-click #(iota-api/send-transfer iota-instance payment-seed 3 14  [{:address address :value 0 :message (iota-helpers/to-trytes "Hello from IOTAPassphra.se. Leave a comment if you want." )}]  (fn  [err res]  (prn res) )) 
                } "Request a payment" ]
              ]
             [:h4 "Choose your options and GENERATE A IOTA SEED."]
             ) )

         ( if ((not zero?) @ui/active-tab)
           (if (not-empty passphrase)
             [:span
              [:h7 "WORDS"] 
              [:h4 (clojure.string/upper-case ( clojure.string/join " - " passphrase) )] 
              [:h7 "EXAMPLES PASSPHRASES"] 
              [:h4 (clojure.string/upper-case ( clojure.string/join " - " passphrase) )] 
              [:h4 (clojure.string/upper-case ( clojure.string/join " - " passphrase) )] 
              [:h4 (clojure.string/upper-case ( clojure.string/join " - " passphrase) )] 
              ]
             [:h4 "Choose your options and GENERATE A PASSPHRASE."]
             ) )
         ]
        
        (if (and ( not-empty invalid) (vector? (first invalid)) )
          [:table.table
           [:thead
            [:tr
             [:th "Invalid seeds"]
             [:th "Word count"]
             [:th "Length"]
             ]]
           [:tbody
            (for  [seed invalid]
              ^{:key seed} 
              [:tr
               [:td (clojure.string/join " " seed)]
               [:td (count seed)]
               [:td (count (clojure.string/join "" seed))]
               ]
              )]])

        ]] ]]))


(defn main-panel []
  (let
    [active @(re-frame/subscribe  [:active-panel])
     ]
    [:div
     [ui/navbar]
     (condp = active             
       :about   [about/about]
       :tutorial   [tutorial/tutorial]
       :how [how/how]
       :generator [generator]
       :comments [ui/comments-ui]
       )
     [:hr]
     [ui/footer] 
     ]))
