(ns eff.comments
  (:require-macros  [cljs.core.async.macros :refer  [go]])
  (:require 
    [cljs-iota.api :as iota-api]
    [cljs.core.async :as async :refer  [<! >! chan]]
    [cljs-iota.utils :as iota-utils]
    [clojure.core.async :as async]
    [re-frame.core :as re-frame]
    [promesa.core :as p]
    [cljs-time.format :as f]
    [cljs.reader :as reader]
    [cljs-time.coerce :as c]
    [cljs-time.core :as t]
    [eff.iota :as iota-helpers :refer [iota-instance]]
    ))


(def comment-seed "99OVERJOYEDSTRATEGYDAYTIMEOVERCOATSIERRAHAZARDDRAGONFLYPREHEATEDCHEWINGEXEMPLIFY9")
(def comment-address (iota-helpers/get-address comment-seed))

(defn transfers [message]
  [{:address comment-address :value 0 :message message}]
  )

(defn message-from-object [object]
  (as->  object n
    (:signature-message-fragment n )
    (clojure.string/join "" (drop-last n) )
    (iota-helpers/from-trytes n) 
    [( :timestamp object ) n]
    ) )

(defn get-objects-callback [err vector-of-objects]
  (let [messages (map message-from-object vector-of-objects)
        sorted-messages (reverse (sort-by first messages) )]
    (do
    (prn "Sorted messages by date: " sorted-messages) 
    (re-frame/dispatch [:hashes-change sorted-messages])
    )
    ) )

(defn find-transactions-callback [err vector-of-hashes]
  (let [list-of-hashes (apply list vector-of-hashes)]
    (prn  "Transaction hashes found" list-of-hashes)
  (iota-api/get-transactions-objects iota-instance list-of-hashes get-objects-callback)
    ))

(defn get-comments []
  (iota-helpers/find-transactions comment-address find-transactions-callback) 
  )


(defn send-comment [message]
  (prn "MESSAGE SENT TO TANGLE")
  (iota-api/send-transfer iota-instance comment-seed 3 14 (transfers message) (fn [err res] (prn res) (get-comments)))
  )
