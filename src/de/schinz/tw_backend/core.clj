(ns de.schinz.tw-backend.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as s]
            [cheshire.core :as c]
            [clojure.walk :as w]))

; do a task sync
(defn task-sync []
  (let [res (sh "task" "sync")]
    (when (not (= (:exit res) 0))
      (println "[ERROR] There was an error syncing tasks")
      (println "[ERROR] " (:out res)))))

; export all tasks or taks foung by filter
(defn task-export [& f]
  (let [filters (or (when (not (empty? f))
                      (s/join " " (s/split (first f) #",")))
                    nil)
        task-out (:out (if filters
                         (sh "task" filters "export")
                         (sh "task" "export")))]
    (w/keywordize-keys (c/parse-string task-out))))

; sort tasks by urgency ascending or descending
(defn tsort [t o]
  (cond
    (= o "asc") (sort-by :urgency t)
    (= o "desc") (reverse (sort-by :urgency t))
    :else t))

; get all tasks from taskd sorted by urgency
(defn get-tasks [args]
  (let [tasks (cond
                (not (nil? (:filt args))) (task-export (:filt args))
                (not (nil? (:order args))) (tsort (task-export) (:order args))
                (not (nil? (:uuid args))) (task-export (:uuid args))
                :else (task-export))]
    (c/generate-string tasks)))

; get next task without respecting the context or other filters
(defn get-next [_]
  (c/generate-string
    (first
      (reverse
        (sort-by :urgency (filterv #(= (:status %) "pending") (task-export)))))))

; get all available contexts
(defn contexts []
  (let [ctxs (s/split (:out (sh "task" "_context")) #"\n")]
    (if (s/includes? (first ctxs) ".")
      (into [] (set (mapv #(first (s/split % #"\.")) ctxs)))
      ctxs)))

; get all projects
(defn projects []
  (s/split (:out (sh "task" "_projects")) #"\n"))

; set context to none
(defn set-context-none []
  (sh "task" "context" "none"))

; create new inbox task
(defn create-inbox-task [task]
  (let [task-desc (:task-input task)]
    (set-context-none)
    (sh "task" "add" "+in" task-desc)))

; get context read tags from rc
(defn task-get [cfgopt value]
  (:out (sh "task" "_get" (str "rc." cfgopt "." value))))

