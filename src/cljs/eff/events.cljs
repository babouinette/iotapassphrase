(ns eff.events
  (:require
    [re-frame.core :as re-frame]
    [eff.db :as db]
    [eff.comments :as comments]
    [eff.iota :as iota-helpers]
    ))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    {
     :invalid []
     :passphrase []
     :active-panel :generator
     :hashes ()
     }
    ))

(re-frame/reg-event-db
  :generate-passphrase
  (fn  [db  [_ [new-passphrase invalid]]]
    (assoc db :passphrase new-passphrase :invalid invalid)))

(re-frame/reg-event-fx
  :submit-comment
  (fn  [cofx  [_ encoded-message]]
    (comments/send-comment encoded-message)
    )) 

(re-frame/reg-event-db
  :number-of-words-change
  (fn  [db  [_ new-number]]  
    (assoc db :number-of-words new-number)))

(re-frame/reg-event-db
  :hashes-change
  (fn  [db  [_ new-hashes]]  
    (assoc db :hashes new-hashes)))


(re-frame/reg-event-db
  :message-name-change
  (fn  [db  [_ new-name]]  
    (assoc db :message-name new-name)))

(re-frame/reg-event-db
  :message-body-change
  (fn  [db  [_ new-body]]  
    (assoc db :message-body new-body)))


(re-frame/reg-event-db
  :passphrase-length-change
  (fn  [db  [_ new-length]]  
    (assoc db :passphrase-length new-length)))

(re-frame/reg-event-db
  :set-active-panel
  (fn  [db  [_ value]]
    (assoc db :active-panel value)))
