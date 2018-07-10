(ns eff.tutorial
  (:require [re-frame.core :as re-frame]
            [eff.ui :as ui])

  )

(defn tutorial-panel []
  [:nav.panel.is-pulled-left.is-clipped
   [:p.panel-heading "Repository"]
  [:div.panel-block
   [:p.control.has-icons-left
    [:input.input.is-small {:type "text" :placeholder "search"}]
   [:span.icon.is-small.is-left
    [:i.fas.fa-search]] 
    ]]
[:p.panel-tabs
 [:a.is-active "Summary"]
 [:a "Git repo"]
 
 ]
[:a.panel-block.is-active "Directory"

   ]]
  )

(defn tutorial []
  (let  [
         passphrase @(re-frame/subscribe  [:passphrase])
         invalid @(re-frame/subscribe  [:invalid])
         ]
    [:div
     (ui/hero "is-warning " "Tutorial" "How to create this site using CLJS, re-frame, vim, linux and a vps :)")
     [:div.section.container
      [:div.content.has-text-centered.box
       [:p.is-size-1 "SOON "
       [:span.icon.is-large
        [:i.fas.fa-registered]] ]]
     ] ]
    ) )


