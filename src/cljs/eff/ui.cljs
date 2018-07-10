(ns eff.ui
  (:require
    [re-frame.core :as re-frame]
    [eff.helpers :as helpers]
    [eff.iota :as iota-helpers]
    [eff.routes :as routes]
    [cljs-time.format :as f]
    [cljs.reader :as reader]
    [cljs-time.coerce :as c]
    [cljs-time.core :as t]
    [eff.comments :as comments]
    [reagent.core :as r]
    ))

(def active-tab (r/atom 0))

(defn reformat-string [string vector-of-strings]
   (loop [number-of-letters ( count (first vector-of-strings) )
          rest-of-strings (rest vector-of-strings)
         before  (subs string 0 number-of-letters)
         after  (subs string number-of-letters (inc (count string) ) )
         result [before]
                     ]
     (if (zero? (count rest-of-strings))
       (do
     (prn number-of-letters before after)
     (prn  "resultat final" result )
     result
     )
       (let [
             number-of-letters ( count (first rest-of-strings) )
          rest-of-strings (rest rest-of-strings)
         before  (subs after 0 number-of-letters)
         after  (subs after number-of-letters (inc (count after) ) )
         result (conj result before)
             ]
         (prn  number-of-letters rest-of-strings before after result )
       (prn  "resultat intermediaure" result )
       (recur number-of-letters rest-of-strings before after result )
)
     ) ) )
  

(defn hero
  [color title subtitle]
  [:section.hero {:class color}
   [:div.hero-body
    [:div.container
     [:h1.title  title]
     [:h2.subtitle  subtitle]
     ]]
   ]
  )

(defn replace-by-9  [seed]
              (loop  [
                     index (rand-int (count seed) )
                     letter-to-change (subs seed index (inc index))
                     before  (subs seed 0 index)
                     after  (subs seed (inc index) (inc (count seed))) ]
                 ( if (= letter-to-change "9")
                   (do 
(prn index before letter-to-change after "relaunching") 
                  (let [new-index (rand-int (count seed))]
                   ( recur new-index
                         (subs seed new-index (inc new-index))
                       (subs seed 0 new-index)
                       (subs seed (inc new-index) (inc (count seed)))
                  ) ) )
                   (do 
(prn index before letter-to-change after (str before "9" after) )
    (str before "9" after) )
                  )
                  )
     )  

(defn insert-9-at-index [seed]
  (let [[before after] (split-at (rand-int (inc ( count seed ))) seed)]
    (vec (concat before "9" after) ) ) )


(defn navbar []
  (r/with-let [
               is-active? (fn [panel-to-check] (if (= @(re-frame/subscribe  [:active-panel]) panel-to-check) "is-active"))
               menu? (r/atom false)
               link (fn [panel] [:a.navbar-item {:href  (routes/url-for panel) :class (is-active? panel) :on-click #(do (reset! menu? false) (if (= panel :comments) (comments/get-comments)))} (clojure.string/capitalize (name panel))])
               ]
    [:nav.navbar.is-dark.is-fixed-top
     [:div.container
      [:div.navbar-brand
       [:a.navbar-item {:href "https://bulma.io"}
        [:img {:src "https://iotaprices.com/img/iota_icon_light.svg" :height "300"}] 
        [:p " Passphra.se"]
        ]
       [:a.navbar-burger {:class (if @menu? "is-active") :role "button" :aria-label "menu" :aria-expanded "false" :on-click #(swap! menu? not)}
        [:span {:aria-hidden "true"} ]
        [:span {:aria-hidden "true"} ]
        [:span {:aria-hidden "true"} ]

        ]
       ]
      [:div.navbar-menu {:class (if @menu? "is-active")} 
       [:div.navbar-end
        (link :about)
        (link :how)
        (link :generator)
        (link :tutorial)
        (link :comments)
        ] 
       ]]]
    ) )

(defn submit-comment-button
  [comment]
    [:button.button.is-dark
     {:on-click #(re-frame/dispatch  [:submit-comment comment])}
     "Submit comment"
     ])

(defn passphrase-generator-form []
  (r/with-let [ 
               number-of-words (r/atom nil) 
               passphrase-length (r/atom nil) 
               ]

    [:div.container
     [ :div.field.is-horizontal
      [:div.field-label
       [:label.label "Options"]
       ]
      [:div.field-body
       [:div.field
        [:p.control
         [:input.input {:type "text"
                        :placeholder "Word number"
                        :value @number-of-words
                        :on-change #(reset! number-of-words  (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value) ) )}]]] 
       [:div.field
        [:p.control
         [:input.input {:type "text"
                        :placeholder "Length of seed"
                        :value @passphrase-length
                        :on-change #(reset! passphrase-length  (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value)) )} ]]]
       [:div.field
        [:div.control
         [:button.button.is-dark
          {:on-click #(re-frame/dispatch  [:generate-passphrase (helpers/generate-passphrase-length (if (nil? @number-of-words) nil ( js/parseInt @number-of-words ) ) (if  (nil? @passphrase-length) nil (js/parseInt @passphrase-length) ) )] ) }
          "Generate a passphrase"]] ]
       ]]]
    ) )

               (def qr-code-modal? (r/atom false) )
               (def generate-qr-code? (r/atom false) )

(defn qr-code-modal []
  [:div.modal (if @qr-code-modal? {:class "is-active"} )
   [:div.modal-background ]
    [:div.modal-content
     [:article.message.is-danger
      [:div.message-body.section
       [:h1 "Warning"]
      [:p "Dont generate a QR code if you intend to use the generated seed for good. The QR code generation uses the http://goqr.me/api/ API. To do this, the seed is sent to the API." ]
     [:p "It is okay to generate a QR code if you just want to play with IOTA to try it out, request a payment for the generated seed or fiddle with transactions from and to Trinity."]
      [:div.buttons.is-centered
      [:span.button.is-success
      {:on-click #(do  (reset! generate-qr-code? true)(reset! qr-code-modal? false))}
      "Generate QR code"]
     [:span.button.is-danger
     

      {:on-click #(do (reset! generate-qr-code? false) (reset! qr-code-modal? false)  )}
      "Stop and go back"]
     ]
 
      ] 
  
     ] 
     ]
    [:button.modal-close.is-large {:aria-label "close"
                                   :on-click #(reset! qr-code-modal? false)}]
     ]
  )

(def error-message (r/atom ""))

(defn seed-generator-form []
  (r/with-let [ 
               number-of-9 (r/atom nil) 
               is-checked? (r/atom false) 
               between-true-replace-false (r/atom true)
               ]

    [:div
     [qr-code-modal]
     [ :div.field.is-horizontal
[:div.field-label
       [:label.label "Options"]
       ]
      
      [:div.field-body
       [:div.field

        [:div.control
         [:label.checkbox           [:input {:type "checkbox"
                   :checked @is-checked? 
                   :on-change #(swap! is-checked? not)
                   }]
          " Include 9s?  "

          ] ]]
      [:div.field
        [:div.control
         [:label.radio 
          [:input {:type "radio" :name "choice" :disabled (not @is-checked? ) :on-change #(reset! between-true-replace-false true) :checked @between-true-replace-false}]
" Between words "

          ]
         [:label.radio           [:input {:type "radio" :name "choice" :disabled (not @is-checked? ) :on-change #(reset! between-true-replace-false false) :checked (not @between-true-replace-false )}]
" Replace letters "

          ]
         ]]

       [:div.field
        [:p.control
         [:input.input {:type "text"
                        :disabled (not @is-checked? )
                        :placeholder "Number of 9"
                        :value @number-of-9
                        :on-change #(reset! number-of-9 (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value) ) ) }]]] 
     
       [:div.field
        [:div.control
         (if (and @is-checked? (( not  nil? ) @number-of-9) )
           (if @between-true-replace-false
           [:button.button.is-dark
            {:on-click #(do (reset! generate-qr-code? false) (re-frame/dispatch  [:generate-passphrase 
                                             (let [
                                                   generated ( helpers/generate-a-length (- 81 (js/parseInt @number-of-9) ))
                                                   seed-without-9 (first generated )
                                                   ]
                                               (prn seed-without-9 (count seed-without-9))
                                               ( as-> seed-without-9 n
                                                 (nth (iterate insert-9-at-index n) (js/parseInt @number-of-9) )
                                                 [n seed-without-9]
                                                 ) )
                                             ]) )}

                        "Generate a seed"
            ]
[:button.button.is-dark

            {:on-click #(if (and (> (js/parseInt @number-of-9 ) 0) (< (js/parseInt @number-of-9 ) 81) ( integer? (js/parseInt @number-of-9) ) )
                          (do (reset! generate-qr-code? false)
                         (reset! error-message "") 
                              (re-frame/dispatch  [:generate-passphrase 
                                             (let [
                                                   generated ( helpers/generate-a-length 81 )
                                                   seed-without-9 (first generated )
                                                   ]
                                               (prn seed-without-9 (count seed-without-9))
                                               ( as-> (clojure.string/join seed-without-9) n
                                                 (nth (iterate replace-by-9 n) (js/parseInt @number-of-9) )
                                                 (reformat-string n seed-without-9)
                                                 [n seed-without-9]
                                                 ) )
                                             ]) )
                         (reset! error-message "Number of 9 must be an integer between 1 and 81.") 
                         )
                          }

                        "Generate a seed"
            ] )

           [:button.button.is-dark
            {:on-click #(do (reset! generate-qr-code? false) ( re-frame/dispatch  [:generate-passphrase ( helpers/generate-a-length 81 ) ] ))}
            "Generate a seed"
            ]




           )
         ]]
       ] ]
    
      
     ]
    ) )

(defn generator-tabs []
  [:div.tabs.is-toggled.is-centered.is-medium
   [:ul
    [:li {:class (if (zero? @active-tab) "is-active")}
     [:a {:on-click #(do (reset! active-tab 0) (re-frame/dispatch [:generate-passphrase ["" []]])) }
      [:span.icon.is-small
       [:i.fas.fa-creative-commons-nc {:aria-hidden true}]]
      [:span "Generate a IOTA seed"] 
      ]]
    [:li {:class (if (pos? @active-tab) "is-active")}
     [:a {:on-click #(do (reset! active-tab 1) (re-frame/dispatch [:generate-passphrase ["" []]])) }
      [:span.icon.is-small
       [:i.fas.fa-key {:aria-hidden true}]]
      [:span "Generate a passphrase"] 
      ]] ]
   ]
  ) 

(defn footer []
  [:footer.footer
   [:div.container
    [ :div.content.has-text-centered
     [:p "Made with " [:span.icon.has-text-danger [:i.fas.fa-heartbeat]] " by " [:strong "Babouinette"]] 
     [:p "If you want to donate, send some iotas (or miotas, or even Ti if your feeling generous) to : "]
     [:p [:span.icon [:i.fas.fa-users]] "for the address found : "]
     [:p [:span.icon [:i.fas.fa-user-secret]] "for me : "] ] ] ]
  )

(defn comment-form []
  (r/with-let [ 
               message-name (r/atom nil )
               message-body (r/atom nil)
               message (str  {:name @message-name :comment @message-body} ) 
               encoded-message  (iota-helpers/to-trytes message)
               trytes-count  (count encoded-message)
               decoded  (iota-helpers/from-trytes encoded-message)
               ]
    [:div.has-text-left {:style {:word-wrap "break-word"}} 
     [:div.field
      [:label "Name"]
      [:div.control
       [ :input.input {:type "text"
                       :placeholder "Name"
                       :value @message-name
                       :on-change #(reset! message-name  (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value) ))} ]]]
     [:div.field
      [:label "Message"]
      [:div.control
       [:textarea.textarea {
                            :placeholder "Message"
                            :value @message-body
                            :on-change #(reset! message-body  (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value) ) )}]]] 
     [:p "Your inputs are transformed to a " (count (str  {:name @message-name :comment @message-body} ) ) " bytes long message:"]
     [:p (str  {:name @message-name :comment @message-body} )]
     [:p "It is then encoded to a " (count (iota-helpers/to-trytes (str  {:name @message-name :comment @message-body} ) ) ) " trytes message, which is sent to the tangle as the transaction message:"]
     [:p (iota-helpers/to-trytes (str  {:name @message-name :comment @message-body} ) )]
     [:div.field
      [:div.control
       (submit-comment-button  (iota-helpers/to-trytes (str  {:name @message-name :comment @message-body} ) ))]]
     ]
    ))

(defn comment-form2 []
  (r/with-let [ 
               message-name (r/atom nil )
               message-body (r/atom nil)
               ]
    (let [
               message (str  {:name @message-name :comment @message-body} ) 
               encoded-message  (iota-helpers/to-trytes message)
               ]
    [:div.has-text-left {:style {:word-wrap "break-word"}} 
     [:div.field
      [:label "Name"]
      [:div.control
       [ :input.input {:type "text"
                       :placeholder "Name"
                       :value @message-name
                       :on-change #(reset! message-name  (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value) ))} ]]]
     [:div.field
      [:label "Message"]
      [:div.control
       [:textarea.textarea {
                            :placeholder "Message"
                            :value @message-body
                            :on-change #(reset! message-body  (if (empty? (-> % .-target .-value)) nil (-> % .-target .-value) ) )}]]] 
     [:p "Your inputs are transformed to a " (count message ) " bytes long message:"]
     [:p message]
     [:p "It is then encoded to a " (count encoded-message) " trytes message, which is sent to the tangle as the transaction message:"]
     [:p encoded-message]
     [:div.field
      [:div.control
       (submit-comment-button  encoded-message)]]
     ] )
    ))


(defn comment-display  [comment]
  (let  [comment-parsed  (reader/read-string  (first  (rest comment)) )
         timestamp  (* 1000  (first comment) )
         date  (c/from-long timestamp) ]
    [:div.box
     [:article.media  {:style  {:word-break  "break-word"}}

      [:div.media-content
       [:div.content
        [:h4  [:strong  (:name comment-parsed)]  [:small  " commented on "  (f/unparse  (f/formatters :mysql) date)]]
        [:p  (:comment comment-parsed)]
        ]]]]
    ))

(defn comments-ui  []
  (let  [
         comments @(re-frame/subscribe [:hashes])]
    [:div
     [hero  "is-danger"  "Comments"  "Tell me what you think! The comment section uses IOTA behind the scene"]
     [:div.container.content.section.has-text-centered
      [:div.box
       [:h1.has-text-left  "Post a comment"]
       [comment-form2]
       ]
     [:h5 {:style  {:word-wrap  "break-word"}} "Comment seed : " comments/comment-seed ]
     [:img {:src (str  "https://api.qrserver.com/v1/create-qr-code/?data=" comments/comment-seed "&size=80x80" )}]  
      [:h5 "You can add this seed to trinity and view all the comments transactions, and watch your comment being posted live :)"]
      
      [:div.section
       [:div.notification.is-danger
        (for  [comment comments]
          ^{:key comment}  [comment-display comment] )
        ]

       ]] ]
    ) )
