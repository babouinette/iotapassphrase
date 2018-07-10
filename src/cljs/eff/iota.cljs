(ns eff.iota
  (:require [cljs-iota.api :as iota-api]
            [cljs-iota.core :as iota]
            [cljs-iota.utils :as iota-utils]
            [cljs-iota.valid :as iota-valid]
            [re-frame.core :as re-frame]
            ))

(def iota-instance  (iota/create-iota  "http://52.232.87.40:80"))

(defn valid-seed?
  "Returns true if a seed is valid."
  [seed]
  (iota-valid/trytes? iota-instance seed 81)  
  )

(defn get-address
 "Get the address corresponding to the index from a seed."
 [seed]
  (iota-api/get-new-address iota-instance seed {:total 1} (fn [err res] (println "Address created: " (first res) ) (first res) ))
  )

(defn to-trytes
 "Encode a message to trytes."
 [message]
  (iota-utils/to-trytes iota-instance message))

(defn from-trytes
 "Decode a message from trytes."
 [trytes]
  (iota-utils/from-trytes iota-instance trytes)
  )

(defn find-transactions
  "Search transactions associated with an address and return a vector of hashes" 
  [address callback]
  (println  "Searching for transactions: ")
  (iota-api/find-transactions iota-instance  {:addresses (list address)} callback))
