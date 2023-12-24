(ns de.schinz.views.tasks
  (:require [de.schinz.views.core :as vc]
            [clojure.string :as s]
            [de.schinz.views.helpers :as helper]
            [de.schinz.tw-backend.core :as tc]
            [hiccup.core :as h]))


(defn tasks-list []
  (loop [contexts (tc/contexts)
         links []]
    (if (empty? contexts)
      (apply conj [:div {:id "contexts"}] links)
      (recur (rest contexts) (conj links [:div {:id "ctx"} [:a {:href (str "/tasks?context=" (first contexts))} (first contexts)]])))))

; tasks wrapper is the html boilerplate
(defn tasks-wrapper [content]
  [:div {:id "tasks"}
   [:div {:id "tasks-menu"} (tasks-list)]
   [:div {:id "tasks-list"} (helper/tasks-list "pending" content)]])

;(let [task-items (tc/task-export "+HOME,status:pending")]
;  (helper/tasks-list "home-pending-tasks" task-items))

(defn context-tags [args]
  (if (nil? (:context args))
    "+PENDING,status:pending"
    (let [ctx-tags (tc/task-get "context" (str (:context args) ".read"))]
      (str "+PENDING," (s/join "," (s/split (s/trim ctx-tags) #"\ ")) ",status:pending"))))

; tasks view
(defn tasks-page [args]
  (let [context (context-tags args)
        project (:project args)]
    (vc/page (tasks-wrapper (tc/tsort (tc/task-export (str context)) "desc")))))

; filterd tasks
(defn filtered-tasks [args]
  (let [context (context-tags args)
        project (:project args)]
    (h/html (helper/tasks-list "pending" (tc/tsort (tc/task-export (str context)) "desc")))))
