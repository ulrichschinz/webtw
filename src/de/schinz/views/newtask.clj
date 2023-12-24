(ns de.schinz.views.newtask
  (:require [de.schinz.tw-backend.core :as twb]
            [clojure.string :as s]
            [ring.util.anti-forgery :as anti-forgery]
            [ring.util.response :as resp]
            [de.schinz.views.core :as vc]
            [hiccup.core :as h]))

(defn create [r]
  (let [rparams (:params r)]
    (twb/create-inbox-task rparams)
    (twb/task-sync)
    (resp/redirect "/")))

; contexts available
(defn context-options []
  [:div {:id "ctx-options"}
      [:label {:for "task-ctx-input"} "Choose context"] [:br]
      [:input {:list "task-ctx-list" :id "task-ctx-input" :name "task-ctx"}]
       (loop [contexts (conj (twb/contexts) "none")
              options []]
         (if (empty? contexts)
           (apply conj [:datalist {:id "task-ctx-list"}] options)
           (recur (rest contexts) (conj options [:option {:value (s/capitalize (str (first contexts)))}]))))])

; projects to show or create new one
(defn project-options []
  [:div {:id "project-options"}
   [:label {:for "task-project-input"} "Choose project"] [:br]
   [:input {:list "task-project-list" :id "task-project-input" :name "task-project"}]
   (loop [projects (twb/projects)
          options []]
     (if (empty? projects)
       (apply conj [:datalist {:id "task-project-list"}] options)
       (recur (rest projects) (conj options [:option {:value (str (first projects))}]))))])

; due date select
(defn due-options []
  [:div {:id "due-options"}
   [:label {:for "task-due-input"} "Duedate"] [:br]
   [:input {:type "date" :id "task-due-input" :name "task-due"}]])

; form for new tasks
(defn newtaskform []
  [:div {:id "create"}
    [:form {:action "/tasks/create" :method "post"}
     (anti-forgery/anti-forgery-field)
     [:div {:id "taskdescription"}
      [:label {:for "task-input"} "Task:"] [:br]
      [:textarea {:name "task-input" :type "textfield" :id "task-input"}]]
     [:div {:id "newtask-submit"}
      [:input {:type "submit" :value "Save"}]]]])

(defn newtask [r]
  (vc/page (newtaskform)))

(newtask {})
