(ns de.schinz.views.inbox
  (:require [de.schinz.tw-backend.core :as twb]
            [de.schinz.views.core :as vc]
            [hiccup.core :as h]))

(defn context-select []
  [:div {:class "ctx-select"}
   [:select
    [:option {:value "chose"} "Chose context..."]
    [:option {:value "HOME"} "HOME"]
    [:option {:value "WORK"} "WORK"]
    [:option {:value "none"} "none"]]])


; returns all inbox items, can also add a filter
(defn inbox-items [& f]
  (let [filters (if (not (empty? f)) (str (first f) ",+in,status:pending") "+in,status:pending")]
    (loop [
           inbox-tasks (twb/task-export filters)
           task-list []]
      (if (empty? inbox-tasks)
        (apply conj [:ul] task-list)
        (recur (rest inbox-tasks)
               (conj task-list [:li (:description (first inbox-tasks))]))))))

(defn inbox [args]
  (let [inbox-items (if (not (nil? (:ctx args)))
                      (inbox-items (:ctx args))
                      (inbox-items))
        ctx-sel (context-select)]
    (h/html [:div {:id "inbox-container"}
             [:div {:id "inbox-menue"} (h/html ctx-sel)]
             [:div {:id "inbox-itemlist"} (h/html inbox-items)]])))

(defn inbox-page [args]
  (vc/page (inbox args)))
