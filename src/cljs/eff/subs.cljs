(ns eff.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :message-name
 (fn [db]
   (:message-name db)))

(re-frame/reg-sub
 :message-body
 (fn [db]
   (:message-body db)))
(re-frame/reg-sub
 :hashes
 (fn [db]
   (:hashes db)))



(re-frame/reg-sub
 :passphrase
 (fn [db]
   (:passphrase db)))

(re-frame/reg-sub
 :invalid
 (fn [db]
   (:invalid db)))


(re-frame/reg-sub
 :passphrase-length
 (fn [db]
   (:passphrase-length db)))



(re-frame/reg-sub
 :number-of-words
 (fn [db]
   (:number-of-words db)))


(re-frame/reg-sub
    :active-panel
      (fn  [db]
            (:active-panel db)))
