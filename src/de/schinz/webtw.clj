(ns de.schinz.webtw
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer [defroutes GET POST context]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [de.schinz.tw-backend.core :as tb]
            [de.schinz.views.core :as vc]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]
            [de.schinz.views.newtask :as vn]
            [de.schinz.views.tasks :as vt]
            [de.schinz.views.inbox :as vi]))

; atom containing the server
; see https://practical.li/blog/posts/clojure-web-server-cli-tools-deps-edn/
(defonce ^:private api-server (atom nil))

; Routes for the app
(defroutes app-routes
  (GET "/" [] vc/home)
  (context "/tasks" []
           (GET "/" [& a] (vt/tasks-page a))
           (GET "/filtered" [& a] (vt/filtered-tasks a))
           (GET "/inbox" [& a] (vi/inbox-page a))
           (GET "/next" [] tb/get-next)
           (GET "/new" [] vn/newtask)
           (POST "/create" [] vn/create)
           (GET "/:id" [id] (tb/get-tasks {:uuid id})))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn authenticated? [user pass]
  (let [u (or (System/getenv "BASIC_AUTH_USER") "bau")
        p (or (System/getenv "BASIC_AUTH_PASS") "bap")]
    (and (= user u)
         (= pass p))))

; join routes and wrap in ring defaults
(def app (-> app-routes
             (wrap-defaults site-defaults)
             (wrap-basic-authentication authenticated?)))


(defn stop-server
  "Gracefully shutdown the server, waiting 100ms "
  []
  (when-not (nil? @api-server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@api-server :timeout 100)
    (reset! api-server nil)))

(defn -main
  "Start a httpkit server with a specific port
  #' enables hot-reload of the handler function and anything that code calls"
  [& {:keys [ip port]
      :or   {ip   "0.0.0.0"
             port 4040}}]
  (println "INFO: Starting httpkit server on port:" port)
  (reset! api-server (server/run-server app {:port port})))
