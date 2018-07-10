(ns eff.core
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [eff.events :as events]
    [eff.views :as views]
    [eff.routes :as routes]
    [eff.config :as config]
    [eff.wordlist :as wordlist]
    ))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "Launching in dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
