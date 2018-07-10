(ns eff.helpers
  (:require
    [eff.wordlist :as wordlist]))

(defn generate-word
  "Generate a symbol like :15436 and return its associated word from the wordlist"
  []
  (as->  (repeatedly 5 #(+  (rand-int 6) 1)) n
    (clojure.string/join  "" n)
    (keyword n)
    (n wordlist/wordlist)
    ))

(defn generate-passphrase
  "Generate a passphrase of a given word count"
  [number-of-words]
  [(repeatedly number-of-words #(generate-word)) []]
  )

(defn generate-a-length
  "Generate a passphrase of a given length"
  [length]
  (loop  [passphrase []
          invalid []
          ]
    (let [pass-length (count (clojure.string/join "" passphrase))]
      ( if  (= pass-length length)
        (do
        ;;(println  "found a phrase with given length" passphrase pass-length)
        ;;(println  "invalid passphrases:" invalid)
        [passphrase invalid])
          (if (< pass-length length)
            (do
             ;; (println "new word" passphrase pass-length)
              (recur  (conj passphrase  (generate-word)) invalid))
            (do
           ;; (println "trop grand" passphrase pass-length)
            (conj invalid passphrase)
            (recur [] (conj invalid passphrase))
            )
          )))))


(defn generate-passphrase-length
  "Generate a passphrase of a given word count and a given length"
  [number-of-words passphrase-length]
  (do 
  (if ( (not nil?) number-of-words )
    (if ( (not nil?) passphrase-length )
  (loop [new-pass (generate-passphrase number-of-words)
         invalid []]
    (let [length (count (clojure.string/join "" (first new-pass)))]
      (if (= length passphrase-length)
        [(first new-pass) invalid]
        (do
          (println new-pass length "not good")
          (recur (generate-passphrase number-of-words) (conj invalid (first new-pass)))
          )
        )))
  (generate-passphrase number-of-words)
  )
(if ( (not nil?) passphrase-length)
  (generate-a-length passphrase-length)
  ))
    
 ) )
