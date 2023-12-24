(ns de.schinz.views.helpers
  (:require [hiccup.core :as h]))

; return datalist for items with selector name
;<input id="contexts-input" list="contexts-list" name="contexts" hx-get="/tasks" hx-trigger="changed delay:100ms" hx-vals="context: value" hx-target="#tasks-list" hx-swap="outerHTML" hx-push-url="true">
(defn datalist [selector items]
  (let [label-name (str selector "-input")
        id (str selector "-options")
        listname (str selector "-list")
        hx-vals (str selector ": value")]
    [:div {:id id}
     [:label {:for label-name} (str "Choose " selector)] [:br]
     [:input {:list listname :id label-name :name selector
              :hx-get "/tasks/filtered" :hx-trigger "change delay:100ms" :hx-vals hx-vals
              :hx-target "#tasks-list" :hx-swap "outerHTML" :hx-push-url "true"}]
     (loop [items items
            options []]
       (if (empty? items)
         (apply conj [:datalist {:id listname}] options)
         (recur (rest items) (conj options [:option {:value (str (first items))}]))))]))

(defn tasks-list [selector items]
  (let [ul-id (str "list-" selector)]
     (loop [items items
            li-items []]
       (if (empty? items)
         (apply conj [:ul {:id ul-id}] li-items)
         (recur (rest items) (conj li-items [:li (str (:description (first items)))]))))))

